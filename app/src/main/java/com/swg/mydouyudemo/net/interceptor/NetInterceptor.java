package com.swg.mydouyudemo.net.interceptor;

import com.swg.mydouyudemo.net.config.NetWorkConfiguration;
import com.swg.mydouyudemo.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络连接器
 * Created by swg on 2017/11/23.
 */

public class NetInterceptor implements Interceptor {

    private NetWorkConfiguration configuration;

    public NetInterceptor(NetWorkConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        /**
         *  断网后是否加载本地缓存数据
         *
         */
        if (!NetworkUtil.isNetworkAvailable(configuration.context)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        } else if (configuration.getIsMemoryCache()) {
            // 加载内存缓存数据
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        /**
         *  加载网络数据
         */
        else {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
        }
        Response response = chain.proceed(request);
        // 有网进行内存缓存数据
        if (NetworkUtil.isNetworkAvailable(configuration.context) && configuration.getIsMemoryCache()) {
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + configuration.getmemoryCacheTime())
                    .removeHeader("Pragma")
                    .build();
        } else {
            // 进行本地缓存数据
            if (configuration.getIsDiskCache()) {
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + configuration.getDiskCacheTime())
                        .removeHeader("Pragma")
                        .build();
            }
        }
        return response;
    }
}
