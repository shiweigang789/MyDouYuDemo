package com.swg.mydouyudemo.net.request;

import com.swg.mydouyudemo.net.factory.ExGsonConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by swg on 2017/11/23.
 */

public class RetrofitClient {

    private static OkHttpClient mOkHttpClient;
    private static Retrofit retrofit;
    private static String baseUrl;

    public RetrofitClient(String baseUrl, OkHttpClient mOkHttpClient) {
        this.baseUrl = baseUrl;
        this.mOkHttpClient = mOkHttpClient;
    }

    /**
     *  修改BaseUrl地址
     * @param baseUrl
     */
    public RetrofitClient setBaseUrl(String baseUrl)
    {
        this.baseUrl=baseUrl;
        return this;
    }

    /**
     *  获得对应的ApiServcie对象
     * @param service
     * @param <T>
     * @return
     */
    public  <T> T builder(Class<T> service)
    {
        if(baseUrl==null)
        {
            throw new RuntimeException("baseUrl is null!");
        }
        if (service == null) {
            throw new RuntimeException("api Service is null!");
        }
        retrofit=new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(ExGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }

}
