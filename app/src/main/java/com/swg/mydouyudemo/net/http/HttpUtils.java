package com.swg.mydouyudemo.net.http;

import android.content.Context;

import com.swg.mydouyudemo.net.config.NetWorkConfiguration;
import com.swg.mydouyudemo.net.cookie.NovateCookieManager;
import com.swg.mydouyudemo.net.interceptor.LogInterceptor;
import com.swg.mydouyudemo.net.interceptor.NetInterceptor;
import com.swg.mydouyudemo.net.request.RetrofitClient;
import com.swg.mydouyudemo.utils.LogUtil;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Http帮助类
 * Created by swg on 2017/11/23.
 */

public class HttpUtils {

    public static final String TAG = "HttpUtils";
    private static HttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private static NetWorkConfiguration configuration;
    private Context context;
    private NetInterceptor netInterceptor;
    /**
     * 是否加载本地缓存数据，默认为true
     */
    private boolean isLoadDiskCache = true;

    /**
     * ----> 针对无网络情况，是否加载本地缓存
     *
     * @param isCache true为加载，false为不加载
     * @return
     */
    public HttpUtils setLoadDiskCache(boolean isCache) {
        this.isLoadDiskCache = isCache;
        return this;
    }

    /**
     * 是否加载内存缓存数据，默认为false
     */
    private boolean isLoadMemoryCache = false;

    /**
     * 是否加载内存缓存
     *
     * @param isCache true为加载，false为不加载
     * @return
     */
    public HttpUtils setLoadMemoryCache(boolean isCache) {
        this.isLoadMemoryCache = isCache;
        return this;
    }

    /**
     * 获取请求网络实例
     *
     * @return
     */
    public static HttpUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (HttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtils(context);
                }
            }
        }
        return mInstance;
    }

    public HttpUtils(Context context) {
        this.context = context;
        if (configuration == null) {
            configuration = new NetWorkConfiguration(context);
        }
        netInterceptor = new NetInterceptor(configuration);

        if (configuration.getIsCache()) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(netInterceptor)
                    .addNetworkInterceptor(netInterceptor)
                    .addInterceptor(new LogInterceptor())
                    .cache(configuration.getDiskCache())
                    .connectTimeout(configuration.getConnectTimeOut(), TimeUnit.SECONDS)
                    .connectionPool(configuration.getConnectionPool())
                    .retryOnConnectionFailure(true)
                    .build();
        } else {
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new LogInterceptor())
                    .connectTimeout(configuration.getConnectTimeOut(), TimeUnit.SECONDS)
                    .connectionPool(configuration.getConnectionPool())
                    .retryOnConnectionFailure(true)
                    .build();
        }

        if (configuration.getCertificates() != null) {
            mOkHttpClient = getOkHttpClient().newBuilder()
                    .sslSocketFactory(HttpsUtils.getSSLSocketFactory(configuration.getCertificates(), null, null))
                    .build();
        }

    }

    /**
     * 获得OkHttpClient实例
     *
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 设置网络配置参数
     *
     * @param configuration
     */
    public static void setConFiguration(NetWorkConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("ImageLoader configuration can not be initialized with null");
        }

        LogUtil.i("ConFiguration" + configuration.toString());

        if (HttpUtils.configuration == null) {
            LogUtil.d("Initialize NetWorkConfiguration with configuration");
            HttpUtils.configuration = configuration;
        } else {
            LogUtil.e("Try to initialize NetWorkConfiguration which had already been initialized before. To re-init NetWorkConfiguration with new configuration ");
        }

    }

    public RetrofitClient getRetofitClinet() {
        LogUtil.i("configuration:" + configuration.toString());
        return new RetrofitClient(configuration.getBaseUrl(), mOkHttpClient);
    }

    /**
     * 设置HTTPS客户端带证书访问
     *
     * @param certificates 本地证书
     */
    public HttpUtils setCertificates(InputStream... certificates) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .sslSocketFactory(HttpsUtils.getSSLSocketFactory(certificates, null, null))
                .build();
        return this;
    }

    /**
     * 设置是否打印网络日志
     *
     * @param falg
     */
    public HttpUtils setDBugLog(boolean falg) {
        if (falg) {
            mOkHttpClient = getOkHttpClient().newBuilder()
                    .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
        }
        return this;
    }

    /**
     * 设置Coolie
     *
     * @return
     */
    public HttpUtils addCookie() {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .cookieJar(new NovateCookieManager(context))
                .build();
        return this;
    }

}
