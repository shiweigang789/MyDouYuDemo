package com.swg.mydouyudemo.net.transformer;

import com.swg.mydouyudemo.net.exception.ExceptionHandle;
import com.swg.mydouyudemo.net.exception.ServerException;
import com.swg.mydouyudemo.net.response.HttpResponse;
import com.swg.mydouyudemo.utils.LogUtil;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by swg on 2017/11/23.
 */

public class ErrorTransformer<T> implements Observable.Transformer<HttpResponse<T>, T> {
    @Override
    public Observable<T> call(Observable<HttpResponse<T>> httpResponseObservable) {
        // 对服务器端给出的Json数据进行校验
        return httpResponseObservable.map(new Func1<HttpResponse<T>, T>() {
            @Override
            public T call(HttpResponse<T> tHttpResponse) {
                if (tHttpResponse.getErrror() != 0) {
                    LogUtil.e("HttpResponse:", tHttpResponse.toString());
                    //如果服务器端有错误信息返回，那么抛出异常，让下面的方法去捕获异常做统一处理
                    throw new ServerException(String.valueOf(tHttpResponse.getData()), tHttpResponse.getErrror());
                }
                //服务器请求数据成功，返回里面的数据实体
                return tHttpResponse.getData();
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
            @Override
            public Observable<? extends T> call(Throwable throwable) {
                throwable.printStackTrace();
                return Observable.error(ExceptionHandle.handleException(throwable));
            }
        });
    }

    private static ErrorTransformer instance = null;

    public static <T> ErrorTransformer<T> create() {
        return new ErrorTransformer<>();
    }

    private ErrorTransformer() {
    }

    public static <T> ErrorTransformer<T> getInstance() {
        if (instance == null) {
            synchronized (ErrorTransformer.class) {
                if (instance == null) {
                    instance = new ErrorTransformer();
                }
            }
        }
        return instance;
    }

}
