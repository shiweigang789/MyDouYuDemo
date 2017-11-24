package com.swg.mydouyudemo.net.callback;

/**
 * Created by swg on 2017/11/22.
 */

public abstract class RxSubscriber<T> extends ErrorSubscriber<T> {

    /**
     * 开始请求网络
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 获取网络数据
     *
     * @param t
     */
    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    /**
     * 网络请求完成
     */
    @Override
    public void onCompleted() {

    }

    /**
     * 成功回调
     *
     * @param t
     */
    protected abstract void onSuccess(T t);

}
