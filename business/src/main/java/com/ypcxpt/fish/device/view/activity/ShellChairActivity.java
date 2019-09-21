package com.ypcxpt.fish.device.view.activity;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.graphics.RectF;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.blankj.utilcode.util.ObjectUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gyf.barlibrary.ImmersionBar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.DisplayUtils;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.core.ble.input.ShellchairDataParser;
import com.ypcxpt.fish.core.ble.output.data.ShellAutoActionData;
import com.ypcxpt.fish.core.ble.output.data.ShellManualActionData;
import com.ypcxpt.fish.core.model.DeviceAction;
import com.ypcxpt.fish.device.contract.ShellchairContract;
import com.ypcxpt.fish.device.event.OnUseDeviceEvent;
import com.ypcxpt.fish.device.presenter.BaseBLEPresenter;
import com.ypcxpt.fish.device.presenter.ShellchairPresenter;
import com.ypcxpt.fish.library.ui.fuction.LongClickDeviceItemView;
import com.ypcxpt.fish.library.ui.fuction.OnActionListener;
import com.ypcxpt.fish.library.ui.widget.popup.OnPopupItemClickListener;
import com.ypcxpt.fish.library.ui.widget.popup.actionsheet.ActionSheet;
import com.ypcxpt.fish.library.ui.widget.popup.slide.SlidingMenu;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ResourceUtils;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

@Route(path = Path.Device.SHELL_CHAIR)
public class ShellChairActivity extends BaseActivity implements ShellchairContract.View {
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.supl) SlidingUpPanelLayout supl;

    /* 底部 */
//    @BindView(R.id.rl_bottom) RelativeLayout rlBottom;
    @BindView(R.id.iv_bottom_arrow) ImageView ivBottomArrow;
    @BindView(R.id.rl_bottom_pause) RelativeLayout rl_bottom_pause;//暂停
    @BindView(R.id.iv_bottom_pause) ImageView ivBottomPause;//暂停
    @BindView(R.id.rl_bottom_power) RelativeLayout rl_bottom_power;//开关
    @BindView(R.id.iv_bottom_power) ImageView ivBottomPower;//开关

    /* 更多功能 */
    @BindView(R.id.ll_slide) LinearLayout llSlide;
    @BindView(R.id.tv_more_func_air_intensity) TextView tvMFAirIntensity;
    @BindView(R.id.tv_more_func_3d_strength) TextView tvMF3DStrength;
    @BindView(R.id.tv_more_func_width) TextView tv_more_func_width;

    @BindView(R.id.ll_d_d_error) LinearLayout llError;

    /* 显示板-时间 */
    @BindView(R.id.tv_timer) TextView tvTimer;
    @BindView(R.id.tv_more_func_timer) TextView tv_more_func_timer;
    /* 显示板-3D/脚滚/背滚/腿滚 */
    @BindView(R.id.iv_3d) ImageView iv3D;
    @BindView(R.id.iv_bei_gun) ImageView ivBeiGun;
    @BindView(R.id.iv_bei_gun01) ImageView iv_bei_gun01;
    @BindView(R.id.iv_bei_gun02) ImageView iv_bei_gun02;
    @BindView(R.id.iv_bei_gun03) ImageView iv_bei_gun03;
    @BindView(R.id.iv_bei_gun04) ImageView iv_bei_gun04;
    @BindView(R.id.iv_bei_gun05) ImageView iv_bei_gun05;
    @BindView(R.id.iv_bei_gun06) ImageView iv_bei_gun06;
    @BindView(R.id.iv_bei_gun07) ImageView iv_bei_gun07;
    @BindView(R.id.iv_bei_gun08) ImageView iv_bei_gun08;
    @BindView(R.id.iv_bei_gun09) ImageView iv_bei_gun09;
    @BindView(R.id.iv_bei_gun10) ImageView iv_bei_gun10;
    @BindView(R.id.iv_bei_gun11) ImageView iv_bei_gun11;
    @BindView(R.id.iv_bei_gun12) ImageView iv_bei_gun12;
    @BindView(R.id.iv_bei_gun13) ImageView iv_bei_gun13;
    @BindView(R.id.iv_bei_gun14) ImageView iv_bei_gun14;
    @BindView(R.id.iv_bei_gun15) ImageView iv_bei_gun15;
    @BindView(R.id.iv_jiao_gun) ImageView ivJiaoGun;
    @BindView(R.id.iv_tui_gun) ImageView ivTuiGun;
    /* 显示板-手法 */
    @BindView(R.id.iv_rou_nie) ImageView ivRouNie;
    @BindView(R.id.iv_qiao_ji) ImageView ivQiaoJi;
    @BindView(R.id.iv_zhi_ya) ImageView ivZhiYa;
    @BindView(R.id.iv_rou_qiao) ImageView ivRouQiao;
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

    /* 强度速度宽度 */
    @BindView(R.id.iv_air) ImageView iv_air;
    @BindView(R.id.iv_speed) ImageView iv_speed;
    @BindView(R.id.iv_width) ImageView iv_width;

    //切换自动和手动按钮iv_change
    @BindView(R.id.iv_change) ImageView iv_change;
    @BindView(R.id.tv_change) TextView tv_change;
    @BindView(R.id.ll_shoudong) LinearLayout ll_shoudong;
    @BindView(R.id.ll_zidong) LinearLayout ll_zidong;
    //手动自动Tab标签按钮
