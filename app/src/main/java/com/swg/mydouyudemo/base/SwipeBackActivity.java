package com.swg.mydouyudemo.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import com.swg.mydouyudemo.model.ContractProxy;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * Created by swg on 2017/11/22.
 */
@SuppressWarnings("unchecked")
public abstract class SwipeBackActivity<M extends BaseModel, P extends BasePresenter> extends RxAppCompatActivity {

    public SwipeBackLayout mSwipeBackLayout;
    protected P mPresenter;
    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            processSaveInstanceState(savedInstanceState);
        }
        onActivityCreate();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            mUnbinder = ButterKnife.bind(this);
            bindMVP();
            onInitView(savedInstanceState);
            onEvent();
        }

    }

    @Override
    protected void onStart() {
        if (mPresenter == null) {
            bindMVP();
        }
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            ContractProxy.getInstance().unbindView(getView(), mPresenter);
            ContractProxy.getInstance().unbindModel(getModelClazz(), mPresenter);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackLayout.attachToActivity(this);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        View view = super.findViewById(id);
        if (view == null && mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return (T) view;
    }

    protected void processSaveInstanceState(Bundle saveInstanceState) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            boolean showFlag = false;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            for (int i = fragments.size() - 1; i >= 0; i--) {
                Fragment fragment = fragments.get(i);
                if (fragment != null) {
                    if (!showFlag) {
                        ft.show(fragment);
                        showFlag = true;
                    } else {
                        ft.hide(fragment);
                    }
                }
            }
            ft.commit();
        }
    }

    protected void onActivityCreate() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = new SwipeBackLayout(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSwipeBackLayout.setLayoutParams(params);
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
     * 绑定View和Model
     */
    protected void bindVM() {
        if (mPresenter != null && !mPresenter.isViewBind() && getModelClazz() != null) {
            ContractProxy.getInstance().bindModel(getModelClazz(), mPresenter);
            ContractProxy.getInstance().bindView(getView(), mPresenter);
//            mPresenter = null;
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
    protected abstract BaseView getView();

    /**
     * 初始化界面
     *
     * @param bundle
     */
    protected abstract void onInitView(Bundle bundle);

    /**
     * 初始化数据
     */
    protected abstract void onEvent();

}
