package com.ypcxpt.fish.device.view.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.core.ble.output.data.T100ManualActionData;
import com.ypcxpt.fish.device.contract.T100Contract;
import com.ypcxpt.fish.device.event.OnUseDeviceEvent;
import com.ypcxpt.fish.device.presenter.BaseBLEt100Presenter;
import com.ypcxpt.fish.device.presenter.T100Presenter;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;

@Route(path = Path.Device.T100)
public class T100DeviceDetailActivity extends BaseActivity implements T100Contract.View {
    @BindView(R.id.tv_title) TextView tv_title;

    @BindView(R.id.supl) SlidingUpPanelLayout supl;
    @BindView(R.id.ll_slide) LinearLayout llSlide;//更多功能
    /* 底部 */
    @BindView(R.id.iv_bottom_arrow) ImageView ivBottomArrow;

    @BindView(R.id.rl_bottom_power) RelativeLayout rl_bottom_power;//开关
    @BindView(R.id.iv_bottom_power) ImageView ivBottomPower;//开关
    @BindView(R.id.tv_power) TextView tv_power;
    @BindView(R.id.iv_img) ImageView iv_img;
    @BindView(R.id.iv_gif) ImageView iv_gif;

    @BindView(R.id.ll_d_d_error) LinearLayout llError;

    @BindView(R.id.tv_timer) TextView tv_timer;//时间

    //自动程序4个功能
    @BindView(R.id.ll_auto_tuntui) LinearLayout ll_auto_tuntui;//臀腿
    @BindView(R.id.ll_auto_huyao) LinearLayout ll_auto_huyao;//护腰
    @BindView(R.id.ll_auto_huoli) LinearLayout ll_auto_huoli;//活力
    @BindView(R.id.ll_auto_shuimian) LinearLayout ll_auto_shuimian;//睡眠

    //颈部按摩
    @BindView(R.id.ll_neckDown) LinearLayout ll_neckDown;
    @BindView(R.id.ll_neckUp) LinearLayout ll_neckUp;
    @BindView(R.id.ll_neckPositive) LinearLayout ll_neckPositive;
    @BindView(R.id.ll_neckNegative) LinearLayout ll_neckNegative;

    //背部按摩
    @BindView(R.id.ll_backUp) LinearLayout ll_backUp;
    @BindView(R.id.ll_backAll) LinearLayout ll_backAll;
    @BindView(R.id.ll_backDown) LinearLayout ll_backDown;

    //气压
    @BindView(R.id.ll_airLow) LinearLayout ll_airLow;
    @BindView(R.id.ll_airMid) LinearLayout ll_airMid;
    @BindView(R.id.ll_airHigh) LinearLayout ll_airHigh;

    //辅助功能
    @BindView(R.id.ll_heat) LinearLayout ll_heat;
    @BindView(R.id.ll_spot) LinearLayout ll_spot;

    @Autowired
    public String macAddress;
    @Autowired
    public String chairName;
    @Autowired
    public String characteristicUuid;

    private T100Contract.Presenter mPresenter;

    @Override
    protected int layoutResID() {
        return R.layout.activity_device_detail_t100;
    }

    @Override
    protected ImmersionBar initImmersionBar() {
        return ImmersionBar.with(this).statusBarColor(R.color.main_color_new).fitsSystemWindows(true);
    }

    @Override
    protected void initData() {
        mPresenter = new T100Presenter();
        addPresenter(mPresenter);
        mPresenter.acceptData("chairName", chairName);
        mPresenter.acceptData("macAddress", macAddress);
        mPresenter.acceptData("characteristicUuid", UUID.fromString(characteristicUuid));
        Logger.e("T100DeviceDetailActivity", "macAddress:" + macAddress + ",characteristicUuid:" + UUID.fromString(characteristicUuid));
    }

    @Override
    protected void initViews() {
        tv_title.setText("T100");
        initSupl();
        initState();
//        initLongClickFunc();

        addUseDevice();
    }

    private void addUseDevice() {
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnUseDeviceEvent()), 500);
    }

    @Subscribe
    public void onEventReceived(OnUseDeviceEvent event) {
        mPresenter.useDevice(macAddress);
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
                    ivBottomArrow.setImageResource(R.mipmap.ic_device_detail_arrow_ed);
//                    rlBottom.setBackgroundResource(R.mipmap.bg_device_detail_bottom_up);
                } else {
                    ivBottomArrow.setImageResource(R.mipmap.ic_device_detail_arrow_u);
//                    rlBottom.setBackgroundResource(R.mipmap.bg_device_detail_bottom);
                }
            }
        });
        llSlide.setOnClickListener(v -> {
        });
    }

    private void initState() {
        supl.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);
//        showLoading();
    }

