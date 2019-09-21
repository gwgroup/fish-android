package com.ypcxpt.fish.device.view.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.gyf.barlibrary.ImmersionBar;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.DisplayUtils;
import com.ypcxpt.fish.core.ble.input.DeviceDataParser;
import com.ypcxpt.fish.device.contract.DeviceDetailContract;
import com.ypcxpt.fish.device.presenter.BaseBLEPresenter;
import com.ypcxpt.fish.device.presenter.DeviceDetailPresenter;
import com.ypcxpt.fish.device.view.fragment.AutoActionFragment;
import com.ypcxpt.fish.device.view.fragment.ManualActionFragment;
import com.ypcxpt.fish.library.util.ResourceUtils;
import com.ypcxpt.fish.library.view.activity.BaseActivity;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;

import static com.ypcxpt.fish.app.util.DisplayUtils.DEFAULT_MAX_PROGRESS;
import static com.ypcxpt.fish.core.app.Constant.HAS_DATA;
import static com.ypcxpt.fish.core.app.Constant.NO_DATA;

//@Route(path = Path.Device.DETAIL)
public class DeviceDetailActivity extends BaseActivity implements DeviceDetailContract.View {
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tab) SmartTabLayout tab;

    /* 时间 */
    @BindView(R.id.tv_timer) TextView tvTimer;
    /* 3D/脚滚/背滚/腿滚 */
    @BindView(R.id.iv_3d) ImageView iv3D;
    @BindView(R.id.iv_bei_gun) ImageView ivBeiGun;
    @BindView(R.id.iv_jiao_gun) ImageView ivJiaoGun;
    @BindView(R.id.iv_tui_gun) ImageView ivTuiGun;
    /* 手法 */
    @BindView(R.id.iv_rou_nie) ImageView ivRouNie;
    @BindView(R.id.iv_qiao_ji) ImageView ivQiaoJi;
    @BindView(R.id.iv_zhi_ya) ImageView ivZhiYa;
    @BindView(R.id.iv_tui_na) ImageView ivTuiNa;
    @BindView(R.id.iv_jia_re) ImageView ivJiaRe;
    @BindView(R.id.iv_zero_gravity) ImageView ivZeroGravity;
    /* 气囊 */
    @BindView(R.id.iv_qi_nang_foot) ImageView ivQiNangFoot;
    @BindView(R.id.iv_qi_nang_leg) ImageView ivQiNangLeg;
    @BindView(R.id.iv_qi_nang_back) ImageView ivQiNangBack;
    @BindView(R.id.iv_qi_nang_ass) ImageView ivQiNangAss;
    @BindView(R.id.iv_qi_nang_hand) ImageView ivQiNangHand;
    @BindView(R.id.iv_qi_nang_arm) ImageView ivQiNangArm;
    /* 进度条 */
    @BindView(R.id.pb1) ProgressBar pb1;
    @BindView(R.id.pb2) ProgressBar pb2;
    @BindView(R.id.pb3) ProgressBar pb3;
    @BindView(R.id.pb4) ProgressBar pb4;

    @Autowired
    public String macAddress;

    @Autowired
    public String characteristicUuid;

    private DeviceDetailContract.Presenter mPresenter;

    @Override
    protected int layoutResID() {
        return R.layout.activity_device_detail;
    }

    @Override
    protected ImmersionBar initImmersionBar() {
        return ImmersionBar.with(this).statusBarColor(R.color.bg_header_color).fitsSystemWindows(true);
    }

    @Override
    protected void initData() {
        mPresenter = new DeviceDetailPresenter();
        addPresenter(mPresenter);
        mPresenter.acceptData("macAddress", macAddress);
        mPresenter.acceptData("characteristicUuid", UUID.fromString(characteristicUuid));
    }

    @Override
    protected void initViews() {
        initVp();
        initPb();
        initTechMode();
    }

    private void initVp() {
        FragmentPagerItems fragmentPagerItems = FragmentPagerItems.with(this)
                .add(R.string.device_mode_manual, ManualActionFragment.class)
                .add(R.string.device_mode_auto, AutoActionFragment.class)
                .create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), fragmentPagerItems);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tab.setViewPager(viewPager);
    }

    private void initPb() {
        pb1.setMax(DEFAULT_MAX_PROGRESS);
        pb2.setMax(DEFAULT_MAX_PROGRESS);
        pb3.setMax(DEFAULT_MAX_PROGRESS);
        pb4.setMax(DEFAULT_MAX_PROGRESS);
    }

    private void initTechMode() {
        ivRouNie.setEnabled(false);
        ivQiaoJi.setEnabled(false);
        ivZhiYa.setEnabled(false);
        ivTuiNa.setEnabled(false);
        ivJiaRe.setEnabled(false);
    }

    @Override
    public BaseBLEPresenter getBLEPresenter() {
        return (BaseBLEPresenter) mPresenter;
    }

    @Override
    public void displayDeviceState(List<Boolean> data) {

    }

    @Override
    public void displayTime(String time) {
        tvTimer.setText(time);
    }

    @Override
    public void displayAutoMode(int data3, int data4) {

    }

    /**
     * {@link DeviceDataParser#getTechniqueMode(List)}
     */
    @Override
    public void displayTechniqueMode(int data) {
        ivRouNie.setEnabled(data == 0 || data == 2);
        ivQiaoJi.setEnabled(data == 1 || data == 2);
        ivZhiYa.setEnabled(data == 3);
        ivTuiNa.setEnabled(data == 4);
    }

    /**
     * {@link DeviceDataParser#get3DStrength(List)}
     */
    @Override
    public void display3DStrength(int data) {
        int imgRes;
        if (data == NO_DATA) {
            imgRes = R.mipmap.ic_3d_0;
        } else {
            imgRes = ResourceUtils.getDrawableIDByName("ic_3d_" + (data + 1));
        }
        iv3D.setImageResource(imgRes);
    }

    /**
     * {@link DeviceDataParser#getZeroGravityLevel(List)}}
     */
    @Override
    public void displayZeroGravity(int data) {
        int imgRes;
        if (data == NO_DATA) {
            imgRes = R.mipmap.ic_device_detail_technique_7_off;
        } else {
            imgRes = ResourceUtils.getDrawableIDByName("ic_device_detail_technique_7_on_" + (data + 1));
        }
        ivZeroGravity.setImageResource(imgRes);
    }

    /**
     * {@link DeviceDataParser#getBeiGun$AirScrPlace(List)}
     */
    @Override
    public void display$AirScrPlace(List<Integer> data) {
        /* 背滚 */
        Integer beiGun = data.get(0);
        if (beiGun == NO_DATA) {
            ivBeiGun.setVisibility(View.GONE);
        } else {
            ivBeiGun.setVisibility(View.VISIBLE);
            int imRes = ResourceUtils.getDrawableIDByName("ic_bei_gun_" + (beiGun + 1));
            ivBeiGun.setImageResource(imRes);
        }

        /* 气囊 */
        ivQiNangFoot.setVisibility(data.get(1) == HAS_DATA ? View.VISIBLE : View.GONE);
        ivQiNangLeg.setVisibility(data.get(2) == HAS_DATA ? View.VISIBLE : View.GONE);
        ivQiNangBack.setVisibility(data.get(3) == HAS_DATA ? View.VISIBLE : View.GONE);
        ivQiNangAss.setVisibility(data.get(3) == HAS_DATA ? View.VISIBLE : View.GONE);
        ivQiNangHand.setVisibility(data.get(4) == HAS_DATA ? View.VISIBLE : View.GONE);
        ivQiNangArm.setVisibility(data.get(5) == HAS_DATA ? View.VISIBLE : View.GONE);
    }

    /**
     * {@link DeviceDataParser#getAssistFunctions(List)}
     */
    @Override
    public void displayAssistFunctions(List<Boolean> data) {
        ivJiaRe.setEnabled(data.get(1));
        ivTuiGun.setVisibility(data.get(2) ? View.VISIBLE : View.GONE);
        ivJiaoGun.setVisibility(data.get(3) ? View.VISIBLE : View.GONE);
    }

    /**
     * {@link DeviceDataParser#getGearLevel1(List)}
     */
    @Override
    public void displayGearLevel1(List<Integer> data) {
        DisplayUtils.showDeviceProgress(data.get(0), 5, pb1);
        DisplayUtils.showDeviceProgress(data.get(1), 3, pb3);
    }

    /**
     * {@link DeviceDataParser#getGearLevel3(List)}
     */
    @Override
    public void displayGearLevel3(int data) {
        DisplayUtils.showDeviceProgress(data, 3, pb4);
    }

    @Override
    public void onConnectSuccess() {

    }

    @Override
    public void onConnectFailure() {

    }

    /**
     * {@link DeviceDataParser#getGearLevel2(List)}
     */
    @Override
    public void displayGearLevel2(int data) {
        DisplayUtils.showDeviceProgress(data, 5, pb2);
    }

}
