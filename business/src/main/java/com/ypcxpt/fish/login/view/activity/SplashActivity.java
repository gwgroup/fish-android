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
        if (!isTaskRoot()){
            finish();
            return;
        }

        EventBus.getDefault().register(this);//EventBus注册
        mPresenter = new SplashPresenter();

        doOnCreate();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);//EventBus解注册（防止内存泄漏）
        super.onDestroy();
    }

    private void doOnCreate() {
//        AppData.setToken("b5fff1337ab14429b7880d5678c94651");
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
//            Router.build(Path.Login.ADVERTISING)
//                    .withParcelable("userProfile", event.userProfile)
//                    .withFinish().navigation(this);

            Router.build(Path.Main.MAIN)
                    .withParcelable("userProfile", event.userProfile)
                    .withFinish()
                    .navigation(this, android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            jumpToLoginPage();
        }
    }

}
