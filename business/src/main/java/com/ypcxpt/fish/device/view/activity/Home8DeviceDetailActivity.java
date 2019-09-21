package com.ypcxpt.fish.device.view.activity;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;
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
import com.ypcxpt.fish.core.ble.input.DeviceDataParser;
import com.ypcxpt.fish.core.ble.output.data.AutoActionData;
import com.ypcxpt.fish.core.ble.output.data.ManualActionData;
import com.ypcxpt.fish.core.model.DeviceAction;
import com.ypcxpt.fish.device.contract.DeviceDetailContract;
import com.ypcxpt.fish.device.event.OnUseDeviceEvent;
import com.ypcxpt.fish.device.presenter.BaseBLEPresenter;
import com.ypcxpt.fish.device.presenter.DeviceDetailPresenter;
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
import static com.ypcxpt.fish.core.ble.output.data.DeviceConstant.AIR_INTENSITY_MAX_LEVEL;

@Route(path = Path.Device.DETAIL)
public class Home8DeviceDetailActivity extends BaseActivity implements DeviceDetailContract.View {
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

    @BindView(R.id.iv_air) ImageView iv_air;
    @BindView(R.id.iv_speed) ImageView iv_speed;

    @BindView(R.id.tv_fudu_low) TextView tv_fudu_low;
    @BindView(R.id.tv_fudu_mid) TextView tv_fudu_mid;
    @BindView(R.id.tv_fudu_high) TextView tv_fudu_high;

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
    @BindView(R.id.ll_rouqiao) LinearLayout ll_rouqiao;//揉敲
    @BindView(R.id.iv_manual_rouqiao) ImageView iv_manual_rouqiao;
    @BindView(R.id.tv_manual_rouqiao) TextView tv_manual_rouqiao;

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
    @BindView(R.id.rl_Huolihuifu) RelativeLayout rl_Huolihuifu;//活力恢复
    @BindView(R.id.rl_Jianjinghuli) RelativeLayout rl_Jianjinghuli;//肩颈护理
    @BindView(R.id.rl_Yaobushuhuan) RelativeLayout rl_Yaobushuhuan;//腰部舒缓
    @BindView(R.id.rl_Tunbusuxing) RelativeLayout rl_Tunbusuxing;//臀部塑形
    @BindView(R.id.rl_Qianyinlashen) RelativeLayout rl_Qianyinlashen;//牵引拉伸
    @BindView(R.id.rl_Zhinenghehu) RelativeLayout rl_Zhinenghehu;//智能呵护
    @BindView(R.id.rl_Zhijianhuanxing) RelativeLayout rl_Zhijianhuanxing;//指尖唤醒
    @BindView(R.id.rl_Shangwuxiuxian) RelativeLayout rl_Shangwuxiuxian;//商务休闲
    @BindView(R.id.rl_Zhumianxiuyang) RelativeLayout rl_Zhumianxiuyang;//助眠修养
    @BindView(R.id.rl_Huanjiefangsong) RelativeLayout rl_Huanjiefangsong;//缓解放松
    @BindView(R.id.iv_Huolihuifu) ImageView iv_Huolihuifu;//活力恢复
    @BindView(R.id.iv_Jianjinghuli) ImageView iv_Jianjinghuli;//肩颈护理
    @BindView(R.id.iv_Yaobushuhuan) ImageView iv_Yaobushuhuan;//腰部舒缓
    @BindView(R.id.iv_Tunbusuxing) ImageView iv_Tunbusuxing;//臀部塑形
    @BindView(R.id.iv_Qianyinlashen) ImageView iv_Qianyinlashen;//牵引拉伸
    @BindView(R.id.iv_Zhinenghehu) ImageView iv_Zhinenghehu;//智能呵护
    @BindView(R.id.iv_Zhijianhuanxing) ImageView iv_Zhijianhuanxing;//指尖唤醒
    @BindView(R.id.iv_Shangwuxiuxian) ImageView iv_Shangwuxiuxian;//商务休闲
    @BindView(R.id.iv_Zhumianxiuyang) ImageView iv_Zhumianxiuyang;//助眠修养
    @BindView(R.id.iv_Huanjiefangsong) ImageView iv_Huanjiefangsong;//缓解放松
    @BindView(R.id.tv_Huolihuifu) TextView tv_Huolihuifu;//活力恢复
    @BindView(R.id.tv_Jianjinghuli) TextView tv_Jianjinghuli;//肩颈护理
    @BindView(R.id.tv_Yaobushuhuan) TextView tv_Yaobushuhuan;//腰部舒缓
    @BindView(R.id.tv_Tunbusuxing) TextView tv_Tunbusuxing;//臀部塑形
    @BindView(R.id.tv_Qianyinlashen) TextView tv_Qianyinlashen;//牵引拉伸
    @BindView(R.id.tv_Zhinenghehu) TextView tv_Zhinenghehu;//智能呵护
    @BindView(R.id.tv_Zhijianhuanxing) TextView tv_Zhijianhuanxing;//指尖唤醒
    @BindView(R.id.tv_Shangwuxiuxian) TextView tv_Shangwuxiuxian;//商务休闲
    @BindView(R.id.tv_Zhumianxiuyang) TextView tv_Zhumianxiuyang;//助眠修养
    @BindView(R.id.tv_Huanjiefangsong) TextView tv_Huanjiefangsong;//缓解放松

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

