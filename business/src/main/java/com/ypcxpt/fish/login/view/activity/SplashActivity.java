package com.ypcxpt.fish.login.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;

import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.login.event.SplashEvent;
import com.ypcxpt.fish.login.presenter.SplashPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SplashActivity extends Activity {
    private SplashPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //得到当前界面的装饰视图
//        if(Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            //设置让应用主题内容占据状态栏和导航栏
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            //设置状态栏和导航栏颜色为透明
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);
//        }

        if (!isTaskRoot()){
            finish();
            return;
        }

//        setContentView(R.layout.activity_splash);
        EventBus.getDefault().register(this);//EventBus注册
        mPresenter = new SplashPresenter();

//        Handler handler = new Handler();
//        //当计时结束时，跳转至主界面
//        handler.postDelayed(() -> doOnCreate(), 1000);
        doOnCreate();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);//EventBus解注册（防止内存泄漏）
        super.onDestroy();
    }

    private void doOnCreate() {
        if (StringUtils.isTrimEmpty(AppData.token())) {
            /* 第一次启动或者双清 */
            jumpToLoginPage();
        } else {
            /* 本地有token，但不确定是否过期，还是要重新请求下本地 */
            mPresenter.checkToken();
        }
    }

    private void jumpToLoginPage() {
        Router.build(Path.Login.LOGIN).withFinish().navigation(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(SplashEvent event) {
        if (event.isRequestSuccess) {
            Router.build(Path.Login.ADVERTISING)
                    .withParcelable("userProfile", event.userProfile)
                    .withFinish().navigation(this);

//            JPushAliasUtil.setAliasJPush(event.userProfile.id.replace("-", ""));
        } else {
            jumpToLoginPage();
        }
    }

}
