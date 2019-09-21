package com.ypcxpt.fish.library.view.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ObjectUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.app.GlobalEvent;
import com.ypcxpt.fish.library.listener.RequestPermissionListener;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.presenter.PresenterDelegate;
import com.ypcxpt.fish.library.router.FinishStyle;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.ui.widget.PageLoadingView;
import com.ypcxpt.fish.library.util.InputMethodUtils;
import com.ypcxpt.fish.library.util.PermissionHelper;
import com.ypcxpt.fish.library.view.IView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
import static com.ypcxpt.fish.library.router.ActivityTransitionAnim.getFinishEnterAnim;
import static com.ypcxpt.fish.library.router.ActivityTransitionAnim.getFinishExitAnim;

public abstract class BaseActivity extends RxAppCompatActivity implements IView, RequestPermissionListener {
    /**
     * Presenter代理
     */
    private PresenterDelegate mPresenterDelegate;

    /**
     * 全局加载页
     */
    private PageLoadingView mLoadingView;

    /**
     * 沉浸式状态栏
     */
    private ImmersionBar mImmersionBar;

    /**
     * 结束动画样式
     */
    private FinishStyle mFinishStyle = FinishStyle.COMMON;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performOnCreate(savedInstanceState);
    }

    /**
     * 初始化
     */
    private void performOnCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            requestPermissions(new String[]{});
            routineTasks();
        } else {
            onRecycled(savedInstanceState);
        }
    }

    /**
     * 常规初始化内容
     */
    private void routineTasks() {
        prepareTools();
        prepareDataAndViews();
        preparePresenter();
        onFinishInit();
    }

    /**
     * 系统回收后的处理. 子类可以覆写，进行特殊处理（如恢复先前界面的显示等）
     */
    protected void onRecycled(Bundle savedInstanceState) {
        finish();
    }

    /**
     * 如果子类要申请权限，需要覆写该方法.
     *
     * @param permissions 待申请权限.
     * @return 是否需要申请权限，默认否.
     */
    protected boolean requestPermissions(String... permissions) {
        if (ObjectUtils.isEmpty(permissions)) {
            return false;
        } else {
            new PermissionHelper().requestPermissions(this, this, permissions);
            return true;
        }
    }

    private void prepareTools() {
        Router.inject(this);
        EventBus.getDefault().register(this);
    }

    private void prepareDataAndViews() {
        setContentView(layoutResID());
        ButterKnife.bind(this);

        /* Presenter代理初始化 */
        mPresenterDelegate = new PresenterDelegate();
        /* 全局加载样式 */
        mLoadingView = new PageLoadingView(this);

        /* 默认沉浸式的样式 */
        mImmersionBar = initImmersionBar();
        mImmersionBar.init();

        /* 默认输入法样式 */
        adjustPan();

        /* 抛给子类自由定制初始化 */
        initData();
        initViews();
    }

    /**
     * 默认沉浸式的样式，可以让子类覆写.
     */
    protected ImmersionBar initImmersionBar() {
        return ImmersionBar.with(this).statusBarColor(R.color.main_color_new).fitsSystemWindows(true);
    }

    /**
     * 默认输入法属性.
     */
    public void adjustPan() {
        getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
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

    @LayoutRes
    protected abstract int layoutResID();

    protected void initData() {
    }

    protected abstract void initViews();

    protected void addPresenter(IPresenter presenter) {
        mPresenterDelegate.add(presenter);
    }

    @Override
    @CallSuper
    public void finish() {
        super.finish();
        overridePendingTransition(getFinishEnterAnim(mFinishStyle), getFinishExitAnim(mFinishStyle));
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onStart();
        }
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onResume();
        }
    }

    @Override
    @CallSuper
    protected void onPause() {
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onPause();
        }
        hideInputMethod();
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onStop();
        }
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onDestroy();
        }
        unregisterTools();
        super.onDestroy();
    }

    private void unregisterTools() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * @param finishStyle Animation style when {@link #finish()}.
     */
    protected void setFinishAnimStyle(FinishStyle finishStyle) {
        mFinishStyle = finishStyle;
    }

    /**
     * 关闭输入法.
     */
    public void hideInputMethod() {
        View focusView = getCurrentFocus();
        if (focusView == null) {
            focusView = new View(this);
        }
        InputMethodUtils.getIMM(this).hideSoftInputFromWindow(focusView.getWindowToken(), 0);
    }

    @Override
    public void showLoading() {
        if (mLoadingView != null) {
            mLoadingView.show();
        }
    }

    @Override
    public void dismissLoading() {
        if (mLoadingView != null && mLoadingView.isShowing()) {
            mLoadingView.dismiss();
        }
    }

    @Override
    public <E> LifecycleTransformer<E> bindToRxLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void onAcceptAllPermissions() {
        /* 默认不拦截，所以交给子类处理 */
    }

    @Override
    public void onDenySomePermissions(List<String> deniedPermissions) {
        /* 交给子类处理 */
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGlobalEvent(GlobalEvent event) {
    }
}
