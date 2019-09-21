package com.ypcxpt.fish.login.view.activity;

import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.RegexUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ypcxpt.fish.App;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.ui.widget.CountDownButton;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.login.Constants;
import com.ypcxpt.fish.login.contract.LoginContract;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.login.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Login.LOGIN)
public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.count_down_btn) CountDownButton countDownBtn;
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.et_verify_code) EditText etVerifyCode;

    private LoginContract.Presenter mPresenter;

    /**
     * 微信登录相关
     */
    private IWXAPI api;

    @Override
    protected int layoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected ImmersionBar initImmersionBar() {
        return ImmersionBar.with(this).statusBarColor(android.R.color.white).fitsSystemWindows(true);
    }

    @Override
    protected void initData() {
        mPresenter = new LoginPresenter();
        addPresenter(mPresenter);
    }

    @Override
    protected void initViews() {
        App.getInstance().addActivity(this);
        countDownBtn.init(true);

        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID_WX,true);
        //将应用的appid注册到微信
        api.registerApp(Constants.APP_ID_WX);
    }

    @OnClick({R.id.count_down_btn, R.id.tv_login, R.id.iv_wechat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.count_down_btn:
                doSendCode();
                break;
            case R.id.tv_login:
                doLogin();
                break;
            case R.id.iv_wechat:
                doWechatLogin();
                break;
                
        }
    }

    private void doWechatLogin() {
        if (!api.isWXAppInstalled()) {
            Toaster.showShort("您手机尚未安装微信，请安装后再登录");
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        //应用授权作用域，如获取用户个人信息则填写snsapi_userinfo
        req.scope = "snsapi_userinfo";
        //官方说明：用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
        req.state = "wechat_sdk_reead";
        api.sendReq(req);
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

    private void doLogin() {
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
        mPresenter.login(phoneNo, verifyCode);
    }

    @Override
    public void afterLoginSuccess() {
//        Router.build(Path.Main.MAIN).withFinish().navigation(this);
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