    //3D力度加减
    @BindView(R.id.iv_more_func_3d_strength_add) ImageView iv_more_func_3d_strength_add;
    @BindView(R.id.iv_more_func_3d_strength_minus) ImageView iv_more_func_3d_strength_minus;
    //气囊强度加减
    @BindView(R.id.iv_more_func_air_intensity_add) ImageView iv_more_func_air_intensity_add;
    @BindView(R.id.iv_more_func_air_intensity_minus) ImageView iv_more_func_air_intensity_minus;

    @Autowired
    public String macAddress;
    @Autowired
    public String chairName;
    @Autowired
    public String characteristicUuid;

    private DeviceDetailContract.Presenter mPresenter;

    private ActionSheet<DeviceAction> mPopup;
    private SlidingMenu<DeviceAction> mPopup2;

    private int TAB_CODE = 2;//手动自动标签：手动为1自动为2

    @Override
    protected int layoutResID() {
        return R.layout.activity_device_detail_home8;
    }

    @Override
    protected ImmersionBar initImmersionBar() {
        return ImmersionBar.with(this).statusBarColor(R.color.bg_device_detail_top).fitsSystemWindows(true);
    }

    @Override
    protected void initData() {
        mPresenter = new DeviceDetailPresenter();
        addPresenter(mPresenter);
        mPresenter.acceptData("chairName", chairName);
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
     * {@link DeviceDataParser#getDeviceState(List)}
     */
    @Override
    public void displayDeviceState(List<Boolean> data) {
//        Logger.e("displayTechniqueMode", "机器状态-->" + data);
        isPowerOn = data.get(0);
        boolean isPause = data.get(1);

        isAutoJiance = data.get(2);//true时为自动模式肩部检测中
        isAutoTiaojie = data.get(3);//true时为自动模式肩部调节中
        isAutoJianceComplete = data.get(4);//true时为自动模式肩部检测完成
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
        ll_rouqiao.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
        //图片变换
        iv_manual_rounie.setImageResource(R.mipmap.ic_manual_rounie_u);
        iv_manual_qiaoji.setImageResource(R.mipmap.ic_manual_qiaoji_u);
        iv_manual_zhiya.setImageResource(R.mipmap.ic_manual_zhiya_u);
        iv_manual_tuina.setImageResource(R.mipmap.ic_manual_tuina_u);
        iv_manual_rouqiao.setImageResource(R.mipmap.ic_manual_rouqiao_u);
        //字体变色
        tv_manual_rounie.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_qiaoji.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_zhiya.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_tuina.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_manual_rouqiao.setTextColor(getResources().getColor(R.color.bg_device_detail_top));

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
        rl_Huolihuifu.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Jianjinghuli.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Yaobushuhuan.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Tunbusuxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Qianyinlashen.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Zhinenghehu.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Zhijianhuanxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Shangwuxiuxian.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Zhumianxiuyang.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);
        rl_Huanjiefangsong.setBackgroundResource(R.mipmap.bg_device_detail_btn_u);

        iv_Huolihuifu.setImageResource(R.mipmap.ic_auto_huolihuifu_u);
        iv_Jianjinghuli.setImageResource(R.mipmap.ic_auto_jianjinghuli_u);
        iv_Yaobushuhuan.setImageResource(R.mipmap.ic_auto_yaobushuhuan_u);
        iv_Tunbusuxing.setImageResource(R.mipmap.ic_auto_tunbusuxing_u);
        iv_Qianyinlashen.setImageResource(R.mipmap.ic_auto_qianyinlashen_u);
        iv_Zhinenghehu.setImageResource(R.mipmap.ic_auto_zhinenghehu_u);
        iv_Zhijianhuanxing.setImageResource(R.mipmap.ic_auto_zhijianhuanxing_u);
        iv_Shangwuxiuxian.setImageResource(R.mipmap.ic_auto_shangwuxiuxian_u);
        iv_Zhumianxiuyang.setImageResource(R.mipmap.ic_auto_zhumianxiuyang_u);
        iv_Huanjiefangsong.setImageResource(R.mipmap.ic_auto_huanjiefangsong_u);

        tv_Huolihuifu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Jianjinghuli.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Yaobushuhuan.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Tunbusuxing.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Qianyinlashen.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Zhinenghehu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Zhijianhuanxing.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Shangwuxiuxian.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Zhumianxiuyang.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_Huanjiefangsong.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
    }

    /**
     * {@link DeviceDataParser#getTime(List)}
     */
    @Override
    public void displayTime(String time) {
        ViewHelper.setNoneNullText(tvTimer, time);
//        Logger.e("displayTime", "时间code-->" + time);
    }

    private boolean isReadManual = true;
    @Override
    public void displayAutoMode(int data3, int data4) {
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
                tv_autoStatus.setText("模式：活力恢复");
            }
            setAutoImagebgClose();
            rl_Huolihuifu.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Huolihuifu.setImageResource(R.mipmap.ic_auto_huolihuifu_ed);
            tv_Huolihuifu.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 1) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：肩颈护理");
            }
            setAutoImagebgClose();
            rl_Jianjinghuli.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Jianjinghuli.setImageResource(R.mipmap.ic_auto_jianjinghuli_ed);
            tv_Jianjinghuli.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 2) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：腰背舒缓");
            }
            setAutoImagebgClose();
            rl_Yaobushuhuan.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Yaobushuhuan.setImageResource(R.mipmap.ic_auto_yaobushuhuan_ed);
            tv_Yaobushuhuan.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 3) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：臀部塑型");
            }
            setAutoImagebgClose();
            rl_Tunbusuxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Tunbusuxing.setImageResource(R.mipmap.ic_auto_tunbusuxing_ed);
            tv_Tunbusuxing.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 4) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：牵引拉伸");
            }
            setAutoImagebgClose();
            rl_Qianyinlashen.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Qianyinlashen.setImageResource(R.mipmap.ic_auto_qianyinlashen_ed);
            tv_Qianyinlashen.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 5) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：智能呵护");
            }
            setAutoImagebgClose();
            rl_Zhinenghehu.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Zhinenghehu.setImageResource(R.mipmap.ic_auto_zhinenghehu_ed);
            tv_Zhinenghehu.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 6) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：指尖唤醒");
            }
            setAutoImagebgClose();
            rl_Zhijianhuanxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Zhijianhuanxing.setImageResource(R.mipmap.ic_auto_zhijianhuanxing_ed);
            tv_Zhijianhuanxing.setTextColor(getResources().getColor(R.color.white));
        } else if (data3 == 7) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：商务休闲");
            }
            setAutoImagebgClose();
            rl_Shangwuxiuxian.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Shangwuxiuxian.setImageResource(R.mipmap.ic_auto_shangwuxiuxian_ed);
            tv_Shangwuxiuxian.setTextColor(getResources().getColor(R.color.white));
        } else if (data4 == 0) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：助眠修养");
            }
            setAutoImagebgClose();
            rl_Zhumianxiuyang.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Zhumianxiuyang.setImageResource(R.mipmap.ic_auto_zhumianxiuyang_ed);
            tv_Zhumianxiuyang.setTextColor(getResources().getColor(R.color.white));
        } else if (data4 == 1) {
            tv_autoStatus.setTextColor(getResources().getColor(R.color.white));
            if (isAutoJiance && !isAutoJianceComplete) {
                tv_autoStatus.setText("肩部位置检测中，请等待...");
            } else {
                tv_autoStatus.setText("模式：缓解放松");
            }
            setAutoImagebgClose();
            rl_Huanjiefangsong.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
            iv_Huanjiefangsong.setImageResource(R.mipmap.ic_auto_huanjiefangsong_ed);
            tv_Huanjiefangsong.setTextColor(getResources().getColor(R.color.white));
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
     * {@link DeviceDataParser#getTechniqueMode(List)}
     */
    @Override
    public void displayTechniqueMode(int data) {
        ivRouNie.setEnabled(data == 0 || data == 2);
        ivQiaoJi.setEnabled(data == 1 || data == 2);
        ivZhiYa.setEnabled(data == 3);
        ivTuiNa.setEnabled(data == 4);

        if (isReadManual) {
            if (data == 0) {
                tv_autoStatus.setText("自定义：揉捏");
            } else if (data == 1) {
                tv_autoStatus.setText("自定义：敲打");
            } else if (data == 2) {
                tv_autoStatus.setText("自定义：揉敲");
            } else if (data == 3) {
                tv_autoStatus.setText("自定义：指压");
            } else if (data == 4) {
                tv_autoStatus.setText("自定义：推拿");
            }
        }
//        Logger.e("displayTechniqueMode", "手法code-->" + data);
    }

    /**
     * {@link DeviceDataParser#get3DStrength(List)}
     */
    @Override
    public void display3DStrength(int data) {
//        Logger.e("display3DStrength", "3D力度code-->" + data);
        int imgRes;
        if (data == NO_DATA) {
            imgRes = R.mipmap.ic_3d_new_0;
            tvMF3DStrength.setText(DEVICE_DETAIL_NO_DATA);
        } else {
            int level = data + 1;
            imgRes = ResourceUtils.getDrawableIDByName("ic_3d_new_" + level);
            tvMF3DStrength.setText(level + "档");
        }
        iv3D.setImageResource(imgRes);
    }

    int zeroCode = -1;
    /**
     * {@link DeviceDataParser#getZeroGravityLevel(List)}}
     */
    @Override
    public void displayZeroGravity(int data) {
        zeroCode = data;
//        Logger.e("displayZeroGravity", "零重力code-->" + data);
        int imgRes;
        if (data == NO_DATA) {
            imgRes = R.mipmap.ic_device_detail_technique_home8_7_off;
        } else {
            //老代码有三张图这里只有一张所以重写
//            imgRes = ResourceUtils.getDrawableIDByName("ic_device_detail_technique_7_on_" + (data + 1));
            //新代码零重力逻辑
//            imgRes = R.mipmap.ic_device_detail_technique_new_7_on_1;
            imgRes = ResourceUtils.getDrawableIDByName("ic_device_detail_technique_home8_7_on_" + (data + 1));
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
            int imRes = ResourceUtils.getDrawableIDByName("ic_bei_gun_new_" + (beiGun + 1));
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

        /**
         * 腿滚和脚滚显示反了
         */
//        ivTuiGun.setVisibility(data.get(2) ? View.VISIBLE : View.GONE);
//        ivJiaoGun.setVisibility(data.get(3) ? View.VISIBLE : View.GONE);

        ivTuiGun.setVisibility(data.get(3) ? View.VISIBLE : View.GONE);
        ivJiaoGun.setVisibility(data.get(2) ? View.VISIBLE : View.GONE);
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
        if (data == NO_DATA) {
            iv_speed.setImageResource(R.mipmap.ic_level_0);
        } else {
            int imgRes = ResourceUtils.getDrawableIDByName("ic_level_" + (data + 1));
            iv_speed.setImageResource(imgRes);
        }

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

    /**
     * {@link DeviceDataParser#getGearLevel2(List)}
     */
    @Override
    public void displayGearLevel2(int data) {
//        Logger.e("displayGearLevel2", "气囊强度code-->" + data);
        if (data == NO_DATA) {
            tvMFAirIntensity.setText(DEVICE_DETAIL_NO_DATA);
            iv_air.setImageResource(R.mipmap.ic_levelmax_0);
        } else {
            tvMFAirIntensity.setText((data + 1) + "档");
            int imgRes = ResourceUtils.getDrawableIDByName("ic_levelmax_" + (data + 1));
            iv_air.setImageResource(imgRes);
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
//        showPopup(ManualActionData.getZeroGravityList());
//        mPresenter.toggleZeroGravity();

        mPresenter.zeroGravity(zeroCode);
//        if (zeroCode == -1) {
//            Logger.e("为-1", "执行1档");
//            getBLEPresenter().writeData(ManualActionData.getZeroGravityList().get(0));//1挡
//        } else if (zeroCode == 0) {
//            Logger.e("为0", "执行2档");
//            getBLEPresenter().writeData(ManualActionData.getZeroGravityList().get(1));//2挡
//        } else if (zeroCode == 1) {
//            Logger.e("为1", "执行3档");
//            getBLEPresenter().writeData(ManualActionData.getZeroGravityList().get(2));//3挡
//        } else if (zeroCode == 2) {
//            Logger.e("为2", "执行复位档");
//            getBLEPresenter().writeData(ManualActionData.getZeroGravityList().get(2));//3挡
////            mPresenter.toggleZeroGravity();
//        }
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
    @OnClick({R.id.ll_rounie, R.id.ll_qiaoji, R.id.ll_zhiya, R.id.ll_tuina, R.id.ll_rouqiao})
    public void manualFunction(View view) {
        switch (view.getId()) {
            case R.id.ll_rounie:
//                if (!isPowerOn) {
//                    Toaster.showShort("未开机");
//                    return;
//                }
//                ll_rounie.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                ll_qiaoji.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_zhiya.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_tuina.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                //图片变换
//                iv_manual_rounie.setImageResource(R.mipmap.ic_manual_rounie_ed);
//                iv_manual_qiaoji.setImageResource(R.mipmap.ic_manual_qiaoji_u);
//                iv_manual_zhiya.setImageResource(R.mipmap.ic_manual_zhiya_u);
//                iv_manual_tuina.setImageResource(R.mipmap.ic_manual_tuina_u);
//                //字体变色
//                tv_manual_rounie.setTextColor(getResources().getColor(R.color.white));
//                tv_manual_qiaoji.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_zhiya.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_tuina.setTextColor(getResources().getColor(R.color.bg_device_detail_top));

                //手法-->揉捏
                getBLEPresenter().writeData(ManualActionData.getTechniqueModeList().get(0));
                Logger.i("T100DeviceDetailActivity", "手动模块-->揉捏");
                break;
            case R.id.ll_qiaoji:
//                ll_rounie.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiaoji.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                ll_zhiya.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_tuina.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                //图片变换
//                iv_manual_rounie.setImageResource(R.mipmap.ic_manual_rounie_u);
//                iv_manual_qiaoji.setImageResource(R.mipmap.ic_manual_qiaoji_ed);
//                iv_manual_zhiya.setImageResource(R.mipmap.ic_manual_zhiya_u);
//                iv_manual_tuina.setImageResource(R.mipmap.ic_manual_tuina_u);
//                //字体变色
//                tv_manual_rounie.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiaoji.setTextColor(getResources().getColor(R.color.white));
//                tv_manual_zhiya.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_tuina.setTextColor(getResources().getColor(R.color.bg_device_detail_top));

                //手法-->敲击
                getBLEPresenter().writeData(ManualActionData.getTechniqueModeList().get(1));
                Logger.i("T100DeviceDetailActivity", "手动模块-->敲击");
                break;
            case R.id.ll_rouqiao:
                //手法-->揉敲
                getBLEPresenter().writeData(ManualActionData.getTechniqueModeList().get(2));
                break;
            case R.id.ll_zhiya:
//                ll_rounie.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiaoji.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_zhiya.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                ll_tuina.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                //图片变换
//                iv_manual_rounie.setImageResource(R.mipmap.ic_manual_rounie_u);
//                iv_manual_qiaoji.setImageResource(R.mipmap.ic_manual_qiaoji_u);
//                iv_manual_zhiya.setImageResource(R.mipmap.ic_manual_zhiya_ed);
//                iv_manual_tuina.setImageResource(R.mipmap.ic_manual_tuina_u);
//                //字体变色
//                tv_manual_rounie.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiaoji.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_zhiya.setTextColor(getResources().getColor(R.color.white));
//                tv_manual_tuina.setTextColor(getResources().getColor(R.color.bg_device_detail_top));

                //手法-->指压
                getBLEPresenter().writeData(ManualActionData.getTechniqueModeList().get(3));
                Logger.i("T100DeviceDetailActivity", "手动模块-->指压");
                break;
            case R.id.ll_tuina:
//                ll_rounie.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiaoji.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_zhiya.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_tuina.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                //图片变换
//                iv_manual_rounie.setImageResource(R.mipmap.ic_manual_rounie_u);
//                iv_manual_qiaoji.setImageResource(R.mipmap.ic_manual_qiaoji_u);
//                iv_manual_zhiya.setImageResource(R.mipmap.ic_manual_zhiya_u);
//                iv_manual_tuina.setImageResource(R.mipmap.ic_manual_tuina_ed);
//                //字体变色
//                tv_manual_rounie.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiaoji.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_zhiya.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_tuina.setTextColor(getResources().getColor(R.color.white));

                //手法-->推拿
                getBLEPresenter().writeData(ManualActionData.getTechniqueModeList().get(4));
                Logger.i("T100DeviceDetailActivity", "手动模块-->推拿");
                break;
        }
    }

    //部位切换6个
    @OnClick({R.id.ll_quyuquanshen, R.id.ll_qujianbeibu, R.id.ll_qujianyaobu, R.id.ll_qujianzuobu, R.id.ll_qujianbeiyao, R.id.ll_qujianyaozuo})
    public void manualBodypartFunction(View view) {
        switch (view.getId()) {
            case R.id.ll_quyuquanshen:
//                if (!isPowerOn) {
//                    Toaster.showShort("未开机");
//                    return;
//                }
//                ll_quyuquanshen.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                ll_qujianbeibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianyaobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianzuobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianbeiyao.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianyaozuo.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                //字体变色
//                tv_manual_quyuquanshen.setTextColor(getResources().getColor(R.color.white));
//                tv_manual_qujianbeibu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianyaobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianzuobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianbeiyao.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianyaozuo.setTextColor(getResources().getColor(R.color.bg_device_detail_top));

                //部位切换-->区域全身
                getBLEPresenter().writeData(ManualActionData.getBodyPartList().get(0));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区域全身");
                break;
            case R.id.ll_qujianbeibu:
//                ll_quyuquanshen.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianbeibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                ll_qujianyaobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianzuobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianbeiyao.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianyaozuo.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                //字体变色
//                tv_manual_quyuquanshen.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianbeibu.setTextColor(getResources().getColor(R.color.white));
//                tv_manual_qujianyaobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianzuobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianbeiyao.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianyaozuo.setTextColor(getResources().getColor(R.color.bg_device_detail_top));

                //部位切换-->区间背部
                getBLEPresenter().writeData(ManualActionData.getBodyPartList().get(1));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区间背部");
                break;
            case R.id.ll_qujianyaobu:
//                ll_quyuquanshen.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianbeibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianyaobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                ll_qujianzuobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianbeiyao.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianyaozuo.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                //字体变色
//                tv_manual_quyuquanshen.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianbeibu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianyaobu.setTextColor(getResources().getColor(R.color.white));
//                tv_manual_qujianzuobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianbeiyao.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianyaozuo.setTextColor(getResources().getColor(R.color.bg_device_detail_top));

                //部位切换-->区间腰部
                getBLEPresenter().writeData(ManualActionData.getBodyPartList().get(2));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区间腰部");
                break;
            case R.id.ll_qujianzuobu:
//                ll_quyuquanshen.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianbeibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianyaobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianzuobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                ll_qujianbeiyao.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianyaozuo.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                //字体变色
//                tv_manual_quyuquanshen.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianbeibu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianyaobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianzuobu.setTextColor(getResources().getColor(R.color.white));
//                tv_manual_qujianbeiyao.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianyaozuo.setTextColor(getResources().getColor(R.color.bg_device_detail_top));

                //部位切换-->区间座部
                getBLEPresenter().writeData(ManualActionData.getBodyPartList().get(3));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区间座部");
                break;
            case R.id.ll_qujianbeiyao:
//                ll_quyuquanshen.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianbeibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianyaobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianzuobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianbeiyao.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                ll_qujianyaozuo.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                //字体变色
//                tv_manual_quyuquanshen.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianbeibu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianyaobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianzuobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianbeiyao.setTextColor(getResources().getColor(R.color.white));
//                tv_manual_qujianyaozuo.setTextColor(getResources().getColor(R.color.bg_device_detail_top));

                //部位切换-->区间背腰
                getBLEPresenter().writeData(ManualActionData.getBodyPartList().get(4));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区间背腰");
                break;
            case R.id.ll_qujianyaozuo:
//                ll_quyuquanshen.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianbeibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianyaobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianzuobu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianbeiyao.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qujianyaozuo.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                //字体变色
//                tv_manual_quyuquanshen.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianbeibu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianyaobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianzuobu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianbeiyao.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qujianyaozuo.setTextColor(getResources().getColor(R.color.white));

                //部位切换-->区间腰座
                getBLEPresenter().writeData(ManualActionData.getBodyPartList().get(5));
                Logger.i("T100DeviceDetailActivity", "部位切换-->区间腰座");
                break;
        }
    }

    //手动模块4个气压部位功能
    @OnClick({R.id.ll_qiyayaojian, R.id.ll_qiyatunbu, R.id.ll_qiyashoubu, R.id.ll_qiyatuibu})
    public void manualPressurePartFunction(View view) {
        switch (view.getId()) {
            case R.id.ll_qiyayaojian:
//                ll_qiyayaojian.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                ll_qiyatunbu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiyashoubu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiyatuibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//
//                tv_manual_qiyayaojian.setTextColor(getResources().getColor(R.color.white));
//                tv_manual_qiyatunbu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiyashoubu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiyatuibu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
                //手动-->气压腰肩
                getBLEPresenter().writeData(ManualActionData.getPressurePartList().get(0));
//                mPresenter.toggleQiyayaojian();
                Logger.i("T100DeviceDetailActivity", "手动模块-->气压腰肩");
                break;
            case R.id.ll_qiyatunbu:
//                ll_qiyayaojian.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiyatunbu.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                ll_qiyashoubu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiyatuibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//
//                tv_manual_qiyayaojian.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiyatunbu.setTextColor(getResources().getColor(R.color.white));
//                tv_manual_qiyashoubu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiyatuibu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
                //手动-->气压臀部
                getBLEPresenter().writeData(ManualActionData.getPressurePartList().get(1));
//                mPresenter.toggleQiyatunbu();
                Logger.i("T100DeviceDetailActivity", "手动模块-->气压臀部");
                break;
            case R.id.ll_qiyashoubu:
//                ll_qiyayaojian.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiyatunbu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiyashoubu.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//                ll_qiyatuibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//
//                tv_manual_qiyayaojian.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiyatunbu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiyashoubu.setTextColor(getResources().getColor(R.color.white));
//                tv_manual_qiyatuibu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
                //手动-->气压手部
                getBLEPresenter().writeData(ManualActionData.getPressurePartList().get(2));
//                mPresenter.toggleQiyashoubu();
                Logger.i("T100DeviceDetailActivity", "手动模块-->气压手部");
                break;
            case R.id.ll_qiyatuibu:
//                ll_qiyayaojian.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiyatunbu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiyashoubu.setBackgroundResource(R.drawable.bg_device_detail_btnele_u);
//                ll_qiyatuibu.setBackgroundResource(R.drawable.bg_device_detail_btnele_d);
//
//                tv_manual_qiyayaojian.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiyatunbu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiyashoubu.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
//                tv_manual_qiyatuibu.setTextColor(getResources().getColor(R.color.white));
                //手动-->气压腿部
                getBLEPresenter().writeData(ManualActionData.getPressurePartList().get(3));
//                mPresenter.toggleQiyatuibu();
                Logger.i("T100DeviceDetailActivity", "手动模块-->气压腿部");
                break;
        }
    }

    //幅度3档
    @OnClick({R.id.tv_fudu_low, R.id.tv_fudu_mid, R.id.tv_fudu_high})
    public void manualFuduFunction(View view) {
        switch (view.getId()) {
            case R.id.tv_fudu_low:
//                setFuduTextColorClose();
//                tv_fudu_low.setTextColor(getResources().getColor(R.color.yellow_shoufa));
                getBLEPresenter().writeData(ManualActionData.getWidthList().get(0));
                YoYo.with(Techniques.Tada)
                        .duration(1000)
                        .repeat(1)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(tv_fudu_low);
                break;
            case R.id.tv_fudu_mid:
//                setFuduTextColorClose();
//                tv_fudu_mid.setTextColor(getResources().getColor(R.color.yellow_shoufa));
                getBLEPresenter().writeData(ManualActionData.getWidthList().get(1));
                YoYo.with(Techniques.Tada)
                        .duration(1000)
                        .repeat(1)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(tv_fudu_mid);
                break;
            case R.id.tv_fudu_high:
//                setFuduTextColorClose();
//                tv_fudu_high.setTextColor(getResources().getColor(R.color.yellow_shoufa));
                getBLEPresenter().writeData(ManualActionData.getWidthList().get(2));
                YoYo.with(Techniques.Tada)
                        .duration(1000)
                        .repeat(1)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(tv_fudu_high);
                break;
        }
    }

    private void setFuduTextColorClose() {
        tv_fudu_low.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_fudu_mid.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
        tv_fudu_high.setTextColor(getResources().getColor(R.color.bg_device_detail_top));
    }

    //自动模块功能
    @OnClick({R.id.rl_Huolihuifu, R.id.rl_Jianjinghuli, R.id.rl_Yaobushuhuan, R.id.rl_Tunbusuxing,
            R.id.rl_Qianyinlashen, R.id.rl_Zhinenghehu, R.id.rl_Zhijianhuanxing, R.id.rl_Shangwuxiuxian,
            R.id.rl_Zhumianxiuyang, R.id.rl_Huanjiefangsong})
    public void autoFunction(View view) {
        switch (view.getId()) {
            case R.id.rl_Huolihuifu:
                //活力恢复
                getBLEPresenter().writeData(AutoActionData.get().get(0));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：活力恢复");
                    startShakeByView(tv_autoStatus, 1f, 1.2f, 1, 1200);
                } else {
                    setAutoImagebgClose();
                    rl_Huolihuifu.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Huolihuifu.setImageResource(R.mipmap.ic_auto_huolihuifu_ed);
                    tv_Huolihuifu.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Jianjinghuli:
                //肩颈护理
                getBLEPresenter().writeData(AutoActionData.get().get(1));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：肩颈护理");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Jianjinghuli.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Jianjinghuli.setImageResource(R.mipmap.ic_auto_jianjinghuli_ed);
                    tv_Jianjinghuli.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Yaobushuhuan:
                //腰部舒缓
                getBLEPresenter().writeData(AutoActionData.get().get(2));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：腰部舒缓");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Yaobushuhuan.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Yaobushuhuan.setImageResource(R.mipmap.ic_auto_yaobushuhuan_ed);
                    tv_Yaobushuhuan.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Tunbusuxing:
                //臀部塑型
                getBLEPresenter().writeData(AutoActionData.get().get(3));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：臀部塑型");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Tunbusuxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Tunbusuxing.setImageResource(R.mipmap.ic_auto_tunbusuxing_ed);
                    tv_Tunbusuxing.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Qianyinlashen:
                //牵引拉伸
                getBLEPresenter().writeData(AutoActionData.get().get(4));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：牵引拉伸");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Qianyinlashen.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Qianyinlashen.setImageResource(R.mipmap.ic_auto_qianyinlashen_ed);
                    tv_Qianyinlashen.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Zhinenghehu:
                //智能呵护
                getBLEPresenter().writeData(AutoActionData.get().get(5));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：智能呵护");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Zhinenghehu.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Zhinenghehu.setImageResource(R.mipmap.ic_auto_zhinenghehu_ed);
                    tv_Zhinenghehu.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Zhijianhuanxing:
                //指尖唤醒
                getBLEPresenter().writeData(AutoActionData.get().get(6));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：指尖唤醒");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Zhijianhuanxing.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Zhijianhuanxing.setImageResource(R.mipmap.ic_auto_zhijianhuanxing_ed);
                    tv_Zhijianhuanxing.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Shangwuxiuxian:
                //商务休闲
                getBLEPresenter().writeData(AutoActionData.get().get(7));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：商务休闲");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Shangwuxiuxian.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Shangwuxiuxian.setImageResource(R.mipmap.ic_auto_shangwuxiuxian_ed);
                    tv_Shangwuxiuxian.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Zhumianxiuyang:
                //助眠修养
                getBLEPresenter().writeData(AutoActionData.get().get(8));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：助眠修养");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Zhumianxiuyang.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Zhumianxiuyang.setImageResource(R.mipmap.ic_auto_zhumianxiuyang_ed);
                    tv_Zhumianxiuyang.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.rl_Huanjiefangsong:
                //缓解放松
                getBLEPresenter().writeData(AutoActionData.get().get(9));
                if (isPowerOn) {
                    tv_autoStatus.setText("模式：缓解放松");
                    startShakeByView(tv_autoStatus, 1f, 1.1f, 1, 1000);
                } else {
                    setAutoImagebgClose();
                    rl_Huanjiefangsong.setBackgroundResource(R.mipmap.bg_device_detail_btn_d);
                    iv_Huanjiefangsong.setImageResource(R.mipmap.ic_auto_huanjiefangsong_ed);
                    tv_Huanjiefangsong.setTextColor(getResources().getColor(R.color.white));
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
