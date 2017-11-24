package com.swg.mydouyudemo.net.cookie;

import android.content.Context;

import com.swg.mydouyudemo.utils.LogUtil;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 持久化Cookie管理类
 * Created by swg on 2017/11/23.
 */

public class NovateCookieManager implements CookieJar {

    private static Context mContext;
    private static PersistentCookieStore cookieStore;

    public NovateCookieManager(Context context) {
        mContext = context;
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(context);
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie cookie : cookies) {
                cookieStore.add(url, cookie);
                LogUtil.i(cookie.name() + "=" + cookie.value());
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        for (Cookie cookie : cookies) {
            LogUtil.i(cookie.name() + "=" + cookie.value());
        }
        return cookies;
    }

}
