package com.ypcxpt.fish.library.view.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import com.ypcxpt.fish.library.app.GlobalEvent;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.presenter.PresenterDelegate;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.library.view.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

public abstract class BaseFragment extends RxFragment implements IView {
    /**
     * Presenter代理.
     */
    private PresenterDelegate mPresenterDelegate;

    /**
     * 是否已经初始化过.
     */
    private boolean hasInitialized;

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            onRecycled(savedInstanceState);
        }
    }

    /**
     * 系统回收后的处理. 子类可以覆写，进行特殊处理（如恢复先前界面的显示等）
     */
    protected void onRecycled(Bundle savedInstanceState) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResID(), container, false);
        if (!hasInitialized) {
            routineTasks(view);
            hasInitialized = true;
        }
        return view;
    }

    /**
     * 常规初始化内容
     */
    private void routineTasks(View view) {
        prepareTools();
        prepareDataAndViews(view);
        preparePresenter();
        onFinishInit();
    }

    private void prepareTools() {
        EventBus.getDefault().register(this);
    }

    private void prepareDataAndViews(View view) {
        ButterKnife.bind(this, view);

        /* Presenter代理初始化 */
        mPresenterDelegate = new PresenterDelegate();

        /* 抛给子类自由定制初始化 */
        initData();
        initViews();
    }

    private void preparePresenter() {
        mPresenterDelegate.init(this);
        mPresenterDelegate.openDataFetching();
    }

    /**
     * 抛给子类，所有初始化完毕后的回调
     */
    protected void onFinishInit() {
    }


    protected void addPresenter(IPresenter presenter) {
        mPresenterDelegate.add(presenter);
    }

    @LayoutRes
    protected abstract int layoutResID();

    protected void initData() {
    }

    protected abstract void initViews();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onStart();
        }
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onResume();
        }
    }

    @Override
    @CallSuper
    public void onPause() {
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onPause();
        }
        super.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onStop();
        }
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onDestroy();
        }
        unregisterTools();
        super.onDestroy();
    }

    private void unregisterTools() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showLoading() {
        ((BaseActivity) getActivity()).showLoading();
    }

    @Override
    public void dismissLoading() {
        ((BaseActivity) getActivity()).dismissLoading();
    }

    @Override
    public <T> LifecycleTransformer<T> bindToRxLifecycle() {
        return bindToLifecycle();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGlobalEvent(GlobalEvent event) {
    }
}
