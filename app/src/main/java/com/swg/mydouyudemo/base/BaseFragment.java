package com.swg.mydouyudemo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swg.mydouyudemo.model.ContractProxy;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by swg on 2017/11/22.
 */
@SuppressWarnings("unchecked")
public abstract class BaseFragment<M extends BaseModel, P extends BasePresenter> extends RxFragment {

    protected P mPresenter;
    protected Unbinder mUnBinder;
    protected View rootView;
    protected Context mContext;
    /**
     * 标识fragment视图已经初始化完毕
     */
    private boolean isViewPresenter;
    /**
     * 标识已经触发过懒加载数据
     */
    private boolean hasFetchData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            if (getLayoutId() != 0) {
                rootView = inflater.inflate(getLayoutId(), container, false);
            } else {
                rootView = super.onCreateView(inflater, container, savedInstanceState);
            }
        }

        mUnBinder = ButterKnife.bind(this, rootView);
        bindMVP();
        onInitView(savedInstanceState);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewPresenter = true;
        lazyFetchDataPrepared();
        if (mPresenter == null) {
            bindMVP();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onEvent();
    }

    @Override
    public void onStart() {
        if (mPresenter == null) {
            bindMVP();
        }
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (mPresenter != null) {
            ContractProxy.getInstance().unbindView(getViewImpl(), mPresenter);
            ContractProxy.getInstance().unbindModel(getModelClazz(), mPresenter);
//            mPresenter = null;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyFetchDataPrepared();
        }
    }

    /**
     * 进行懒加载
     */
    private void lazyFetchDataPrepared() {
        if (getUserVisibleHint() && !hasFetchData && isViewPresenter) {
            hasFetchData = true;
            lazyFetchData();
        }
    }

    /**
     * 绑定Presenter
     */
    private void bindMVP() {
        if (getPresenterClazz() != null) {
            mPresenter = getPresenterImpl();
            mPresenter.mContext = getActivity();
            bindVM();
        }
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
     * 获取Presenter实例
     *
     * @param <T>
     * @return
     */
    private <T> T getPresenterImpl() {
        return ContractProxy.getInstance().presenter(getPresenterClazz());
    }

    /**
     * 绑定Model和View
     */
    private void bindVM() {
        if (mPresenter != null && mPresenter.isViewBind() && getModelClazz() != null) {
            ContractProxy.getInstance().bindModel(getModelClazz(), mPresenter);
            ContractProxy.getInstance().bindView(getViewImpl(), mPresenter);
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
     * 获取资源ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 获取View对象
     *
     * @return
     */
    protected abstract BaseView getViewImpl();

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
     * 懒加载的方式获取数据，仅在满足fragment可见和视图已经准备好的时候调用一次
     */
    protected abstract void lazyFetchData();

}
