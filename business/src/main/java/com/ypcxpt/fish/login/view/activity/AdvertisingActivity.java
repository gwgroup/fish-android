package com.ypcxpt.fish.login.view.activity;

import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.gyf.barlibrary.ImmersionBar;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.login.contract.AdvertisingContract;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.login.presenter.AdvertisingPresenter;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Login.ADVERTISING)
public class AdvertisingActivity extends BaseActivity implements AdvertisingContract.View {
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;

    @Autowired
    public UserProfile userProfile;

    private AdvertisingContract.Presenter mPresenter;

    @Override
    protected int layoutResID() {
        return R.layout.activity_advertising;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected ImmersionBar initImmersionBar() {
        return ImmersionBar.with(this).transparentStatusBar();
    }

    @Override
    protected void initViews() {
        mPresenter = new AdvertisingPresenter();
        addPresenter(mPresenter);
    }

    @OnClick({R.id.tv_count_down})
    public void jumpToMain() {
        mPresenter.cancelCountDown();
        Router.build(Path.Main.MAIN)
                .withParcelable("userProfile", userProfile)
                .withFinish()
                .navigation(this);
    }

    @OnClick({R.id.rl_content})
    public void jumpToDetail() {
        mPresenter.cancelCountDown();
        Router.build(Path.Login.ADVERTISING_DETAIL)
                .withParcelable("userProfile", userProfile)
                .withFinish()
                .navigation(this);
    }

    @Override
    public void onCountDownTick(int untilFinishedSeconds) {
        if (untilFinishedSeconds == 0) {
            tvCountDown.setText("即将跳过");
        } else {
            tvCountDown.setText("跳过  |  " + String.valueOf(untilFinishedSeconds));
        }
    }

    @Override
    public void onCountDownFinish() {
        jumpToMain();
    }
}
