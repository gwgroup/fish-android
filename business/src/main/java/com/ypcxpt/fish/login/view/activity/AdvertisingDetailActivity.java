package com.ypcxpt.fish.login.view.activity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;

import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.router.FinishStyle;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.router.StartStyle;
import com.ypcxpt.fish.library.view.activity.BaseWebActivity;
import com.ypcxpt.fish.login.model.UserProfile;

@Route(path = Path.Login.ADVERTISING_DETAIL)
public class AdvertisingDetailActivity extends BaseWebActivity {

    @Autowired
    public UserProfile userProfile;

    @Override
    protected void initViews() {
        super.initViews();
        setFinishAnimStyle(FinishStyle.HOME);
        getTitleBar().setLeftClick(view -> jumpToMain());
    }

    @Override
    public String getUrl() {
        return "https://shop.m.jd.com/?shopId=615216";
    }

    public void jumpToMain() {
        Router.build(Path.Main.MAIN)
                .startStyle(StartStyle.REVERSE)
                .withParcelable("userProfile", userProfile)
                .withFinish().navigation(this);
    }

    @Override
    public void onBackPressed() {
        if (isWebPageBackEnabled()) {
            super.onBackPressed();
        } else {
            jumpToMain();
        }
    }

}
