package com.ypcxpt.fish.login.view.activity;

import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.RegexUtils;

import com.ypcxpt.fish.App;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.ui.widget.CountDownButton;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.login.contract.BindContract;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.login.presenter.BindPresenter;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Login.BIND)
public class BindActivity extends BaseActivity implements BindContract.View {

    @BindView(R.id.count_down_btn) CountDownButton countDownBtn;
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.et_verify_code) EditText etVerifyCode;

    private BindContract.Presenter mPresenter;

    @Autowired(name = "openid")
    public String openid;

    @Override
    protected int layoutResID() {
        return R.layout.activity_bind;
    }

    @Override
    protected void initData() {
        mPresenter = new BindPresenter();
        addPresenter(mPresenter);
    }

    @Override
    protected void initViews() {
        App.getInstance().addActivity(this);
        countDownBtn.init(true);
    }

    @OnClick({R.id.count_down_btn, R.id.tv_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.count_down_btn:
                doSendCode();
                break;
            case R.id.tv_bind:
                doLoginBind();
                break;
        }
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        onBackPressed();
    }

    private void doSendCode() {
        String phoneNo = ViewHelper.getText(etPhone);
        if (!RegexUtils.isMobileExact(phoneNo)) {
            Toaster.showLong("请输入合法手机号");
            return;
        }
        countDownBtn.start();
        mPresenter.getVerifyCode(phoneNo);
    }

    private void doLoginBind() {
        String phoneNo = ViewHelper.getText(etPhone);
        String verifyCode = ViewHelper.getText(etVerifyCode);
        if (!RegexUtils.isMobileExact(phoneNo)) {
            Toaster.showLong("请输入合法手机号");
            return;
        }
        if (verifyCode.length() < 6) {
            Toaster.showLong("请输入6位验证码");
            return;
        }
        mPresenter.bind(phoneNo, verifyCode, openid);
    }

    @Override
    public void afterLoginSuccess() {
        mPresenter.getUser();//登录成功之后获取user信息
    }

    @Override
    public void afterLoginSuccessGetUser(UserProfile userProfile) {
        Router.build(Path.Main.MAIN)
                .withParcelable("userProfile", userProfile)
                .withFinish()
                .navigation(this);

//        JPushAliasUtil.setAliasJPush(userProfile.id.replace("-", ""));
    }
}