//    @BindView(R.id.tv_shoudong) TextView tv_shoudong;
//    @BindView(R.id.tv_zidong) TextView tv_zidong;

    //手动模块下的滑动布局
    @BindView(R.id.scrollview_shoudong) ScrollView scrollview_shoudong;

    //手动模块4个手法功能
    @BindView(R.id.ll_rounie) LinearLayout ll_rounie;//揉捏
    @BindView(R.id.iv_manual_rounie) ImageView iv_manual_rounie;
    @BindView(R.id.tv_manual_rounie) TextView tv_manual_rounie;
    @BindView(R.id.ll_qiaoji) LinearLayout ll_qiaoji;//敲击
    @BindView(R.id.iv_manual_qiaoji) ImageView iv_manual_qiaoji;
    @BindView(R.id.tv_manual_qiaoji) TextView tv_manual_qiaoji;
    @BindView(R.id.ll_zhiya) LinearLayout ll_zhiya;//指压
    @BindView(R.id.iv_manual_zhiya) ImageView iv_manual_zhiya;
    @BindView(R.id.tv_manual_zhiya) TextView tv_manual_zhiya;
    @BindView(R.id.ll_tuina) LinearLayout ll_tuina;//推拿
    @BindView(R.id.iv_manual_tuina) ImageView iv_manual_tuina;
    @BindView(R.id.tv_manual_tuina) TextView tv_manual_tuina;

    //部位选择
    @BindView(R.id.ll_quyuquanshen) LinearLayout ll_quyuquanshen;//区域全身
    @BindView(R.id.tv_manual_quyuquanshen) TextView tv_manual_quyuquanshen;
    @BindView(R.id.ll_qujianbeibu) LinearLayout ll_qujianbeibu;//区间背部
    @BindView(R.id.tv_manual_qujianbeibu) TextView tv_manual_qujianbeibu;
    @BindView(R.id.ll_qujianyaobu) LinearLayout ll_qujianyaobu;//区间腰部
    @BindView(R.id.tv_manual_qujianyaobu) TextView tv_manual_qujianyaobu;
    @BindView(R.id.ll_qujianzuobu) LinearLayout ll_qujianzuobu;//区间座部
    @BindView(R.id.tv_manual_qujianzuobu) TextView tv_manual_qujianzuobu;
    @BindView(R.id.ll_qujianbeiyao) LinearLayout ll_qujianbeiyao;//区间背腰
    @BindView(R.id.tv_manual_qujianbeiyao) TextView tv_manual_qujianbeiyao;
    @BindView(R.id.ll_qujianyaozuo) LinearLayout ll_qujianyaozuo;//区域腰座
    @BindView(R.id.tv_manual_qujianyaozuo) TextView tv_manual_qujianyaozuo;

    //自动模块功能
    @BindView(R.id.rl_Quanshenfangsong) RelativeLayout rl_Quanshenfangsong;//全身放松
    @BindView(R.id.rl_Shuyatiaojie) RelativeLayout rl_Shuyatiaojie;//舒压调节
    @BindView(R.id.rl_Huolihuanxing) RelativeLayout rl_Huolihuanxing;//活力唤醒
    @BindView(R.id.rl_Qianyinlashen) RelativeLayout rl_Qianyinlashen;//牵引拉伸
    @BindView(R.id.rl_Qingduxiuxi) RelativeLayout rl_Qingduxiuxi;//轻度休息
    @BindView(R.id.rl_Zhijianhuanxing) RelativeLayout rl_Zhijianhuanxing;//指尖唤醒
    @BindView(R.id.rl_Jingjiananmo) RelativeLayout rl_Jingjiananmo;//颈肩按摩
    @BindView(R.id.rl_Hulixiuxin) RelativeLayout rl_Hulixiuxin;//护理修心
    @BindView(R.id.rl_Nvxingshuhuan) RelativeLayout rl_Nvxingshuhuan;//女性舒缓
    @BindView(R.id.rl_Wenrechushi) RelativeLayout rl_Wenrechushi;//温热初始
    @BindView(R.id.rl_Kuaisuyaotun) RelativeLayout rl_Kuaisuyaotun;//快速腰臀
    @BindView(R.id.rl_Kuaisujingjian) RelativeLayout rl_Kuaisujingjian;//快速颈肩
    @BindView(R.id.iv_Quanshenfangsong) ImageView iv_Quanshenfangsong;//全身放松
    @BindView(R.id.iv_Shuyatiaojie) ImageView iv_Shuyatiaojie;//舒压调节
    @BindView(R.id.iv_Huolihuanxing) ImageView iv_Huolihuanxing;//活力唤醒
    @BindView(R.id.iv_Qianyinlashen) ImageView iv_Qianyinlashen;//牵引拉伸
    @BindView(R.id.iv_Qingduxiuxi) ImageView iv_Qingduxiuxi;//轻度休息
    @BindView(R.id.iv_Zhijianhuanxing) ImageView iv_Zhijianhuanxing;//指尖唤醒
    @BindView(R.id.iv_Jingjiananmo) ImageView iv_Jingjiananmo;//颈肩按摩
    @BindView(R.id.iv_Hulixiuxin) ImageView iv_Hulixiuxin;//护理修心
    @BindView(R.id.iv_Nvxingshuhuan) ImageView iv_Nvxingshuhuan;//女性舒缓
    @BindView(R.id.iv_Wenrechushi) ImageView iv_Wenrechushi;//温热初始
    @BindView(R.id.iv_Kuaisuyaotun) ImageView iv_Kuaisuyaotun;//快速腰臀
    @BindView(R.id.iv_Kuaisujingjian) ImageView iv_Kuaisujingjian;//快速颈肩
    @BindView(R.id.tv_Quanshenfangsong) TextView tv_Quanshenfangsong;//全身放松
    @BindView(R.id.tv_Shuyatiaojie) TextView tv_Shuyatiaojie;//舒压调节
    @BindView(R.id.tv_Huolihuanxing) TextView tv_Huolihuanxing;//活力唤醒
    @BindView(R.id.tv_Qianyinlashen) TextView tv_Qianyinlashen;//牵引拉伸
    @BindView(R.id.tv_Qingduxiuxi) TextView tv_Qingduxiuxi;//轻度休息
    @BindView(R.id.tv_Zhijianhuanxing) TextView tv_Zhijianhuanxing;//指尖唤醒
    @BindView(R.id.tv_Jingjiananmo) TextView tv_Jingjiananmo;//颈肩按摩
    @BindView(R.id.tv_Hulixiuxin) TextView tv_Hulixiuxin;//护理修心
    @BindView(R.id.tv_Nvxingshuhuan) TextView tv_Nvxingshuhuan;//女性舒缓
    @BindView(R.id.tv_Wenrechushi) TextView tv_Wenrechushi;//温热初始
    @BindView(R.id.tv_Kuaisuyaotun) TextView tv_Kuaisuyaotun;//快速腰臀
    @BindView(R.id.tv_Kuaisujingjian) TextView tv_Kuaisujingjian;//快速颈肩

    //显示自动模式中的状态
    @BindView(R.id.tv_autoStatus) TextView tv_autoStatus;

    //气压部位
    @BindView(R.id.ll_qiyayaojian) LinearLayout ll_qiyayaojian;
    @BindView(R.id.ll_qiyatunbu) LinearLayout ll_qiyatunbu;
    @BindView(R.id.ll_qiyashoubu) LinearLayout ll_qiyashoubu;
    @BindView(R.id.ll_qiyatuibu) LinearLayout ll_qiyatuibu;
    @BindView(R.id.tv_manual_qiyayaojian) TextView tv_manual_qiyayaojian;
    @BindView(R.id.tv_manual_qiyatunbu) TextView tv_manual_qiyatunbu;
    @BindView(R.id.tv_manual_qiyashoubu) TextView tv_manual_qiyashoubu;
    @BindView(R.id.tv_manual_qiyatuibu) TextView tv_manual_qiyatuibu;

    //速度加减
    @BindView(R.id.iv_more_func_3d_strength_add) ImageView iv_more_func_3d_strength_add;
    @BindView(R.id.iv_more_func_3d_strength_minus) ImageView iv_more_func_3d_strength_minus;
    //宽度加减
    @BindView(R.id.iv_more_func_width_add) ImageView iv_more_func_width_add;
    @BindView(R.id.iv_more_func_width_minus) ImageView iv_more_func_width_minus;
    //气囊强度加减
    @BindView(R.id.iv_more_func_air_intensity_add) ImageView iv_more_func_air_intensity_add;
    @BindView(R.id.iv_more_func_air_intensity_minus) ImageView iv_more_func_air_intensity_minus;

    @Autowired
    public String macAddress;
    @Autowired
    public String chairName;
    @Autowired
    public String characteristicUuid;

    private ShellchairContract.Presenter mPresenter;

    private ActionSheet<DeviceAction> mPopup;
    private SlidingMenu<DeviceAction> mPopup2;

    private int TAB_CODE = 2;//手动自动标签：手动为1自动为2

    @Override
    protected int layoutResID() {
        return R.layout.activity_device_detail_shellchair;
    }

    @Override
    protected ImmersionBar initImmersionBar() {
        return ImmersionBar.with(this).statusBarColor(R.color.bg_device_detail_top).fitsSystemWindows(true);
    }

    @Override
    protected void initData() {
        mPresenter = new ShellchairPresenter();
        addPresenter(mPresenter);
        mPresenter.acceptData("chairName", chairName);
        mPresenter.acceptData("macAddress", macAddress);
        mPresenter.acceptData("characteristicUuid", UUID.fromString(characteristicUuid));
        Logger.i("ShellChairActivity", "chairName:" + chairName + ",macAddress:" + macAddress + ",characteristicUuid:" + UUID.fromString(characteristicUuid));
    }

    @Override
    protected void initViews() {
        tv_title.setText("HOME-6");
        initSupl();
        initPopup();
        initState();
        initLongClickFunc();

//        initScrollView();//监听ScrollView滑动到底部就隐藏上拉按钮防止按钮重合影响用户体验
        addGuideView();
        addUseDevice();
    }

    private void addUseDevice() {
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnUseDeviceEvent()), 500);
    }
    @Subscribe
    public void onEventReceived(OnUseDeviceEvent event) {
        mPresenter.useDevice(macAddress);
    }

    private void addGuideView() {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        NewbieGuide.with(this)
                .setLabel("guide")
                .setShowCounts(1)//控制次数
//                .alwaysShow(true)//总是显示，调试时可以打开
                .addGuidePage(GuidePage.newInstance()
//                        .addHighLight(iv_change)
//                        .addHighLight(new RectF(0, getResources().getDimension(R.dimen.guide_view_top),
//                                getResources().getDimension(R.dimen.guide_view_right),
//                                getResources().getDimension(R.dimen.guide_view_bottom)))
                        .addHighLight(new RectF(width - 160, 0, width, 150))
                        .setLayoutRes(R.layout.view_guide))
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initScrollView() {
        scrollview_shoudong.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View contentView = scrollview_shoudong.getChildAt(0);
                if (contentView != null && contentView.getMeasuredHeight()
                        == (scrollview_shoudong.getScrollY() + scrollview_shoudong.getHeight())) {
                    ivBottomArrow.setVisibility(View.GONE);
                } else {
                    ivBottomArrow.setVisibility(View.VISIBLE);
                }
            }
        });
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

    private void initPopup() {
        mPopup = new ActionSheet<>(this, mMainActionItemClickListener);
        mPopup2 = new SlidingMenu<>(this, mMainActionItemClickListener);
    }

    private void initState() {
        ivRouNie.setEnabled(false);
        ivQiaoJi.setEnabled(false);
        ivZhiYa.setEnabled(false);
        ivRouQiao.setEnabled(false);
        ivTuiNa.setEnabled(false);
        ivJiaRe.setEnabled(false);

        pb1.setMax(DEFAULT_MAX_PROGRESS);
        pb2.setMax(DEFAULT_MAX_PROGRESS);
        pb3.setMax(DEFAULT_MAX_PROGRESS);
        pb4.setMax(DEFAULT_MAX_PROGRESS);

        supl.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);
