package com.ypcxpt.fish.library.presenter;

import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.ypcxpt.fish.library.app.GlobalEvent;
import com.ypcxpt.fish.library.util.ReflectUtils;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.library.view.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class AbsPresenter<T extends IView> implements IPresenter {
    protected T mView;

    @CallSuper
    @Override
    public void init(IView view) {
        initTools();
        takeView(view);
    }

    private void takeView(IView view) {
        mView = (T) view;
    }

    public void dropView() {
        mView = null;
    }

    @Override
    public void acceptData(String filedName, Object value) {
        ReflectUtils.setFiledValue(this, filedName, value);
    }

    @Override
    public BaseActivity getActivity() {
        if (mView instanceof BaseActivity) {
            return (BaseActivity) mView;
        } else if (mView instanceof Fragment) {
            FragmentActivity activity = ((Fragment) mView).getActivity();
            if (activity instanceof BaseActivity) {
                return (BaseActivity) activity;
            }
        }
        return null;
    }

    @Override
    public void openDataFetching() {
    }

    private void initTools() {
        EventBus.getDefault().register(this);
    }

    private void unregisterTools() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    @CallSuper
    public void onDestroy() {
        unregisterTools();
        dropView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGlobalEvent(GlobalEvent event) {
    }
}
