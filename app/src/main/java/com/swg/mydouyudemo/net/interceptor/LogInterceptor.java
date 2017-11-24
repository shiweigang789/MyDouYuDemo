package com.swg.mydouyudemo.net.interceptor;

import com.swg.mydouyudemo.utils.LogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 打印网络Log拦截器
 * Created by swg on 2017/11/22.
 */

public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        MediaType mediaType = request.body().contentType();
        String content = response.body().string();
        long t1 = System.nanoTime();
        LogUtil.i(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
        long t2 = System.nanoTime();
        LogUtil.i(String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        LogUtil.i("NetWork", "response body:" + content);
        LogUtil.json(content);
        if (response.body() != null) {
            ResponseBody body = ResponseBody.create(mediaType, content);
            return response.newBuilder().body(body).build();
        }
        return response;
    }

}
