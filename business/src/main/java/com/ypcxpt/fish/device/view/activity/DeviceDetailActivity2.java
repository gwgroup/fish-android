package com.ypcxpt.fish.device.view.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.blankj.utilcode.util.ObjectUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.DisplayUtils;
import com.ypcxpt.fish.core.ble.input.DeviceDataParser;
import com.ypcxpt.fish.core.ble.output.data.AutoActionData;
import com.ypcxpt.fish.core.ble.output.data.ManualActionData;
import com.ypcxpt.fish.core.model.DeviceAction;
import com.ypcxpt.fish.device.contract.DeviceDetailContract;
import com.ypcxpt.fish.device.presenter.BaseBLEPresenter;
import com.ypcxpt.fish.device.presenter.DeviceDetailPresenter;
import com.ypcxpt.fish.library.ui.fuction.LongClickDeviceItemView;
import com.ypcxpt.fish.library.ui.fuction.OnActionListener;
import com.ypcxpt.fish.library.ui.widget.popup.OnPopupItemClickListener;
import com.ypcxpt.fish.library.ui.widget.popup.actionsheet.ActionSheet;
import com.ypcxpt.fish.library.ui.widget.popup.slide.SlidingMenu;
import com.ypcxpt.fish.library.util.ResourceUtils;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;
import static com.ypcxpt.fish.app.util.DisplayUtils.DEFAULT_MAX_PROGRESS;
import static com.ypcxpt.fish.app.util.DisplayUtils.DEVICE_DETAIL_NO_DATA;
import static com.ypcxpt.fish.core.app.Constant.HAS_DATA;
import static com.ypcxpt.fish.core.app.Constant.NO_DATA;
import static com.ypcxpt.fish.core.ble.output.data.DeviceConstant.AIR_INTENSITY_MAX_LEVEL;

//@Route(path = Path.Device.DETAIL)
public class DeviceDetailActivity2 extends BaseActivity implements DeviceDetailContract.View {
    @BindView(R.id.supl) SlidingUpPanelLayout supl;

    /* 底部 */
    @BindView(R.id.rl_bottom) RelativeLayout rlBottom;
    @BindView(R.id.iv_bottom_arrow) ImageView ivBottomArrow;
    @BindView(R.id.iv_bottom_pause) ImageView ivBottomPause;

    /* 更多功能 */
    @BindView(R.id.ll_slide) LinearLayout llSlide;
    @BindView(R.id.tv_more_func_air_intensity) TextView tvMFAirIntensity;
    @BindView(R.id.tv_more_func_3d_strength) TextView tvMF3DStrength;

    @BindView(R.id.ll_d_d_error) LinearLayout llError;

    /* 显示板-时间 */
    @BindView(R.id.tv_timer) TextView tvTimer;
    /* 显示板-3D/脚滚/背滚/腿滚 */
    @BindView(R.id.iv_3d) ImageView iv3D;
    @BindView(R.id.iv_bei_gun) ImageView ivBeiGun;
    @BindView(R.id.iv_jiao_gun) ImageView ivJiaoGun;
    @BindView(R.id.iv_tui_gun) ImageView ivTuiGun;
    /* 显示板-手法 */
    @BindView(R.id.iv_rou_nie) ImageView ivRouNie;
    @BindView(R.id.iv_qiao_ji) ImageView ivQiaoJi;
    @BindView(R.id.iv_zhi_ya) ImageView ivZhiYa;
    @BindView(R.id.iv_tui_na) ImageView ivTuiNa;
    @BindView(R.id.iv_jia_re) ImageView ivJiaRe;
    @BindView(R.id.iv_zero_gravity) ImageView ivZeroGravity;
    /* 显示板-气囊 */
    @BindView(R.id.iv_qi_nang_foot) ImageView ivQiNangFoot;
    @BindView(R.id.iv_qi_nang_leg) ImageView ivQiNangLeg;
    @BindView(R.id.iv_qi_nang_back) ImageView ivQiNangBack;
    @BindView(R.id.iv_qi_nang_ass) ImageView ivQiNangAss;
    @BindView(R.id.iv_qi_nang_hand) ImageView ivQiNangHand;
    @BindView(R.id.iv_qi_nang_arm) ImageView ivQiNangArm;
    /* 显示板-进度条 */
    @BindView(R.id.pb1) ProgressBar pb1;
    @BindView(R.id.pb2) ProgressBar pb2;
    @BindView(R.id.pb3) ProgressBar pb3;
    @BindView(R.id.pb4) ProgressBar pb4;

