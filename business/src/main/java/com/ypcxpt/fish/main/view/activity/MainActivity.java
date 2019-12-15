package com.ypcxpt.fish.main.view.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.gyf.barlibrary.ImmersionBar;
import com.ypcxpt.fish.App;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.core.ble.BLEHelper;
import com.ypcxpt.fish.jpush.ExampleUtil;
import com.ypcxpt.fish.jpush.JPushAliasUtil;
import com.ypcxpt.fish.jpush.LocalBroadcastManager;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.library.view.fragment.BaseFragment;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.event.OnBluetoothPreparedEvent;
import com.ypcxpt.fish.main.event.OnGetScenesEvent;
import com.ypcxpt.fish.main.event.OnMainPagePermissionResultEvent;
import com.ypcxpt.fish.main.event.OnProfileUpdatedEvent;
import com.ypcxpt.fish.main.view.fragment.EarlyWarningFragment;
import com.ypcxpt.fish.main.view.fragment.MyDeviceFragment;
import com.ypcxpt.fish.main.view.fragment.TimingPlanFragment;
import com.ypcxpt.fish.main.view.fragment.UserProfileFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Main.MAIN)
public class MainActivity extends BaseActivity {

    @BindView(R.id.header_line1)
    View line1;
    @BindView(R.id.header_line2)
    View line2;

    @BindView(R.id.iv_bottom_fish)
    ImageView iv_bottom_fish;
    @BindView(R.id.tv_bottom_fish)
    TextView tv_bottom_fish;
    @BindView(R.id.iv_bottom_plan)
    ImageView iv_bottom_plan;
    @BindView(R.id.tv_bottom_plan)
    TextView tv_bottom_plan;
    @BindView(R.id.iv_bottom_warning)
    ImageView iv_bottom_warning;
    @BindView(R.id.tv_bottom_warning)
    TextView tv_bottom_warning;
    @BindView(R.id.iv_bottom_personal)
    ImageView iv_bottom_personal;
    @BindView(R.id.tv_bottom_personal)
    TextView tv_bottom_personal;

    @Autowired
    public UserProfile userProfile;

    private List<BaseFragment> mFragments;

    private BaseFragment mCurrFragment;

    public static final int FISH_POND = 0;
    public static final int MY_PLAN = 1;
    public static final int EALY_WARNING = 2;
    public static final int USER_PROFILE = 3;

    public static boolean isForeground = false;

    @Override
    protected int layoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean requestPermissions(String... permissions) {
        return super.requestPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected ImmersionBar initImmersionBar() {
        return ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true);
    }

    @Override
    protected void initData() {
        if (userProfile != null && userProfile.user.id != null) {
            JPushAliasUtil.setAliasJPush(userProfile.user.id.replace("-", ""));
        }
        registerMessageReceiver();  // used for receive msg

        initFragments();
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnProfileUpdatedEvent(userProfile)), 500);

        //检测新版本
