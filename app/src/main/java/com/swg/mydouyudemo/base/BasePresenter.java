package com.swg.mydouyudemo.base;

import android.content.Context;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by swg on 2017/11/21.
 */

public class BasePresenter<V extends BaseView, M extends BaseModel> implements Presenter<V, M> {

    protected Context mContext;

    protected V mView;

    protected M mModel;

    protected CompositeSubscription mCompositeSubscription;

    protected void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    // 获取绑定的实例
    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    // 获取绑定Model层实例
    @Override
    public void attachModel(M model) {
        this.mModel = model;
    }

    // 注销View实例
    @Override
    public void detachView() {
        this.mView = null;
    }

    // 注销Model实例
    @Override
    public void detachModel() {
        this.mModel = null;
    }

    public M getModel() {
        return mModel;
    }

    public V getView() {
        return mView;
    }

    public boolean isViewBind() {
        return mView != null;
    }

}