    @Autowired
    public String macAddress;

    @Autowired
    public String characteristicUuid;

    private DeviceDetailContract.Presenter mPresenter;

    private ActionSheet<DeviceAction> mPopup;
    private SlidingMenu<DeviceAction> mPopup2;

    @Override
    protected int layoutResID() {
        return R.layout.activity_device_detail2;
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
        Log.i("DeviceDetailActivity2", "macAddress:" + macAddress + ",characteristicUuid:" + UUID.fromString(characteristicUuid));
    }

    @Override
    protected void initViews() {
        initSupl();
        initPopup();
        initState();
        initLongClickFunc();
    }

    private void initSupl() {
//        supl.setAnchorPoint(0.65f);
        supl.setPanelState(COLLAPSED);
        supl.setFadeOnClickListener(v -> supl.setPanelState(COLLAPSED));
        supl.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    ivBottomArrow.setImageResource(R.mipmap.ic_device_detail_arrow_down);
                    rlBottom.setBackgroundResource(R.mipmap.bg_device_detail_bottom_up);
                } else {
                    ivBottomArrow.setImageResource(R.mipmap.ic_device_detail_arrow_up);
                    rlBottom.setBackgroundResource(R.mipmap.bg_device_detail_bottom);
                }
            }
        });
        llSlide.setOnClickListener(v -> {
        });
    }

    private void initPopup() {
        mPopup = new ActionSheet<>(this, mMainActionItemClickListener);
        mPopup2 = new SlidingMenu<>(this, mMainActionItemClickListener);
    }

    private void initState() {
        ivRouNie.setEnabled(false);
        ivQiaoJi.setEnabled(false);
        ivZhiYa.setEnabled(false);
        ivTuiNa.setEnabled(false);
        ivJiaRe.setEnabled(false);

        pb1.setMax(DEFAULT_MAX_PROGRESS);
        pb2.setMax(DEFAULT_MAX_PROGRESS);
        pb3.setMax(DEFAULT_MAX_PROGRESS);
        pb4.setMax(DEFAULT_MAX_PROGRESS);

        supl.setVisibility(View.INVISIBLE);
        llError.setVisibility(View.GONE);
        showLoading();
    }

    private void initLongClickFunc() {
        boolean normalClick = true;
        if (normalClick) {
            findViewById(R.id.rl_more_func_back_up).setOnClickListener(v -> mPresenter.onBackAltitude(true));
            findViewById(R.id.rl_more_func_back_down).setOnClickListener(v -> mPresenter.onBackAltitude(false));
            findViewById(R.id.rl_more_func_leg_up).setOnClickListener(v -> mPresenter.onLegAltitude(true));
            findViewById(R.id.rl_more_func_leg_down).setOnClickListener(v -> mPresenter.onLegAltitude(false));
            findViewById(R.id.rl_more_func_leg_stretch).setOnClickListener(v -> mPresenter.ontLegFlex(true));
            findViewById(R.id.rl_more_func_leg_tuck).setOnClickListener(v -> mPresenter.ontLegFlex(false));
        } else {
            ((LongClickDeviceItemView) findViewById(R.id.rl_more_func_back_up)).setOnActionListener(mOnLongClickItemAction);
            ((LongClickDeviceItemView) findViewById(R.id.rl_more_func_back_down)).setOnActionListener(mOnLongClickItemAction);
            ((LongClickDeviceItemView) findViewById(R.id.rl_more_func_leg_up)).setOnActionListener(mOnLongClickItemAction);
            ((LongClickDeviceItemView) findViewById(R.id.rl_more_func_leg_down)).setOnActionListener(mOnLongClickItemAction);
            ((LongClickDeviceItemView) findViewById(R.id.rl_more_func_leg_stretch)).setOnActionListener(mOnLongClickItemAction);
            ((LongClickDeviceItemView) findViewById(R.id.rl_more_func_leg_tuck)).setOnActionListener(mOnLongClickItemAction);
        }
    }

    @Override
    public BaseBLEPresenter getBLEPresenter() {
        return (BaseBLEPresenter) mPresenter;
    }

    /**
     * {@link DeviceDataParser#getDeviceState(List)}
     */
    @Override
    public void displayDeviceState(List<Boolean> data) {
        boolean isPowerOn = data.get(0);
        boolean isPause = data.get(1);
        if (isPowerOn) {
            if (isPause) {
                ivBottomPause.setImageResource(R.mipmap.ic_device_detail_start);
            } else {
                ivBottomPause.setImageResource(R.mipmap.ic_device_detail_pause);
            }
        }
    }

    /**
     * {@link DeviceDataParser#getTime(List)}
     */
    @Override
    public void displayTime(String time) {
        ViewHelper.setNoneNullText(tvTimer, time);
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
            tvMF3DStrength.setText(DEVICE_DETAIL_NO_DATA);
        } else {
            int level = data + 1;
            imgRes = ResourceUtils.getDrawableIDByName("ic_3d_" + level);
            tvMF3DStrength.setText(level + "档");
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
        if (ObjectUtils.isEmpty(data)) {
            return;
        }

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
        if (ObjectUtils.isEmpty(data)) {
            return;
        }

        ivJiaRe.setEnabled(data.get(1));
        ivTuiGun.setVisibility(data.get(2) ? View.VISIBLE : View.GONE);
        ivJiaoGun.setVisibility(data.get(3) ? View.VISIBLE : View.GONE);
    }

    /**
     * {@link DeviceDataParser#getGearLevel1(List)}
     */
    @Override
    public void displayGearLevel1(List<Integer> data) {
        if (ObjectUtils.isEmpty(data)) {
            return;
        }

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
        if (supl.getVisibility() != View.VISIBLE) {
            supl.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
            dismissLoading();
        }
    }

    @Override
    public void onConnectFailure() {
        dismissLoading();
        supl.setVisibility(View.INVISIBLE);
        llError.setVisibility(View.VISIBLE);
    }

    /**
     * {@link DeviceDataParser#getGearLevel2(List)}
     */
    @Override
    public void displayGearLevel2(int data) {
        if (data == NO_DATA) {
            tvMFAirIntensity.setText(DEVICE_DETAIL_NO_DATA);
        } else {
            tvMFAirIntensity.setText((data + 1) + "档");
        }
        DisplayUtils.showDeviceProgress(data, AIR_INTENSITY_MAX_LEVEL, pb2);
    }

    private void showPopup(List<DeviceAction> deviceActionList) {
        mPopup2.setNewData(deviceActionList);
        mPopup2.showPopupWindow();
    }

    private OnPopupItemClickListener<DeviceAction> mMainActionItemClickListener =
            itemData ->
                    getBLEPresenter().writeData(itemData);

    private OnActionListener mOnLongClickItemAction = new OnActionListener() {
        @Override
        public void onActionDown(View view) {
            switch (view.getId()) {
                case R.id.rl_more_func_back_up:
                    mPresenter.startBackAltitude(true);
                    break;
                case R.id.rl_more_func_back_down:
                    mPresenter.startBackAltitude(false);
                    break;
                case R.id.rl_more_func_leg_up:
                    mPresenter.startLegAltitude(true);
                    break;
                case R.id.rl_more_func_leg_down:
                    mPresenter.startLegAltitude(false);
                    break;
                case R.id.rl_more_func_leg_stretch:
                    mPresenter.startLegFlex(true);
                    break;
                case R.id.rl_more_func_leg_tuck:
                    mPresenter.startLegFlex(false);
                    break;
            }
        }

        @Override
        public void onActionUp(View view) {
            switch (view.getId()) {
                case R.id.rl_more_func_back_up:
                    mPresenter.endBackAltitude(true);
                    break;
                case R.id.rl_more_func_back_down:
                    mPresenter.endBackAltitude(false);
                    break;
                case R.id.rl_more_func_leg_up:
                    mPresenter.endLegAltitude(true);
                    break;
                case R.id.rl_more_func_leg_down:
                    mPresenter.endLegAltitude(false);
                    break;
                case R.id.rl_more_func_leg_stretch:
                    mPresenter.endLegFlex(true);
                    break;
                case R.id.rl_more_func_leg_tuck:
                    mPresenter.endLegFlex(false);
                    break;
            }
        }
    };

    @OnClick(R.id.ll_main_function_technique)
    public void mainFuncTechnique() {
        showPopup(ManualActionData.getTechniqueModeList());
    }

    @OnClick(R.id.ll_main_function_zero_gravity)
    public void mainFuncZeroGravity() {
//        showPopup(ManualActionData.getZeroGravityList());
        mPresenter.toggleZeroGravity();
    }

    @OnClick(R.id.ll_main_function_body_part)
    public void mainFuncBodyPart() {
        showPopup(ManualActionData.getBodyPartList());
    }

    @OnClick(R.id.ll_main_function_auto_mode)
    public void mainFuncAutoMode() {
        showPopup(AutoActionData.get());
    }

    @OnClick(R.id.tv_error_retry)
    public void errorRetry(){
        mPresenter.openDataFetching();
        showLoading();
    }

    @OnClick(R.id.tv_error_exit)
    public void errorExit(){
        finish();
    }

    @OnClick(R.id.iv_bottom_power)
    public void bottomPower() {
        mPresenter.togglePower();
    }

    @OnClick(R.id.iv_bottom_pause)
    public void bottomPause() {
        mPresenter.togglePause();
    }

    @OnClick({R.id.iv_more_func_air_intensity_add, R.id.iv_more_func_air_intensity_minus})
    public void moreFuncAirIntensity(View view) {
        switch (view.getId()) {
            case R.id.iv_more_func_air_intensity_add:
                mPresenter.changeAirIntensity(true);
                break;
            case R.id.iv_more_func_air_intensity_minus:
                mPresenter.changeAirIntensity(false);
                break;
        }
    }

    @OnClick({R.id.iv_more_func_3d_strength_add, R.id.iv_more_func_3d_strength_minus})
    public void moreFunc3DStrength(View view) {
        switch (view.getId()) {
            case R.id.iv_more_func_3d_strength_add:
                mPresenter.change3DStrength(true);
                break;
            case R.id.iv_more_func_3d_strength_minus:
                mPresenter.change3DStrength(false);
                break;
        }
    }

    @OnClick({R.id.rl_more_func_jia_re, R.id.rl_more_func_jiao_gun, R.id.rl_more_func_tui_gun})
    public void individualMoreFunc(View view) {
        switch (view.getId()) {
            case R.id.rl_more_func_jia_re:
                mPresenter.toggleHeat();
                break;
            case R.id.rl_more_func_jiao_gun:
                mPresenter.toggleJiaoGun();
                break;
            case R.id.rl_more_func_tui_gun:
                mPresenter.toggleTuiGun();
                break;
        }
    }

    @OnClick(R.id.iv_bottom_arrow)
    public void bottomArrow() {
        if (supl.getPanelState() == EXPANDED) {
            supl.setPanelState(COLLAPSED);
        } else if (supl.getPanelState() == COLLAPSED) {
            supl.setPanelState(EXPANDED);
        } else if (supl.getPanelState() == ANCHORED) {
            supl.setPanelState(EXPANDED);
        }
    }


}