//    private void initLongClickFunc() {
//        boolean normalClick = true;
//        if (normalClick) {
////            findViewById(R.id.rl_more_func_back_up).setOnClickListener(v -> mPresenter.onBackAltitude(true));
////            findViewById(R.id.rl_more_func_back_down).setOnClickListener(v -> mPresenter.onBackAltitude(false));
//        } else {
//            ((LongClickDeviceItemView) findViewById(R.id.rl_more_func_back_up)).setOnActionListener(mOnLongClickItemAction);
//            ((LongClickDeviceItemView) findViewById(R.id.rl_more_func_back_down)).setOnActionListener(mOnLongClickItemAction);
//        }
//    }

    @Override
    public BaseBLEt100Presenter getBLEPresenter() {
        return (BaseBLEt100Presenter) mPresenter;
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        finish();
    }

    /**
     * 0关机1开机
     */
    @Override
    public void displayDeviceState(int data) {
        if (data == 1) {
            ivBottomPower.setImageResource(R.mipmap.ic_device_detail_t100_power_d);
            tv_power.setTextColor(getResources().getColor(R.color.t100_blue));

            iv_img.setVisibility(View.GONE);
            iv_gif.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.mipmap.icon_t100_g).into(iv_gif);
        } else {
            //未开机状态
            ivBottomPower.setImageResource(R.mipmap.ic_device_detail_t100_power_u);
            tv_power.setTextColor(getResources().getColor(R.color.white));

            iv_img.setVisibility(View.VISIBLE);
            iv_gif.setVisibility(View.GONE);

            setAllImagebgClose();
        }
    }

    private void setAllImagebgClose() {
        ll_auto_tuntui.setBackgroundResource(R.mipmap.bg_t100_btn_u);
        ll_auto_huyao.setBackgroundResource(R.mipmap.bg_t100_btn_u);
        ll_auto_huoli.setBackgroundResource(R.mipmap.bg_t100_btn_u);
        ll_auto_shuimian.setBackgroundResource(R.mipmap.bg_t100_btn_u);

        ll_neckDown.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_neckUp.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_neckPositive.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_neckNegative.setBackgroundResource(R.mipmap.bg_t100_btn2_u);

        ll_backUp.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_backAll.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_backDown.setBackgroundResource(R.mipmap.bg_t100_btn2_u);

        ll_airLow.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_airMid.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_airHigh.setBackgroundResource(R.mipmap.bg_t100_btn2_u);

        ll_heat.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_spot.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
    }

    private int neckPN = 0;
    /**
     * 0关1正2反
     */
    @Override
    public void displayNeckPositiveAndNegative(int data) {
//        Logger.e("颈部按摩正反", data + "");
        neckPN = data;
        if (data == 0) {
            setNeckPNImagebgClose();
        } else if (data == 1) {
            setNeckPNImagebgClose();
            ll_neckPositive.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        } else if (data == 2) {
            setNeckPNImagebgClose();
            ll_neckNegative.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        }
    }

    private void setNeckPNImagebgClose() {
        ll_neckPositive.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_neckNegative.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
    }

    private int neckUD = 0;
    /**
     * 0关1上2下
     */
    @Override
    public void displayNeckUpAndDown(int data) {
//        Logger.e("颈部按摩上下", "" + data);
        neckUD = data;
        if (data == 0) {
            setNeckUDImagebgClose();
        } else if (data == 1) {
            setNeckUDImagebgClose();
            ll_neckUp.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        } else if (data == 2) {
            setNeckUDImagebgClose();
            ll_neckDown.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        }
    }

    private void setNeckUDImagebgClose() {
        ll_neckUp.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_neckDown.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
    }

    private int autoBack = 0;
    /**
     * 0关1全背2上背3下背7背部定点开 8/9/10/11(四个模式)
     * @param data
     */
    @Override
    public void displayAutoMode(int data) {
//        Logger.e("模式", "" + data);
        autoBack = data;
        if (data == 0) {
            setBackImagebgClose();
            setAutoImagebgClose();
        } else if (data == 1) {
            setBackImagebgClose();
            setAutoImagebgClose();
            ll_backAll.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        } else if (data == 2) {
            setBackImagebgClose();
            setAutoImagebgClose();
            ll_backUp.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        } else if (data == 3) {
            setBackImagebgClose();
            setAutoImagebgClose();
            ll_backDown.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        } else if (data == 7) {
            setBackImagebgClose();
            setAutoImagebgClose();
            ll_spot.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        } else if (data == 8) {
            setBackImagebgClose();
            setAutoImagebgClose();
            ll_auto_huoli.setBackgroundResource(R.mipmap.bg_t100_btn_d);
        } else if (data == 9) {
            setBackImagebgClose();
            setAutoImagebgClose();
            ll_auto_huyao.setBackgroundResource(R.mipmap.bg_t100_btn_d);
        } else if (data == 10) {
            setBackImagebgClose();
            setAutoImagebgClose();
            ll_auto_tuntui.setBackgroundResource(R.mipmap.bg_t100_btn_d);
        } else if (data == 11) {
            setBackImagebgClose();
            setAutoImagebgClose();
            ll_auto_shuimian.setBackgroundResource(R.mipmap.bg_t100_btn_d);
        }
    }

    private void setBackImagebgClose() {
        ll_backUp.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_backAll.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_backDown.setBackgroundResource(R.mipmap.bg_t100_btn2_u);

        ll_spot.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
    }
    private void setAutoImagebgClose() {
        ll_auto_tuntui.setBackgroundResource(R.mipmap.bg_t100_btn_u);
        ll_auto_huyao.setBackgroundResource(R.mipmap.bg_t100_btn_u);
        ll_auto_huoli.setBackgroundResource(R.mipmap.bg_t100_btn_u);
        ll_auto_shuimian.setBackgroundResource(R.mipmap.bg_t100_btn_u);
    }

    private int air = 0;
    /**
     * 0关1低2中3高
     */
    @Override
    public void displayAir(int data) {
//        Logger.e("气压档位", "" + data);
        air = data;
        if (data == 0) {
            setAirImagebgClose();
        } else if (data == 1) {
            setAirImagebgClose();
            ll_airLow.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        } else if (data == 2) {
            setAirImagebgClose();
            ll_airMid.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        } else if (data == 3) {
            setAirImagebgClose();
            ll_airHigh.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        }
    }

    private void setAirImagebgClose() {
        ll_airLow.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_airMid.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        ll_airHigh.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
    }

    private int heat = 0;
    /**
     * 0关1加热
     */
    @Override
    public void displayHeat(int data) {
//        Logger.e("加热", "" + data);
        heat = data;
        if (data == 0) {
            ll_heat.setBackgroundResource(R.mipmap.bg_t100_btn2_u);
        } else if (data == 1) {
            ll_heat.setBackgroundResource(R.mipmap.bg_t100_btn2_d);
        }
    }

    @Override
    public void displayTime(String time) {
        ViewHelper.setNoneNullText(tv_timer, time);
    }

    @Override
    public void onConnectSuccess() {
//        Logger.e("onConnectSuccess", "连接成功-->");
        if (supl.getVisibility() != View.VISIBLE) {
            Logger.e("onConnectSuccess", "supl不显示-->if");
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

    @OnClick(R.id.rl_bottom_power)
    public void bottomPower() {
        mPresenter.togglePower();
    }

    //颈部按摩
    @OnClick({R.id.ll_neckDown, R.id.ll_neckUp, R.id.ll_neckPositive, R.id.ll_neckNegative})
    public void manualNeckFunction(View view) {
        switch (view.getId()) {
            case R.id.ll_neckDown:
                //颈部向下按摩
                if (neckUD == 2) {
                    //发送关闭指令7
                    getBLEPresenter().writeData(T100ManualActionData.getNeck2().get(2));
                } else {
                    //发送正按摩指令6
                    getBLEPresenter().writeData(T100ManualActionData.getNeck2().get(1));
                }
                break;
            case R.id.ll_neckUp:
                //颈部向上按摩 neckUD值为1向上按摩；为2向下按摩
                if (neckUD == 1) {
                    //发送关闭指令7
                    getBLEPresenter().writeData(T100ManualActionData.getNeck2().get(2));
                } else {
                    //发送正按摩指令5
                    getBLEPresenter().writeData(T100ManualActionData.getNeck2().get(0));
                }
                break;
            case R.id.ll_neckPositive:
                //颈部正按摩 neckPN值为1正按摩；为2反按摩
                if (neckPN == 1) {
                    //发送关闭指令4
                    getBLEPresenter().writeData(T100ManualActionData.getNeck().get(2));
                } else {
                    //发送正按摩指令2
                    getBLEPresenter().writeData(T100ManualActionData.getNeck().get(0));
                }
                break;
            case R.id.ll_neckNegative:
                //颈部反按摩
                if (neckPN == 2) {
                    //发送关闭指令4
                    getBLEPresenter().writeData(T100ManualActionData.getNeck().get(2));
                } else {
                    //发送反按摩指令3
                    getBLEPresenter().writeData(T100ManualActionData.getNeck().get(1));
                }
                break;
        }
    }

    //背部按摩
    @OnClick({R.id.ll_backUp, R.id.ll_backAll, R.id.ll_backDown})
    public void manualBackFunction(View view) {
        switch (view.getId()) {
            //上背按摩 autoBack:0关1全背2上背3下背7背部定点开 8/9/10/11(四个模式)
            case R.id.ll_backUp:
                if (autoBack == 2) {
                    //发送关闭指令11
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(3));
                } else {
                    //发送上背按摩指令9{8, 9, 10, 11, 12}
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(1));
                }
                break;
            //全部按摩
            case R.id.ll_backAll:
                if (autoBack == 1) {
                    //发送关闭指令11
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(3));
                } else {
                    //发送全背按摩指令8{8, 9, 10, 11, 12}
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(0));
                }
                break;
            //下背按摩
            case R.id.ll_backDown:
                if (autoBack == 3) {
                    //发送关闭指令11
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(3));
                } else {
                    //发送下背按摩指令10{8, 9, 10, 11, 12}
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(2));
                }
                break;
        }
    }

    //自动模式4个功能 autoBack:0关1全背2上背3下背7背部定点开 8/9/10/11(四个模式)
    @OnClick({R.id.ll_auto_tuntui, R.id.ll_auto_huyao, R.id.ll_auto_huoli, R.id.ll_auto_shuimian})
    public void manualAutoFunction(View view) {
        switch (view.getId()) {
            case R.id.ll_auto_tuntui:
                if (autoBack == 10) {
                    //发送关闭指令11{8, 9, 10, 11, 12}
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(3));
                } else {
                    //发送自动模式3指令15{13, 14, 15, 16}
                    getBLEPresenter().writeData(T100ManualActionData.getAuto().get(2));
                }
                break;
            case R.id.ll_auto_huyao:
                if (autoBack == 9) {
                    //发送关闭指令11{8, 9, 10, 11, 12}
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(3));
                } else {
                    //发送自动模式2指令14{13, 14, 15, 16}
                    getBLEPresenter().writeData(T100ManualActionData.getAuto().get(1));
                }
                break;
            case R.id.ll_auto_huoli:
                if (autoBack == 8) {
                    //发送关闭指令11{8, 9, 10, 11, 12}
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(3));
                } else {
                    //发送自动模式1指令13{13, 14, 15, 16}
                    getBLEPresenter().writeData(T100ManualActionData.getAuto().get(0));
                }
                break;
            case R.id.ll_auto_shuimian:
                if (autoBack == 11) {
                    //发送关闭指令11{8, 9, 10, 11, 12}
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(3));
                } else {
                    //发送自动模式4指令16{13, 14, 15, 16}
                    getBLEPresenter().writeData(T100ManualActionData.getAuto().get(3));
                }
                break;
        }
    }

    //辅助功能
    @OnClick({R.id.ll_heat, R.id.ll_spot})
    public void individualMoreFunc(View view) {
        switch (view.getId()) {
            case R.id.ll_heat:
                //0关1加热
                if (heat == 1) {
                    //发送关闭指令22{21, 22}
                    getBLEPresenter().writeData(T100ManualActionData.getHeat().get(1));
                } else {
                    //发送加热指令21
                    getBLEPresenter().writeData(T100ManualActionData.getHeat().get(0));
                }
                break;
            case R.id.ll_spot:
                if (autoBack == 7) {
                    //发送关闭指令11
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(3));
                } else {
                    //发送背部定点按摩指令12
                    getBLEPresenter().writeData(T100ManualActionData.getBack().get(4));
                }
                break;
        }
    }

    //气压
    @OnClick({R.id.ll_airLow, R.id.ll_airMid, R.id.ll_airHigh})
    public void manualAirFunction(View view) {
        switch (view.getId()) {
            case R.id.ll_airLow:
                //0关1低2中3高
                if (air == 1) {
                    //发送关闭指令20{17, 18, 19, 20};
                    getBLEPresenter().writeData(T100ManualActionData.getAir().get(3));
                } else {
                    //发送气压低指令19
                    getBLEPresenter().writeData(T100ManualActionData.getAir().get(2));
                }
                break;
            case R.id.ll_airMid:
                if (air == 2) {
                    //发送关闭指令20{17, 18, 19, 20};
                    getBLEPresenter().writeData(T100ManualActionData.getAir().get(3));
                } else {
                    //发送气压中指令18
                    getBLEPresenter().writeData(T100ManualActionData.getAir().get(1));
                }
                break;
            case R.id.ll_airHigh:
                if (air == 3) {
                    //发送关闭指令20{17, 18, 19, 20};
                    getBLEPresenter().writeData(T100ManualActionData.getAir().get(3));
                } else {
                    //发送气压高指令17
                    getBLEPresenter().writeData(T100ManualActionData.getAir().get(0));
                }
                break;
        }
    }

    @OnClick(R.id.tv_error_retry)
    public void errorRetry() {
        mPresenter.openDataFetching();
        showLoading();
    }

    @OnClick(R.id.tv_error_exit)
    public void errorExit() {
        finish();
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
