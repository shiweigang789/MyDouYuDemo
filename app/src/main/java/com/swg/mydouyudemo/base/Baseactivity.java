package com.swg.mydouyudemo.base;

import android.os.Bundle;

import com.swg.mydouyudemo.model.ContractProxy;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by swg on 2017/11/21.
 */
@SuppressWarnings("unchecked")
public abstract class Baseactivity<M extends BaseModel, P extends BasePresenter> extends RxAppCompatActivity {

    // 定义Presenter
    protected P mPresenter;

    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        bindMVP();
        onInitView(savedInstanceState);
        onEvent();
    }

    @Override
    protected void onStart() {
        if (mPresenter == null) {
            bindMVP();
        }
        super.onStart();
    }

    /**
     * 界面销毁时解除绑定
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            ContractProxy.getInstance().unbindView(getView(), mPresenter);
            ContractProxy.getInstance().unbindModel(getModelClazz(), mPresenter);
            mPresenter = null;
        }
    }

    /**
     * 绑定Presenter
     */
    private void bindMVP() {
        if (getPresenterClazz() != null) {
            mPresenter = getPresenterImpl();
            mPresenter.mContext = this;
            bindVM();
        }
    }

    /**
     * 获取Presenter实例
     *
     * @param <T>
     * @return
     */
    private <T> T getPresenterImpl() {
        return ContractProxy.getInstance().presenter(getPresenterClazz());
    }

    /**
     * 绑定View和Model
     */
    private void bindVM() {
        if (mPresenter != null && !mPresenter.isViewBind() && getModelClazz() != null) {
            ContractProxy.getInstance().bindModel(getModelClazz(), mPresenter);
            ContractProxy.getInstance().bindView(getView(), mPresenter);
        }
    }

    /**
     * 获取Model对象
     *
     * @return
     */
    protected Class getModelClazz() {
        return (Class<M>) ContractProxy.getModelClazz(getClass(), 0);
    }

    /**
     * 获取Presenter对象
     *
     * @return
     */
    protected Class getPresenterClazz() {
        return (Class<P>) ContractProxy.getPresenterClazz(getClass(), 1);
    }

    /**
     * 获取布局资源文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     *
     * @param bundle
     */
    protected abstract void onInitView(Bundle bundle);

    /**
     * 初始化事件
     */
    protected abstract void onEvent();

    /**
     * 获取抽取View对象
     *
     * @return
     */
    protected abstract BaseView getView();

}
