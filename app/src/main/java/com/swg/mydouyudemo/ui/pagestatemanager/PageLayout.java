package com.swg.mydouyudemo.ui.pagestatemanager;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by swg on 2017/11/24.
 */

public class PageLayout extends FrameLayout {

    private View mLoadingView;
    private View mRetryView;
    private View mContentView;
    private View mEmptyView;
    private LayoutInflater mInflater;

    private static final String TAG = PageLayout.class.getSimpleName();

    public PageLayout(@NonNull Context context) {
        this(context, null);
    }

    public PageLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PageLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 判断是否为主线程
     *
     * @return
     */
    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 显示加载页面
     */
    public void showLoading() {
        if (isMainThread()) {
            showView(mLoadingView);
        } else {
            post(() -> showView(mLoadingView));
        }
    }

    /**
     * 显示重新加载页面
     */
    public void showRetry() {
        if (isMainThread()) {
            showView(mRetryView);
        } else {
            post(() -> showView(mRetryView));
        }
    }

    /**
     * 显示内容页面
     */
    public void showContent() {
        if (isMainThread()) {
            showView(mContentView);
        } else {
            post(() -> showView(mContentView));
        }
    }

    /**
     * 显示空页面
     */
    public void showEmpty() {
        if (isMainThread()) {
            showView(mEmptyView);
        } else {
            post(() -> showView(mEmptyView));
        }
    }

    private void showView(View view) {
        if (view == null) {
            return;
        }

        if (view == mLoadingView) {
            mLoadingView.setVisibility(VISIBLE);
            if (mRetryView != null)
                mRetryView.setVisibility(GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
        } else if (view == mRetryView) {
            mRetryView.setVisibility(View.VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
        } else if (view == mContentView) {
            mContentView.setVisibility(View.VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mRetryView != null)
                mRetryView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
        } else if (view == mEmptyView) {
            mEmptyView.setVisibility(View.VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mRetryView != null)
                mRetryView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
        }

    }

    public View setContentView(int layoutId) {
        return setContentView(mInflater.inflate(layoutId, this, false));
    }

    public View setLoadingView(int layoutId) {
        return setLoadingView(mInflater.inflate(layoutId, this, false));
    }

    public View setEmptyView(int layoutId) {
        return setEmptyView(mInflater.inflate(layoutId, this, false));
    }

    public View setRetryView(int layoutId) {
        return setRetryView(mInflater.inflate(layoutId, this, false));
    }

    public View setContentView(View view) {
        View contentView = mContentView;
        if (contentView != null) {
            throw new IllegalArgumentException("you have already set a retry view and would be instead of this new one.");
        }
        removeView(contentView);
        addView(view);
        mContentView = view;
        return mContentView;
    }

    public View setLoadingView(View view) {
        View loadingView = mLoadingView;
        if (loadingView != null) {
            throw new IllegalArgumentException("you have already set a loading view and would be instead of this new one.");
        }
        removeView(loadingView);
        addView(view);
        mLoadingView = view;
        return mLoadingView;
    }

    public View setEmptyView(View view) {
        View emptyView = mEmptyView;
        if (emptyView != null) {
            throw new IllegalArgumentException("you have already set a empty view and would be instead of this new one.");
        }
        removeView(emptyView);
        addView(view);
        mEmptyView = view;
        return mEmptyView;
    }

    public View setRetryView(View view) {
        View retryView = mRetryView;
        if (retryView != null) {
            throw new IllegalArgumentException("you have already set a retry view and would be instead of this new one.");
        }
        removeView(retryView);
        addView(view);
        mRetryView = view;
        return mRetryView;
    }

    public View getRetryView() {
        return mRetryView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getContentView() {
        return mContentView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

}
