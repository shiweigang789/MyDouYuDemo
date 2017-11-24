package com.swg.mydouyudemo.net.transformer;

import com.swg.mydouyudemo.net.response.HttpResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by swg on 2017/11/23.
 */

public class DefaultTransformer<T> implements Observable.Transformer<HttpResponse<T>, T> {
    @Override
    public Observable<T> call(Observable<HttpResponse<T>> httpResponseObservable) {
        return httpResponseObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .compose(ErrorTransformer.<T>getInstance())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
