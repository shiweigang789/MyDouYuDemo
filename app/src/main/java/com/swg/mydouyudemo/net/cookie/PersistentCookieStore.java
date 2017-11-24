package com.swg.mydouyudemo.net.cookie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.swg.mydouyudemo.utils.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 持久化Cookie
 * Created by swg on 2017/11/22.
 */

public class PersistentCookieStore {

    private static final String LOG_TAG = "PersistentCookieStore";
    private static final String COOKIE_PREFS = "CookiePrefsFile";

    private final HashMap<String, ConcurrentHashMap<String, Cookie>> cookies;
    private final SharedPreferences cookiePrefs;

    public PersistentCookieStore(Context context) {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE);
        cookies = new HashMap<>();
        Map<String, ?> prefsMap = cookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            if (entry.getValue() != null) {
                String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
                for (String name : cookieNames) {
                    String encodeCookie = cookiePrefs.getString(name, null);
                    if (encodeCookie != null) {
                        Cookie decodeCookie = decodeCookie(encodeCookie);
                        if (decodeCookie != null) {
                            if (!cookies.containsKey(entry.getKey())) {
                                cookies.put(entry.getKey(), new ConcurrentHashMap<String, Cookie>());
                            }
                            cookies.get(entry.getKey()).put(name, decodeCookie);
                        }
                    }
                }
            }
        }
    }

    protected String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    @SuppressLint("CommitPrefEdits")
    public void add(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);
        if (!cookie.persistent()) {
            if (!cookies.containsKey(url.host())) {
                cookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
            }
            cookies.get(url).put(name, cookie);
        } else {
            if (cookies.containsKey(url.host())) {
                cookies.get(url.host()).remove(name);
            }
        }

        SharedPreferences.Editor editor = cookiePrefs.edit();
        editor.putString(url.host(), TextUtils.join(",", cookies.get(url.host()).keySet()));
        editor.putString(name, encodeCookie(new SerializableOkHttpCookie(cookie)));
        editor.apply();
    }

    public List<Cookie> get(HttpUrl url) {
        ArrayList<Cookie> ret = new ArrayList<>();
        if (cookies.containsKey(url.host())) {
            ret.addAll(cookies.get(url.host()).values());
        }
        return ret;
    }

    public List<Cookie> getCookies() {
        ArrayList<Cookie> ret = new ArrayList<>();
        for (String key : cookies.keySet()) {
            ret.addAll(cookies.get(key).values());
        }
        return ret;
    }

    public String getStringCookie() {
        String cookieString = "";
        for (Cookie cookie : getCookies()) {
            cookieString = cookie.toString();
        }
        return cookieString;
    }

    public boolean remove(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);
        if (cookies.containsKey(url.host()) && cookies.get(url.host()).containsKey(name)) {
            cookies.get(url.host()).remove(name);
            SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
            if (cookiePrefs.contains(name)) {
                prefsWriter.remove(name);
            }
            prefsWriter.putString(url.host(), TextUtils.join(",", cookies.get(url.host()).keySet()));
            prefsWriter.apply();
            return true;
        } else {
            return false;
        }
    }

    public boolean removeAll() {
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        cookies.clear();
        return true;
    }

    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    protected String encodeCookie(SerializableOkHttpCookie cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException in encodeCookie", e);
            return null;
        }
        return byteArrayToHexString(os.toByteArray());
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableOkHttpCookie) objectInputStream.readObject()).getCookie();
        } catch (ClassNotFoundException e) {
            LogUtil.e(LOG_TAG, "ClassNotFoundException in decodeCookie", e);
        } catch (IOException e) {
            LogUtil.e(LOG_TAG, "IOException in decodeCookie", e);
        }
        return cookie;
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

}
