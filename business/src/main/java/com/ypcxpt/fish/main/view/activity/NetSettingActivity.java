package com.ypcxpt.fish.main.view.activity;

import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.RegexUtils;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.ui.widget.CountDownButton;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Main.NET_SETTING)
public class NetSettingActivity extends BaseActivity {

    @BindView(R.id.count_down_btn) CountDownButton countDownBtn;
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.et_verify_code) EditText etVerifyCode;

    @Override
    protected int layoutResID() {
        return R.layout.activity_net_setting;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initViews() {
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
    }
}