//        VersionCheckUtil.getInstance(this).StartCheckVersion(this, false, false);
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetScenesEvent()), 500);
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new MyDeviceFragment());
        mFragments.add(new TimingPlanFragment());
        mFragments.add(new EarlyWarningFragment());
        mFragments.add(new UserProfileFragment());

        FragmentTransaction transaction = getFragmentTransaction();
        for (BaseFragment fragment : mFragments) {
            transaction.add(R.id.fl_container, fragment);
        }
        transaction.commit();
    }

    @Override
    protected void initViews() {
        App.getInstance().addActivity(this);
        showPage(FISH_POND);
    }

    private void showPage(int index) {
        BaseFragment targetFragment = mFragments.get(index);
        if (mCurrFragment == targetFragment) return;

        FragmentTransaction transaction = getFragmentTransaction();
        for (BaseFragment fragment : mFragments) {
            if (fragment == targetFragment) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }
        transaction.commit();

        if (index == 0) {
//            line1.setVisibility(View.VISIBLE);
//            line2.setVisibility(View.GONE);
            iv_bottom_fish.setImageResource(R.mipmap.icon_bottom_device_ed);
            tv_bottom_fish.setTextColor(getResources().getColor(R.color.common_141515));
            iv_bottom_plan.setImageResource(R.mipmap.icon_bottom_plan_u);
            tv_bottom_plan.setTextColor(getResources().getColor(R.color.common_a1a1a1));
            iv_bottom_warning.setImageResource(R.mipmap.icon_bottom_warning_u);
            tv_bottom_warning.setTextColor(getResources().getColor(R.color.common_a1a1a1));
            iv_bottom_personal.setImageResource(R.mipmap.icon_bottom_personal_u);
            tv_bottom_personal.setTextColor(getResources().getColor(R.color.common_a1a1a1));
        } else if (index == 1) {
            iv_bottom_fish.setImageResource(R.mipmap.icon_bottom_device_u);
            tv_bottom_fish.setTextColor(getResources().getColor(R.color.common_a1a1a1));
            iv_bottom_plan.setImageResource(R.mipmap.icon_bottom_plan_ed);
            tv_bottom_plan.setTextColor(getResources().getColor(R.color.common_141515));
            iv_bottom_warning.setImageResource(R.mipmap.icon_bottom_warning_u);
            tv_bottom_warning.setTextColor(getResources().getColor(R.color.common_a1a1a1));
            iv_bottom_personal.setImageResource(R.mipmap.icon_bottom_personal_u);
            tv_bottom_personal.setTextColor(getResources().getColor(R.color.common_a1a1a1));
        } else if (index == 2) {
            iv_bottom_fish.setImageResource(R.mipmap.icon_bottom_device_u);
            tv_bottom_fish.setTextColor(getResources().getColor(R.color.common_a1a1a1));
            iv_bottom_plan.setImageResource(R.mipmap.icon_bottom_plan_u);
            tv_bottom_plan.setTextColor(getResources().getColor(R.color.common_a1a1a1));
            iv_bottom_warning.setImageResource(R.mipmap.icon_bottom_warning_ed);
            tv_bottom_warning.setTextColor(getResources().getColor(R.color.common_141515));
            iv_bottom_personal.setImageResource(R.mipmap.icon_bottom_personal_u);
            tv_bottom_personal.setTextColor(getResources().getColor(R.color.common_a1a1a1));
        } else {
            iv_bottom_fish.setImageResource(R.mipmap.icon_bottom_device_u);
            tv_bottom_fish.setTextColor(getResources().getColor(R.color.common_a1a1a1));
            iv_bottom_plan.setImageResource(R.mipmap.icon_bottom_plan_u);
            tv_bottom_plan.setTextColor(getResources().getColor(R.color.common_a1a1a1));
            iv_bottom_warning.setImageResource(R.mipmap.icon_bottom_warning_u);
            tv_bottom_warning.setTextColor(getResources().getColor(R.color.common_a1a1a1));
            iv_bottom_personal.setImageResource(R.mipmap.icon_bottom_personal_ed);
            tv_bottom_personal.setTextColor(getResources().getColor(R.color.common_141515));
        }

        mCurrFragment = targetFragment;
    }

    @OnClick({R.id.ll_fish_pond, R.id.ll_plan, R.id.ll_early_warning, R.id.ll_user_profile})
    public void onClick2(View v) {
        switch (v.getId()) {
            case R.id.ll_fish_pond:
                showPage(FISH_POND);
                break;
            case R.id.ll_plan:
                showPage(MY_PLAN);
                break;
            case R.id.ll_early_warning:
                showPage(EALY_WARNING);
                break;
            case R.id.ll_user_profile:
                showPage(USER_PROFILE);
                break;
        }
    }

    private void postPermissionResultEvent() {
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnMainPagePermissionResultEvent()), 750);
    }

    @Override
    public void onAcceptAllPermissions() {
        postPermissionResultEvent();
    }

    @Override
    public void onDenySomePermissions(List<String> deniedPermissions) {
        postPermissionResultEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BLEHelper.TURN_ON_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                /* 蓝牙刚刚开启 */
                EventBus.getDefault().post(new OnBluetoothPreparedEvent());
            } else {
                Toaster.showLong("开启蓝牙失败");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private FragmentTransaction getFragmentTransaction() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.alpha_in, R.anim.alpha_out);
        return transaction;
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                onBackPressed();
                App.getInstance().exit();
//                System.exit(0);
            } else {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetScenesEvent()), 500);
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.ypcxpt.fish.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }

    private void setCostomMsg(String msg) {
        Logger.e("收到自定义通知", msg);
    }

    public static Point point = new Point();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            point.x = (int) ev.getRawX();
            point.y = (int) ev.getRawY();
        }
        return super.dispatchTouchEvent(ev);
    }
}