//        showLoading();
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

    boolean isPowerOn;
    boolean isAutoJiance;
    boolean isAutoTiaojie;
    boolean isAutoJianceComplete;
    /**
     * {@link ShellchairDataParser#getDeviceState(List)}
     */
    @Override
    public void displayDeviceState(List<Boolean> data) {
//        Logger.e("displayDeviceState", "机器状态-->" + data);
        isPowerOn = data.get(0);
        boolean isPause = data.get(1);

        isAutoJiance = data.get(3);//true时为自动模式肩部扫描中
        isAutoTiaojie = data.get(4);//true时为自动模式肩部位置手动调节
        isAutoJianceComplete = data.get(5);//true时为自动模式肩部扫描完成
        if (isPowerOn) {
            ivBottomPower.setImageResource(R.mipmap.ic_device_detail_power_ed);
//            rl_bottom_power.setBackgroundResource(R.drawable.bg_device_detail_open_d2);
            if (isPause) {
                ivBottomPause.setImageResource(R.mipmap.ic_device_detail_start_ed);
//                rl_bottom_pause.setBackgroundResource(R.drawable.bg_device_detail_pause_d2);
            } else {
                ivBottomPause.setImageResource(R.mipmap.ic_device_detail_pause_ed);
//                rl_bottom_pause.setBackgroundResource(R.drawable.bg_device_detail_pause_d2);
            }

//            //状态是手动按摩
//            if (!isAuto) {
//                if (isShoufa || isBodypart) {
//                    tv_autoStatus.setText("当前模式：手动");
//                    //自动标签下的所有图片背景置灰
////                    setAutoImagebgClose();
//                } else {
//                    tv_autoStatus.setText("当前模式：自动");
//                }
//            }
        } else {
//            tv_autoStatus.setText("");
//            Toaster.showShort("未开机");
            //未开机状态
            ivBottomPower.setImageResource(R.mipmap.ic_device_detail_power_u);
            ivBottomPause.setImageResource(R.mipmap.ic_device_detail_start_u);
//            rl_bottom_power.setBackgroundResource(R.drawable.bg_device_detail_open_u2);
//            rl_bottom_pause.setBackgroundResource(R.drawable.bg_device_detail_pause_u2);

            //手动标签下的所有图片背景置灰
//            setManualImagebgClose();
            //自动标签下的所有图片背景置灰
            setAutoImagebgClose();
        }
    }

    private void setManualImagebgClose() {
        /**
         * 手法切换颜色还原
         */
        ll_rounie.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        ll_qiaoji.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        ll_zhiya.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        ll_tuina.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        //图片变换
        iv_manual_rounie.setImageResource(R.mipmap.ic_manual_rounie_u);
        iv_manual_qiaoji.setImageResource(R.mipmap.ic_manual_qiaoji_u);
        iv_manual_zhiya.setImageResource(R.mipmap.ic_manual_zhiya_u);
        iv_manual_tuina.setImageResource(R.mipmap.ic_manual_tuina_u);
        //字体变色
        tv_manual_rounie.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_qiaoji.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_zhiya.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_tuina.setTextColor(getResources().getColor(R.color.bg_device_detail_top));

        /**
         * 部位切换颜色还原
         */
        ll_quyuquanshen.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        ll_qujianbeibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        ll_qujianyaobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        ll_qujianzuobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        ll_qujianbeiyao.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        ll_qujianyaozuo.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        tv_manual_quyuquanshen.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_qujianbeibu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_qujianyaobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_qujianzuobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_qujianbeiyao.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_qujianyaozuo.setTextColor(getResources().getColor(R.color.bg_device_detail_top));

        /**
         * 气囊部位颜色还原
         */
        ll_qiyayaojian.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        ll_qiyatunbu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        ll_qiyashoubu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        ll_qiyatuibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        tv_manual_qiyayaojian.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_qiyatunbu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_qiyashoubu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_qiyatuibu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
    }

    private void setAutoImagebgClose() {
        rl_Quanshenfangsong.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Shuyatiaojie.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Huolihuanxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Qianyinlashen.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Qingduxiuxi.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Zhijianhuanxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Jingjiananmo.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Hulixiuxin.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Nvxingshuhuan.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Wenrechushi.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Kuaisujingjian.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Kuaisuyaotun.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);

        iv_Quanshenfangsong.setImageResource(R.mipmap.ic_shellauto_01_u);
        iv_Shuyatiaojie.setImageResource(R.mipmap.ic_shellauto_02_u);
        iv_Huolihuanxing.setImageResource(R.mipmap.ic_shellauto_03_u);
        iv_Qianyinlashen.setImageResource(R.mipmap.ic_shellauto_04_u);
        iv_Qingduxiuxi.setImageResource(R.mipmap.ic_shellauto_05_u);
        iv_Zhijianhuanxing.setImageResource(R.mipmap.ic_shellauto_06_u);
        iv_Jingjiananmo.setImageResource(R.mipmap.ic_shellauto_07_u);
        iv_Hulixiuxin.setImageResource(R.mipmap.ic_shellauto_08_u);
        iv_Nvxingshuhuan.setImageResource(R.mipmap.ic_shellauto_09_u);
        iv_Wenrechushi.setImageResource(R.mipmap.ic_shellauto_10_u);
        iv_Kuaisujingjian.setImageResource(R.mipmap.ic_shellauto_12_u);
        iv_Kuaisuyaotun.setImageResource(R.mipmap.ic_shellauto_11_u);

        tv_Quanshenfangsong.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Shuyatiaojie.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Huolihuanxing.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Qianyinlashen.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Qingduxiuxi.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Zhijianhuanxing.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Jingjiananmo.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Hulixiuxin.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Nvxingshuhuan.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Wenrechushi.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Kuaisujingjian.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Kuaisuyaotun.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
    }

    /**
     * {@link ShellchairDataParser#getTime(List)}
     */
    @Override
    public void displayTime(String time) {
        ViewHelper.setNoneNullText(tvTimer, time);
        ViewHelper.setNoneNullText(tv_more_func_timer, time);
    }

    private boolean isReadManual = true;
    @Override
    public void displayAutoMode(int data3, int data4) {
//        Logger.e("*******", "data3-->" + data3 + ",data4-->" + data4);
        if (data3 == -1 && data4 == -1) {
            isReadManual = true;
        } else {
            isReadManual = false;
        }
        if (data3 == 0) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：全身放松");
            }
            setAutoImagebgClose();
            rl_Quanshenfangsong.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Quanshenfangsong.setImageResource(R.mipmap.ic_shellauto_01_ed);
            tv_Quanshenfangsong.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 1) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：舒压调节");
            }
            setAutoImagebgClose();
            rl_Shuyatiaojie.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Shuyatiaojie.setImageResource(R.mipmap.ic_shellauto_02_ed);
            tv_Shuyatiaojie.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 2) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：活力唤醒");
            }
            setAutoImagebgClose();
            rl_Huolihuanxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Huolihuanxing.setImageResource(R.mipmap.ic_shellauto_03_ed);
            tv_Huolihuanxing.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 3) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：牵引拉伸");
            }
            setAutoImagebgClose();
            rl_Qianyinlashen.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Qianyinlashen.setImageResource(R.mipmap.ic_shellauto_04_ed);
            tv_Qianyinlashen.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 4) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：轻度休息");
            }
            setAutoImagebgClose();
            rl_Qingduxiuxi.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Qingduxiuxi.setImageResource(R.mipmap.ic_shellauto_05_ed);
            tv_Qingduxiuxi.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 5) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：指尖唤醒");
            }
            setAutoImagebgClose();
            rl_Zhijianhuanxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Zhijianhuanxing.setImageResource(R.mipmap.ic_shellauto_06_ed);
            tv_Zhijianhuanxing.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 6) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：颈肩按摩");
            }
            setAutoImagebgClose();
            rl_Jingjiananmo.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Jingjiananmo.setImageResource(R.mipmap.ic_shellauto_07_ed);
            tv_Jingjiananmo.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 7) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：护理修心");
            }
            setAutoImagebgClose();
            rl_Hulixiuxin.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Hulixiuxin.setImageResource(R.mipmap.ic_shellauto_08_ed);
            tv_Hulixiuxin.setTextColor(getResources().getColor(R.color.white));
        } else if (data4 == 0) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：女性舒缓");
            }
            setAutoImagebgClose();
            rl_Nvxingshuhuan.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Nvxingshuhuan.setImageResource(R.mipmap.ic_shellauto_09_ed);
            tv_Nvxingshuhuan.setTextColor(getResources().getColor(R.color.white));
        } else if (data4 == 1) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：温热初始");
            }
            setAutoImagebgClose();
            rl_Wenrechushi.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Wenrechushi.setImageResource(R.mipmap.ic_shellauto_10_ed);
            tv_Wenrechushi.setTextColor(getResources().getColor(R.color.white));
        } else if (data4 == 2) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：快速颈肩");
            }
            setAutoImagebgClose();
            rl_Kuaisujingjian.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Kuaisujingjian.setImageResource(R.mipmap.ic_shellauto_12_ed);
            tv_Kuaisujingjian.setTextColor(getResources().getColor(R.color.white));
        } else if (data4 == 3) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：快速腰臀");
            }
            setAutoImagebgClose();
            rl_Kuaisuyaotun.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Kuaisuyaotun.setImageResource(R.mipmap.ic_shellauto_11_ed);
            tv_Kuaisuyaotun.setTextColor(getResources().getColor(R.color.white));
        } else {
            setAutoImagebgClose();
            if (isPowerOn) {
                if (TAB_CODE == 2) {//自动页面中
                    tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
                    tv_autoStatus.setText("模式");
                } else {
                    tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
                    tv_autoStatus.setText("控制");
                }
            } else {
                if (TAB_CODE == 2) {//自动页面中
                    tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
                    tv_autoStatus.setText("模式：未启动");
                } else {
                    tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
                    tv_autoStatus.setText("模式：未启动");
                }
            }
        }

    }

    /**
     * {@link ShellchairDataParser#getTechniqueMode(List)}
     */
    @Override
    public void displayTechniqueMode(int data) {
//        Logger.e("手法-->", data + "");
        ivRouNie.setEnabled(data == 0 || data == 5);
        ivQiaoJi.setEnabled(data == 1 || data == 5);
        ivRouQiao.setEnabled(data == 2);
        ivZhiYa.setEnabled(data == 3);
        ivTuiNa.setEnabled(data == 4);

        if (isReadManual) {
            if (data == 0) {
                tv_autoStatus.setText("自定义：揉捏");
            } else if (data == 1) {
                tv_autoStatus.setText("自定义：敲打");
            } else if (data == 2 || data == 5) {
                tv_autoStatus.setText("自定义：揉敲");
            } else if (data == 3) {
                tv_autoStatus.setText("自定义：指压");
            } else if (data == 4) {
                tv_autoStatus.setText("自定义：推拿");
            }
        }

    }

    /**
     * 以前的3D力度方法改为气囊位置
     * {@link ShellchairDataParser#getAirScrPlace(List)}
     */
    @Override
    public void display3DStrength(List<Integer> data) {
        /* 气囊 */
        ivQiNangFoot.setVisibility(data.get(0) == HAS_DATA ? View.VISIBLE : View.GONE);
        ivQiNangLeg.setVisibility(data.get(0) == HAS_DATA ? View.VISIBLE : View.GONE);
        ivQiNangBack.setVisibility(NO_DATA == HAS_DATA ? View.VISIBLE : View.GONE);
        ivQiNangAss.setVisibility(data.get(1) == HAS_DATA ? View.VISIBLE : View.GONE);
        ivQiNangHand.setVisibility(data.get(2) == HAS_DATA ? View.VISIBLE : View.GONE);
        ivQiNangArm.setVisibility(data.get(3) == HAS_DATA ? View.VISIBLE : View.GONE);
    }

    int zeroCode = -1;
    /**
     * {@link ShellchairDataParser#getZeroGravityLevel(List)}}
     */
    @Override
    public void displayZeroGravity(int data) {
        zeroCode = data;
//        Logger.e("displayZeroGravity", "零重力code-->" + data);
        int imgRes;
        if (data == NO_DATA) {
            imgRes = R.mipmap.ic_device_detail_technique_new_7_off;
        } else {
            imgRes = ResourceUtils.getDrawableIDByName("ic_device_detail_technique_new_7_on_" + (data + 1));
        }
        ivZeroGravity.setImageResource(imgRes);
    }

    /**
     * 背滚位置读取
     * {@link ShellchairDataParser#getBeiGun$AirScrPlace(List)}
     */
    @Override
    public void display$AirScrPlace(List<Boolean> data) {
//        Logger.e("背滚***", data.get(0) + "");
        /* 背滚 */
        if (ObjectUtils.isEmpty(data)) {
            return;
        }

//        Integer beiGun = data.get(0);
//        if (beiGun == NO_DATA) {
//            ivBeiGun.setVisibility(View.GONE);
//        } else {
//            ivBeiGun.setVisibility(View.VISIBLE);
//            int imRes = ResourceUtils.getDrawableIDByName("ic_bei_gun_shell_" + (beiGun + 1));
//            ivBeiGun.setImageResource(imRes);
//        }

        iv_bei_gun01.setVisibility(data.get(0) ? View.VISIBLE : View.GONE);
        iv_bei_gun02.setVisibility(data.get(1) ? View.VISIBLE : View.GONE);
        iv_bei_gun03.setVisibility(data.get(2) ? View.VISIBLE : View.GONE);
        iv_bei_gun04.setVisibility(data.get(3) ? View.VISIBLE : View.GONE);
        iv_bei_gun05.setVisibility(data.get(4) ? View.VISIBLE : View.GONE);
        iv_bei_gun06.setVisibility(data.get(5) ? View.VISIBLE : View.GONE);
        iv_bei_gun07.setVisibility(data.get(6) ? View.VISIBLE : View.GONE);
        iv_bei_gun08.setVisibility(data.get(7) ? View.VISIBLE : View.GONE);
        iv_bei_gun09.setVisibility(data.get(8) ? View.VISIBLE : View.GONE);
        iv_bei_gun10.setVisibility(data.get(9) ? View.VISIBLE : View.GONE);
        iv_bei_gun11.setVisibility(data.get(10) ? View.VISIBLE : View.GONE);
        iv_bei_gun12.setVisibility(data.get(11) ? View.VISIBLE : View.GONE);
        iv_bei_gun13.setVisibility(data.get(12) ? View.VISIBLE : View.GONE);
        iv_bei_gun14.setVisibility(data.get(13) ? View.VISIBLE : View.GONE);
        iv_bei_gun15.setVisibility(data.get(14) ? View.VISIBLE : View.GONE);
    }

    /**
     * 加热腿滚脚滚
     * {@link ShellchairDataParser#getAssistFunctions(List)}
     */
    @Override
    public void displayAssistFunctions(List<Boolean> data) {
//        Logger.e("***", "" + data);
        if (ObjectUtils.isEmpty(data)) {
            return;
        }

        ivJiaRe.setEnabled(data.get(1));
        ivJiaoGun.setVisibility(data.get(2) ? View.VISIBLE : View.GONE);
        ivTuiGun.setVisibility(data.get(3) ? View.VISIBLE : View.GONE);
    }

    /**
     * 速度 和 宽度
     * {@link ShellchairDataParser#getGearLevel1(List)}
     */
    @Override
    public void displayGearLevel1(List<Integer> data) {
        if (ObjectUtils.isEmpty(data)) {
            return;
        }
//        Logger.e("speed + width--", data.get(0) + "," + data.get(1));
        if (data.get(0) == NO_DATA) {
            tvMF3DStrength.setText(DEVICE_DETAIL_NO_DATA);
            iv_speed.setImageResource(R.mipmap.ic_level_0);
        } else {
            int level = data.get(0) + 1;
            if (level == 1 || level == 2) {
                tvMF3DStrength.setText("1档");
                iv_speed.setImageResource(R.mipmap.ic_level_1);
            } else if (level == 3 || level == 4) {
                tvMF3DStrength.setText("2档");
                iv_speed.setImageResource(R.mipmap.ic_level_2);
            } else if (level == 5) {
                tvMF3DStrength.setText("3档");
                iv_speed.setImageResource(R.mipmap.ic_level_3);
            }
        }
        if (data.get(1) == NO_DATA) {
            tv_more_func_width.setText(DEVICE_DETAIL_NO_DATA);
            iv_width.setImageResource(R.mipmap.ic_level_0);
        } else {
            if (data.get(1) > 4) {
                int level = data.get(1) - 4;
                tv_more_func_width.setText(level + "档");

                int imgRes = ResourceUtils.getDrawableIDByName("ic_level_" + level);
                iv_width.setImageResource(imgRes);
            } else {
                tv_more_func_width.setText(DEVICE_DETAIL_NO_DATA);
                iv_width.setImageResource(R.mipmap.ic_level_0);
            }
        }
//        DisplayUtils.showDeviceProgress(data.get(0), 5, pb1);
//        DisplayUtils.showDeviceProgress(data.get(1), 3, pb3);
    }

    /**
     * {@link ShellchairDataParser#getGearLevel3(List)}
     */
    @Override
    public void displayGearLevel3(int data) {
        DisplayUtils.showDeviceProgress(data, 3, pb4);
    }

    @Override
    public void onConnectSuccess() {
        if (supl.getVisibility() != View.VISIBLE) {
            Logger.d("onConnectSuccess", "supl不显示-->if");
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
     * 气囊强度和零重力（这里读取气囊强度)
     * {@link ShellchairDataParser#getGearLevel2(List)}
     */
    @Override
    public void displayGearLevel2(List<Boolean> data) {
//        Logger.e("气囊强度-->", "" + data);
        if (ObjectUtils.isEmpty(data)) {
            tvMFAirIntensity.setText(DEVICE_DETAIL_NO_DATA);
            iv_air.setImageResource(R.mipmap.ic_level_0);
        } else {
            if (data.get(0) || data.get(1)) {
                tvMFAirIntensity.setText("1档");
                iv_air.setImageResource(R.mipmap.ic_level_1);
            } else if (data.get(2) || data.get(3)) {
                tvMFAirIntensity.setText("2档");
                iv_air.setImageResource(R.mipmap.ic_level_2);
            } else if (data.get(4)) {
                tvMFAirIntensity.setText("3档");
                iv_air.setImageResource(R.mipmap.ic_level_3);
            } else if (!data.get(0) && !data.get(1) && !data.get(2) && !data.get(3) && !data.get(4)) {
                tvMFAirIntensity.setText(DEVICE_DETAIL_NO_DATA);
                iv_air.setImageResource(R.mipmap.ic_level_0);
            }
        }
//        if (data == NO_DATA) {
//            tvMFAirIntensity.setText(DEVICE_DETAIL_NO_DATA);
//            iv_air.setImageResource(R.mipmap.ic_level_0);
//        } else {
//            int level = data + 1;
//            if (level == 1) {
//                tvMFAirIntensity.setText((data + 1) + "档");
//                iv_air.setImageResource(R.mipmap.ic_level_1);
//            } else if (level == 3) {
//                tvMFAirIntensity.setText("2档");
//                iv_air.setImageResource(R.mipmap.ic_level_2);
//            } else if (level == 5) {
//                tvMFAirIntensity.setText("3档");
//                iv_air.setImageResource(R.mipmap.ic_level_3);
//            }
//        }
//        DisplayUtils.showDeviceProgress(data, AIR_INTENSITY_MAX_LEVEL, pb2);
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

//    老代码“手法选择”，“部位切换”，“自动模式”4个模块的点击时间依次如下
//    @OnClick(R.id.ll_main_function_technique)
//    public void mainFuncTechnique() {
//        showPopup(ManualActionData.getTechniqueModeList());
//    }
//
//    @OnClick(R.id.ll_main_function_body_part)
//    public void mainFuncBodyPart() {
//        showPopup(ManualActionData.getBodyPartList());
//    }
//
//    @OnClick(R.id.ll_main_function_auto_mode)
//    public void mainFuncAutoMode() {
//        showPopup(AutoActionData.get());
//    }

    //零重力
    @OnClick(R.id.ll_main_function_zero_gravity)
    public void mainFuncZeroGravity() {
        mPresenter.zeroGravity(zeroCode);
    }

    @OnClick(R.id.iv_change)
    public void changeMode() {
        if (TAB_CODE == 2) {
            //如果当前是自动模式切换为手动
            TAB_CODE = 1;
            startRotateAnimate(iv_change);
            tv_change.setText("控制");
//            setShowAnimation(tv_change, 100);
            ll_shoudong.setVisibility(View.VISIBLE);
            ll_zidong.setVisibility(View.GONE);
//            startAnimate(ll_shoudong);
//            setHideAnimation(ll_zidong, 500);
//            setShowAnimation(ll_shoudong, 1000);
        } else if (TAB_CODE == 1) {
            //如果当前是手动模式切换为自动
            TAB_CODE = 2;
            startRotateAnimate(iv_change);
            tv_change.setText("模式");
//            setShowAnimation(tv_change, 100);
            ll_shoudong.setVisibility(View.GONE);
            ll_zidong.setVisibility(View.VISIBLE);
//            startAnimate(ll_zidong);
//            setHideAnimation(ll_shoudong, 500);
//            setShowAnimation(ll_zidong, 1000);
        }
    }

//    //手动和自动标签的切换
//    @OnClick({R.id.tv_shoudong, R.id.tv_zidong})
//    public void changeLabel(View view) {
//        switch (view.getId()) {
//            case R.id.tv_shoudong:
//                Logger.i("T100DeviceDetailActivity", "手动模块");
//                TAB_CODE = 1;
//                ll_shoudong.setVisibility(View.VISIBLE);
//                ll_zidong.setVisibility(View.GONE);
//                tv_shoudong.setTextColor(getResources().getColor(R.color.yellow));
//                tv_zidong.setTextColor(getResources().getColor(R.color.white));
//                tv_shoudong.setBackground(getResources().getDrawable(R.mipmap.bg_tab_ed));
//                tv_zidong.setBackground(null);
//                break;
//            case R.id.tv_zidong:
//                Logger.i("T100DeviceDetailActivity", "自动模块");
//                TAB_CODE = 2;
//                ll_shoudong.setVisibility(View.GONE);
//                ll_zidong.setVisibility(View.VISIBLE);
//                tv_shoudong.setTextColor(getResources().getColor(R.color.white));
//                tv_zidong.setTextColor(getResources().getColor(R.color.yellow));
//                tv_shoudong.setBackground(null);
//                tv_zidong.setBackground(getResources().getDrawable(R.mipmap.bg_tab_ed));
//                break;
//        }
//    }

    //手动模块4个手法功能
    @OnClick({R.id.ll_rounie, R.id.ll_qiaoji, R.id.ll_rouqiao, R.id.ll_zhiya, R.id.ll_tuina})
    public void manualFunction(View view) {
        switch (view.getId()) {
            case R.id.ll_rounie:
                //手法-->揉捏
                getBLEPresenter().writeData(ShellManualActionData.getTechniqueModeList().get(0));
                Logger.i("T100DeviceDetailActivity", "手动模块-->揉捏");
                break;
            case R.id.ll_qiaoji:
                //手法-->敲击
                getBLEPresenter().writeData(ShellManualActionData.getTechniqueModeList().get(1));
                Logger.i("T100DeviceDetailActivity", "手动模块-->敲击");
                break;
            case R.id.ll_rouqiao:
                //手法-->揉敲
                getBLEPresenter().writeData(ShellManualActionData.getTechniqueModeList().get(2));
                break;
            case R.id.ll_zhiya:
                //手法-->指压
                getBLEPresenter().writeData(ShellManualActionData.getTechniqueModeList().get(3));
                Logger.i("T100DeviceDetailActivity", "手动模块-->指压");
                break;
            case R.id.ll_tuina:
                //手法-->推拿
                getBLEPresenter().writeData(ShellManualActionData.getTechniqueModeList().get(4));
                Logger.i("T100DeviceDetailActivity", "手动模块-->推拿");
                break;
        }
    }

    //部位切换6个
    @OnClick({R.id.ll_quyuquanshen, R.id.ll_qujianbeibu, R.id.ll_qujianyaobu, R.id.ll_qujianzuobu, R.id.ll_qujianbeiyao, R.id.ll_qujianyaozuo})
    public void manualBodypartFunction(View view) {
        switch (view.getId()) {
            case R.id.ll_quyuquanshen:
                //部位切换-->区域全身
                getBLEPresenter().writeData(ShellManualActionData.getBodyPartList().get(0));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区域全身");
                break;
            case R.id.ll_qujianbeibu:
                //部位切换-->区间背部
                getBLEPresenter().writeData(ShellManualActionData.getBodyPartList().get(1));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区间背部");
                break;
            case R.id.ll_qujianyaobu:
                //部位切换-->区间腰部
                getBLEPresenter().writeData(ShellManualActionData.getBodyPartList().get(2));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区间腰部");
                break;
            case R.id.ll_qujianzuobu:
                //部位切换-->区间座部
                getBLEPresenter().writeData(ShellManualActionData.getBodyPartList().get(3));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区间座部");
                break;
            case R.id.ll_qujianbeiyao:
                //部位切换-->区间背腰
                getBLEPresenter().writeData(ShellManualActionData.getBodyPartList().get(4));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区间背腰");
                break;
            case R.id.ll_qujianyaozuo:
                //部位切换-->区间腰座
                getBLEPresenter().writeData(ShellManualActionData.getBodyPartList().get(5));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区间腰座");
                break;
        }
    }

    //手动模块4个气压部位功能
    @OnClick({R.id.ll_qiyayaojian, R.id.ll_qiyatunbu, R.id.ll_qiyashoubu, R.id.ll_qiyatuibu})
    public void manualPressurePartFunction(View view) {
        switch (view.getId()) {
            case R.id.ll_qiyayaojian:
                //手动-->气压腰肩
                getBLEPresenter().writeData(ShellManualActionData.getPressurePartList().get(0));
                Logger.i("T100DeviceDetailActivity", "手动模块-->气压肩部");
                break;
            case R.id.ll_qiyatunbu:
                //手动-->气压臀部
                getBLEPresenter().writeData(ShellManualActionData.getPressurePartList().get(1));
//                mPresenter.toggleQiyatunbu();
                Logger.i("T100DeviceDetailActivity", "手动模块-->气压臀部");
                break;
            case R.id.ll_qiyashoubu:
                //手动-->气压手部
                getBLEPresenter().writeData(ShellManualActionData.getPressurePartList().get(2));
//                mPresenter.toggleQiyashoubu();
                Logger.i("T100DeviceDetailActivity", "手动模块-->气压手部");
                break;
            case R.id.ll_qiyatuibu:
                //手动-->气压腿部
                getBLEPresenter().writeData(ShellManualActionData.getPressurePartList().get(3));
//                mPresenter.toggleQiyatuibu();
                Logger.i("T100DeviceDetailActivity", "手动模块-->气压腿部");
                break;
        }
    }

    //自动模块功能
    @OnClick({R.id.rl_Quanshenfangsong, R.id.rl_Shuyatiaojie, R.id.rl_Huolihuanxing,
            R.id.rl_Qianyinlashen, R.id.rl_Qingduxiuxi, R.id.rl_Zhijianhuanxing,
            R.id.rl_Jingjiananmo, R.id.rl_Hulixiuxin, R.id.rl_Nvxingshuhuan,
            R.id.rl_Wenrechushi, R.id.rl_Kuaisuyaotun, R.id.rl_Kuaisujingjian})
    public void autoFunction(View view) {
        switch (view.getId()) {
            case R.id.rl_Quanshenfangsong:
                //全身放松
                getBLEPresenter().writeData(ShellAutoActionData.get().get(0));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：全身放松");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Quanshenfangsong.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Quanshenfangsong.setImageResource(R.mipmap.ic_shellauto_01_ed);
                    tv_Quanshenfangsong.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Shuyatiaojie:
                //舒压调节
                getBLEPresenter().writeData(ShellAutoActionData.get().get(1));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：舒压调节");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Shuyatiaojie.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Shuyatiaojie.setImageResource(R.mipmap.ic_shellauto_02_ed);
                    tv_Shuyatiaojie.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Huolihuanxing:
                //活力唤醒
                getBLEPresenter().writeData(ShellAutoActionData.get().get(2));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：活力唤醒");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Huolihuanxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Huolihuanxing.setImageResource(R.mipmap.ic_shellauto_03_ed);
                    tv_Huolihuanxing.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Qianyinlashen:
                //牵引拉伸
                getBLEPresenter().writeData(ShellAutoActionData.get().get(3));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：牵引拉伸");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Qianyinlashen.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Qianyinlashen.setImageResource(R.mipmap.ic_shellauto_04_ed);
                    tv_Qianyinlashen.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Qingduxiuxi:
                //轻度休息
                getBLEPresenter().writeData(ShellAutoActionData.get().get(4));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：轻度休息");
                    startShakeByView(tv_autoStatus, 1f, 1.2f, 1, 1200);
                } else {
                    setAutoImagebgClose();
                    rl_Qingduxiuxi.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Qingduxiuxi.setImageResource(R.mipmap.ic_shellauto_05_ed);
                    tv_Qingduxiuxi.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Zhijianhuanxing:
                //指尖唤醒
                getBLEPresenter().writeData(ShellAutoActionData.get().get(5));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：指尖唤醒");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Zhijianhuanxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Zhijianhuanxing.setImageResource(R.mipmap.ic_shellauto_06_ed);
                    tv_Zhijianhuanxing.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Jingjiananmo:
                //颈肩按摩
                getBLEPresenter().writeData(ShellAutoActionData.get().get(6));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：颈肩按摩");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Jingjiananmo.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Jingjiananmo.setImageResource(R.mipmap.ic_shellauto_07_ed);
                    tv_Jingjiananmo.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Hulixiuxin:
                //护理修心
                getBLEPresenter().writeData(ShellAutoActionData.get().get(7));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：护理修心");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Hulixiuxin.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Hulixiuxin.setImageResource(R.mipmap.ic_shellauto_08_ed);
                    tv_Hulixiuxin.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Nvxingshuhuan:
                //女性舒缓
                getBLEPresenter().writeData(ShellAutoActionData.get().get(8));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：女性舒缓");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Nvxingshuhuan.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Nvxingshuhuan.setImageResource(R.mipmap.ic_shellauto_09_ed);
                    tv_Nvxingshuhuan.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Wenrechushi:
                //温热初始
                getBLEPresenter().writeData(ShellAutoActionData.get().get(9));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：温热初始");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Wenrechushi.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Wenrechushi.setImageResource(R.mipmap.ic_shellauto_10_ed);
                    tv_Wenrechushi.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Kuaisujingjian:
                //快速颈肩
                getBLEPresenter().writeData(ShellAutoActionData.get().get(10));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：快速颈肩");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Kuaisujingjian.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Kuaisujingjian.setImageResource(R.mipmap.ic_shellauto_12_ed);
                    tv_Kuaisujingjian.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Kuaisuyaotun:
                //快速腰臀
                getBLEPresenter().writeData(ShellAutoActionData.get().get(11));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：快速腰臀");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Kuaisuyaotun.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Kuaisuyaotun.setImageResource(R.mipmap.ic_shellauto_11_ed);
                    tv_Kuaisuyaotun.setTextColor(getResources().getColor(R.color.white));
                }
                break;
        }
    }

    private void startShakeByView(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        //由小变大
        /**
         * 参数说明
         * fromX：起始X坐标上的伸缩尺寸。
         * toX：结束X坐标上的伸缩尺寸。
         * fromY：起始Y坐标上的伸缩尺寸。
         * toY：结束Y坐标上的伸缩尺寸。
         */
        Animation scaleAnim = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        //从左向右
        Animation rotateAnim = new RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim.setDuration(duration);
        rotateAnim.setDuration(duration / 10);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setRepeatCount(10);
//        rotateAnim.setRepeatCount(Animation.INFINITE);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);

        view.startAnimation(smallAnimationSet);
    }
    private void startShakeByProperty(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
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

    @OnClick(R.id.rl_bottom_power)
    public void bottomPower() {
        mPresenter.togglePower();
    }

    @OnClick(R.id.rl_bottom_pause)
    public void bottomPause() {
        mPresenter.togglePause();
    }

    @OnClick(R.id.rl_more_func_position)
    public void moreFuncPosition() {
        mPresenter.togglePosition();
    }

    @OnClick(R.id.iv_more_func_timer)
    public void moreFuncTimer() {
        mPresenter.toggleTimer();
    }

    /**
     * 气压调节
     * @param view
     */
    @OnClick({R.id.iv_more_func_air_intensity_add, R.id.iv_more_func_air_intensity_minus})
    public void moreFuncAirIntensity(View view) {
        switch (view.getId()) {
            case R.id.iv_more_func_air_intensity_add:
                mPresenter.changeAirIntensity(true);

                YoYo.with(Techniques.Tada)
                        .duration(1000)
                        .repeat(1)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(iv_more_func_air_intensity_add);
                break;
            case R.id.iv_more_func_air_intensity_minus:
                mPresenter.changeAirIntensity(false);

                YoYo.with(Techniques.Tada)
                        .duration(1000)
                        .repeat(1)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(iv_more_func_air_intensity_minus);
                break;
        }
    }

    /**
     * 速度调节
     * @param view
     */
    @OnClick({R.id.iv_more_func_3d_strength_add, R.id.iv_more_func_3d_strength_minus})
    public void moreFunc3DStrength(View view) {
        switch (view.getId()) {
            case R.id.iv_more_func_3d_strength_add:
                mPresenter.change3DStrength(true);

                YoYo.with(Techniques.Tada)
                        .duration(1000)
                        .repeat(1)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(iv_more_func_3d_strength_add);
                break;
            case R.id.iv_more_func_3d_strength_minus:
                mPresenter.change3DStrength(false);

                YoYo.with(Techniques.Tada)
                        .duration(1000)
                        .repeat(1)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(iv_more_func_3d_strength_minus);
                break;
        }
    }

    /**
     * 宽度调节
     * @param view
     */
    @OnClick({R.id.iv_more_func_width_add, R.id.iv_more_func_width_minus})
    public void moreFuncWidth(View view) {
        switch (view.getId()) {
            case R.id.iv_more_func_width_add:
                mPresenter.changeWidth(true);

                YoYo.with(Techniques.Tada)
                        .duration(1000)
                        .repeat(1)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(iv_more_func_width_add);
                break;
            case R.id.iv_more_func_width_minus:
                mPresenter.changeWidth(false);

                YoYo.with(Techniques.Tada)
                        .duration(1000)
                        .repeat(1)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(iv_more_func_width_minus);
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

    @OnClick(R.id.rl_back)
    public void onBack() {
        onBackPressed();
    }

    /**
     * 设置缩放动画
     * @param view
     */
    private void startAnimate(View view) {
        ScaleAnimation animation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(600);
        view.setAnimation(animation);
    }

    /**
     * View渐隐动画效果
     */
    private void setHideAnimation(View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
//        if (null != mHideAnimation) {
//            mHideAnimation.cancel();
//        }
        // 监听动画结束的操作
        AlphaAnimation mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        view.startAnimation(mHideAnimation);
    }
    /**
     * View渐现动画效果
     */
    private void setShowAnimation(View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
//        if (null != mShowAnimation) {
//            mShowAnimation.cancel();
//        }
        AlphaAnimation mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        view.startAnimation(mShowAnimation);
    }

    /**
     * 设置旋转动画
     * @param view
     */
    private void startRotateAnimate(View view) {
        Animation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(400);
//        animation.setRepeatCount(1);//动画的反复次数
//        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        view.startAnimation(animation);//開始动画
    }
}
