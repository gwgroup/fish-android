package com.ypcxpt.fish.main.view.activity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.xw.repo.XEditText;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.login.model.LoginInfo;
import com.ypcxpt.fish.main.util.WifiConnector;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Main.NET_SETTING)
public class NetSettingActivity extends BaseActivity {

    @BindView(R.id.rl_01)
    RelativeLayout rl_01;
    @BindView(R.id.rl_02)
    RelativeLayout rl_02;
    @BindView(R.id.rl_03)
    RelativeLayout rl_03;
    @BindView(R.id.rl_04)
    RelativeLayout rl_04;

    @BindView(R.id.et_wifiName)
    XEditText et_wifiName;
    @BindView(R.id.et_wifiPwd)
    XEditText et_wifiPwd;

    @BindView(R.id.pb_wait)
    ProgressBar pb_wait;
    @BindView(R.id.iv_pass)
    ImageView iv_pass;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.tv_backToHome)
    TextView tv_backToHome;

    WifiManager wifiManager;
    WifiConnector wac;
    private String SSID = "smart";

    @Override
    protected int layoutResID() {
        return R.layout.activity_net_setting;
    }

    @Override
    protected void initData() {
        rl_01.setVisibility(View.VISIBLE);
        rl_02.setVisibility(View.GONE);
        rl_03.setVisibility(View.GONE);
        rl_04.setVisibility(View.GONE);
    }

    @Override
    protected void initViews() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wac = new WifiConnector(wifiManager);

        wac.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 操作界面
                Logger.e("msg", msg.obj + "");
                ThreadHelper.postDelayed(() -> doNetSet(), 2000);
                super.handleMessage(msg);
            }
        };
    }

    private void doNetSet() {
        RequestParams params = new RequestParams("http://192.168.12.1:9999/onekey-net-config?ssid="
                + et_wifiName.getText().toString() + "&psk=" + et_wifiPwd.getText().toString());
//        params.addParameter("ssid", et_wifiName.getText().toString());
//        params.addParameter("psk", et_wifiPwd.getText().toString());
        params.setAsJsonContent(true);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e("配置网络", result);
                Gson gson = new Gson();
                LoginInfo loginInfo = gson.fromJson(result, LoginInfo.class);
                if (loginInfo.getCode() == 1000) {
                    pb_wait.setVisibility(View.GONE);
                    iv_pass.setVisibility(View.VISIBLE);
                    tv_msg.setText("配网成功");
                    tv_backToHome.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e("配置网络", "onError");
                doNetSet();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logger.e("配置网络", "onCancelled");
            }

            @Override
            public void onFinished() {
                Logger.e("配置网络", "onFinished");
            }
        });
    }

    @OnClick({R.id.tv_set01, R.id.tv_set02back, R.id.tv_set02, R.id.tv_set03back, R.id.tv_set03})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_set01:
            case R.id.tv_set03back:
                rl_01.setVisibility(View.GONE);
                rl_02.setVisibility(View.VISIBLE);
                rl_03.setVisibility(View.GONE);
                rl_04.setVisibility(View.GONE);
                break;
            case R.id.tv_set02back:
                rl_01.setVisibility(View.VISIBLE);
                rl_02.setVisibility(View.GONE);
                rl_03.setVisibility(View.GONE);
                rl_04.setVisibility(View.GONE);
                break;
            case R.id.tv_set02:
//                if (StringUtils.isEmpty(et_wifiName.getText().toString().trim())) {
//                    Toaster.showShort("请输入要使用的Wifi名称");
//                    return;
//                }
                rl_01.setVisibility(View.GONE);
                rl_02.setVisibility(View.GONE);
                rl_03.setVisibility(View.VISIBLE);
                rl_04.setVisibility(View.GONE);
                break;
            case R.id.tv_set03:
                rl_01.setVisibility(View.GONE);
                rl_02.setVisibility(View.GONE);
                rl_03.setVisibility(View.GONE);
                rl_04.setVisibility(View.VISIBLE);

                //连接到smart热点 调用接口
                try {
                    wac.connect(SSID, "", WifiConnector.WifiCipherType.WIFICIPHER_NOPASS);
                } catch (Exception e) {
                    Logger.e("eee", e.getMessage());
                }

                break;
        }
    }

    @OnClick({R.id.rl_back, R.id.tv_backToHome})
    public void onBack() {
        onBackPressed();
    }
}
