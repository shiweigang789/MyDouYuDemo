package com.swg.mydouyudemo.net.callback;

import com.swg.mydouyudemo.net.exception.ResponseThrowable;
import com.swg.mydouyudemo.utils.LogUtil;

import rx.Subscriber;

/**
 * Created by swg on 2017/11/22.
 */

public abstract class ErrorSubscriber<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        LogUtil.e("错误信息:" + e.getMessage());
        if (e instanceof ResponseThrowable) {
            onError((ResponseThrowable) e);
        } else {
            onError(new ResponseThrowable(e, 1000));
        }
    }

    /**
     * 错误回调
     *
     * @param ex
     */
    protected abstract void onError(ResponseThrowable ex);

}
