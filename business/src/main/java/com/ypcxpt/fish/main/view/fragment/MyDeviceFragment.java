package com.ypcxpt.fish.main.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.ypcxpt.fish.BuildConfig;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.VpSwipeRefreshLayout;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.view.fragment.BaseFragment;
import com.ypcxpt.fish.main.adapter.IOAdapter;
import com.ypcxpt.fish.main.adapter.SceneAdapter;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.event.OnGetScenesEvent;
import com.ypcxpt.fish.main.event.OnMainPagePermissionResultEvent;
import com.ypcxpt.fish.main.event.OnSceneInfoEvent;
import com.ypcxpt.fish.main.event.OnScreenEvent;
import com.ypcxpt.fish.main.model.CamsMove;
import com.ypcxpt.fish.main.model.CamsUseable;
import com.ypcxpt.fish.main.model.CamsUseableProfiles;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.IoInfoCurrent;
import com.ypcxpt.fish.main.model.IoStatus;
import com.ypcxpt.fish.main.model.IoStatusAll;
import com.ypcxpt.fish.main.model.Scenes;
import com.ypcxpt.fish.main.model.WeatherInfo;
import com.ypcxpt.fish.main.model.WebSocketInfo;
import com.ypcxpt.fish.main.presenter.MyDevicePresenter;
import com.ypcxpt.fish.main.util.CamsAuthDialog;
import com.ypcxpt.fish.main.util.JWebSocketClient;
import com.ypcxpt.fish.main.util.MainOperationDialog;
import com.ypcxpt.fish.main.util.ScenesRenameDialog;
import com.ypcxpt.fish.main.view.activity.CaptureScanActivity;

import org.easydarwin.util.C;
import org.easydarwin.video.EasyPlayerClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.ypcxpt.fish.BaseUrlConstant.WEBSOCKET_URI;
import static com.ypcxpt.fish.app.util.DisplayUtils.getWeatherCollections;
import static com.ypcxpt.fish.app.util.DisplayUtils.getWeatherIcon;

public class MyDeviceFragment extends BaseFragment implements MyDeviceContract.View {
    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_curr_temp)
    TextView tv_curr_temp;
    @BindView(R.id.iv_weather)
    ImageView iv_weather;
    @BindView(R.id.tv_weather_info)
    TextView tv_weather_info;
    @BindView(R.id.tv_secondary_weather_collection)
    TextView tv_secondary_weather_collection;

    @BindView(R.id.iv_weather2)
    ImageView iv_weather2;
    @BindView(R.id.tv_weather_info2)
    TextView tv_weather_info2;
    @BindView(R.id.tv_secondary_weather_collection2)
    TextView tv_secondary_weather_collection2;

    @BindView(R.id.rv_io)
    RecyclerView rv_io;

    @BindView(R.id.swipe_refresh_layout)
    VpSwipeRefreshLayout swipe_refresh_layout;

    @BindView(R.id.rl_hide01)
    RelativeLayout rl_hide01;
    @BindView(R.id.ll_hide02)
    LinearLayout ll_hide02;

    @BindView(R.id.texture_view)
    TextureView texture_view;
//    @BindView(R.id.vlcMediaView)
//    VlcMediaView vlcMediaView;
    @BindView(R.id.iv_videobg)
    ImageView iv_videobg;
    @BindView(R.id.tv_cams01)
    TextView tv_cams01;
    @BindView(R.id.tv_cams02)
    TextView tv_cams02;
    @BindView(R.id.tv_cams03)
    TextView tv_cams03;

    @BindView(R.id.rl_videobg)
    RelativeLayout rl_videobg;
    @BindView(R.id.rl_weather)
    RelativeLayout rl_weather;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.iv_play)
    ImageView iv_play;
    @BindView(R.id.iv_pause)
    ImageView iv_pause;

    @BindView(R.id.iv_enableAudio)
    ImageView iv_enableAudio;
    @BindView(R.id.tv_videoLabel)
    TextView tv_videoLabel;
    @BindView(R.id.iv_big)
    ImageView iv_big;

    @BindView(R.id.ll_temperature)
    LinearLayout ll_temperature;
    @BindView(R.id.tv_temperature)
    TextView tv_temperature;
    @BindView(R.id.ll_ph)
    LinearLayout ll_ph;
    @BindView(R.id.tv_ph)
    TextView tv_ph;
    @BindView(R.id.ll_oxygen)
    LinearLayout ll_oxygen;
    @BindView(R.id.tv_oxygen)
    TextView tv_oxygen;

    private MyDeviceContract.Presenter mPresenter;

    private SceneAdapter mAdapter;
    private IOAdapter ioAdapter;

    private int REQUEST_CODE_SCAN = 111;

    public static int sceneSelected = 0;
    /* 场景数组 */
    private List<Scenes> mScenes;
    /* 当前场景mac */
    public static String macAddress;
    /* 当前场景名称 */
    public static String sceneName;

    private String mPassKey = "";
    public static String mPass;
    /* 可用的摄像头 */
    private List<CamsUseable> usableCams;
    /* 摄像头唯一标识 */
    private String camsKey;
    /* 摄像头唯一标识下标 */
    private int camsIndex;
    /* 每个摄像头中的播放信息(清晰度) */
    private List<CamsUseableProfiles> profiles;

    private EasyPlayerClient easyPlayerClient;
    private ResultReceiver mResultReceiver;
    public static boolean isFullScreen = false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
            /* 全屏 */
            rl_hide01.setVisibility(View.GONE);
            ll_hide02.setVisibility(View.GONE);
            rv_io.setVisibility(View.GONE);

            //取控件当前的布局参数
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_videobg.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            rl_videobg.setLayoutParams(params);

            EventBus.getDefault().post(new OnScreenEvent(true));
        } else {
            /* 竖屏 */
            rl_hide01.setVisibility(View.VISIBLE);
            ll_hide02.setVisibility(View.VISIBLE);
            rv_io.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_videobg.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = getResources().getDimensionPixelSize(R.dimen.dp200);
            rl_videobg.setLayoutParams(params);

            EventBus.getDefault().post(new OnScreenEvent(false));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            //隐藏的操作
        } else {
            //显示的操作
            /* 获取摄像头配置 */
            mPresenter.getCamsConfig(macAddress);
        }
    }

    @OnClick(R.id.iv_big)
    public void onScreen() {
        if (isFullScreen) {
            isFullScreen = false;
            //缩小竖屏
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            swipe_refresh_layout.setEnabled(true);
        } else {
            isFullScreen = true;
            //放大全屏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

            swipe_refresh_layout.setEnabled(false);
        }
    }

    @OnClick({R.id.iv_play, R.id.iv_pause})
    public void videoStart(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                if (!StringUtils.isEmpty(mPass)) {
                    //输入摄像头密码
                    showCamsAuth();
                    return;
                }
                /* 请求推流 */
                mPresenter.doCamsPlay(macAddress, usableCams, camsKey, camsIndex);
//                iv_pause.setVisibility(View.VISIBLE);
                iv_play.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_pause:
                if (easyPlayerClient != null) {
                    texture_view.setVisibility(View.GONE);
                    easyPlayerClient.stop();
                }
//                iv_pause.setVisibility(View.GONE);
                iv_play.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                break;
        }
    }

    private void showCamsAuth() {
        CamsAuthDialog camsAuthDialog = new CamsAuthDialog(getActivity(), R.style.MyDialog);
        camsAuthDialog.setCancelable(false);
        camsAuthDialog.setOnResultListener(new CamsAuthDialog.OnResultListener() {
            @Override
            public void Ok(String pass) {
                if (StringUtils.isTrimEmpty(pass)) {
                    Toaster.showShort("请输入摄像头口令");
                } else {
                    if (mPass.equals(pass)) {
                        mPresenter.getNotAvailableCams(macAddress, mPassKey, pass);
                        camsAuthDialog.dismiss();
                    } else {
                        Toaster.showShort("口令不正确");
                    }
                }
            }

            @Override
            public void Cancel() {
                camsAuthDialog.dismiss();
            }
        });
        camsAuthDialog.show();
    }

    @OnClick(R.id.iv_enableAudio)
    public void onEnableAudio() {
        if (easyPlayerClient != null) {
            easyPlayerClient.setAudioEnable(!easyPlayerClient.isAudioEnable());
            if (easyPlayerClient.isAudioEnable()) {
                iv_enableAudio.setImageResource(R.mipmap.icon_main_voice_ed);
            } else {
                iv_enableAudio.setImageResource(R.mipmap.icon_main_voice_u);
            }
        }
    }

    @Override
    protected int layoutResID() {
        return R.layout.fragment_my_device;
    }

    @Override
    protected void initData() {
        mPresenter = new MyDevicePresenter();
        addPresenter(mPresenter);
    }

    @Override
    protected void initViews() {
        mResultReceiver = new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);

                Activity activity = getActivity();

                if (activity == null)
                    return;

                if (resultCode == EasyPlayerClient.RESULT_VIDEO_DISPLAYED) {
                    if (resultData != null) {
                        int videoDecodeType = resultData.getInt(EasyPlayerClient.KEY_VIDEO_DECODE_TYPE, 0);
                        Logger.i("ResultReceiver", "视频解码方式:" + (videoDecodeType == 0 ? "软解码" : "硬解码"));
                    }

                    progress.setVisibility(View.GONE);
                    if (easyPlayerClient != null) {
                        if (easyPlayerClient.isAudioEnable()) {
                            iv_enableAudio.setImageResource(R.mipmap.icon_main_voice_ed);
                        } else {
                            iv_enableAudio.setImageResource(R.mipmap.icon_main_voice_u);
                        }
                    }
                } else if (resultCode == EasyPlayerClient.RESULT_VIDEO_SIZE) {
                    progress.setVisibility(View.GONE);
                    if (easyPlayerClient != null) {
                        if (easyPlayerClient.isAudioEnable()) {
                            iv_enableAudio.setImageResource(R.mipmap.icon_main_voice_ed);
                        } else {
                            iv_enableAudio.setImageResource(R.mipmap.icon_main_voice_u);
                        }
                    }
                } else if (resultCode == EasyPlayerClient.RESULT_TIMEOUT) {
                    new AlertDialog.Builder(getActivity()).setMessage("试播时间到").setTitle("SORRY").setPositiveButton(android.R.string.ok, null).show();
                } else if (resultCode == EasyPlayerClient.RESULT_UNSUPPORTED_AUDIO) {
                    new AlertDialog.Builder(getActivity()).setMessage("音频格式不支持").setTitle("SORRY").setPositiveButton(android.R.string.ok, null).show();
                } else if (resultCode == EasyPlayerClient.RESULT_UNSUPPORTED_VIDEO) {
                    new AlertDialog.Builder(getActivity()).setMessage("视频格式不支持").setTitle("SORRY").setPositiveButton(android.R.string.ok, null).show();
                } else if (resultCode == EasyPlayerClient.RESULT_EVENT) {
                    int errorCode = resultData.getInt("errorcode");
//                    if (errorCode != 0) {
//                        stopRending();
//                    }

//                    if (activity instanceof PlayActivity) {
//                        int state = resultData.getInt("state");
//                        String msg = resultData.getString("event-msg");
//                        ((PlayActivity) activity).onEvent(PlayFragment.this, state, errorCode, msg);
//                    }
                } else if (resultCode == EasyPlayerClient.RESULT_RECORD_BEGIN) {
//                    if (activity instanceof PlayActivity)
//                        ((PlayActivity)activity).onRecordState(1);
                } else if (resultCode == EasyPlayerClient.RESULT_RECORD_END) {
//                    if (activity instanceof PlayActivity)
//                        ((PlayActivity)activity).onRecordState(-1);
                }
            }
        };

        mAdapter = new SceneAdapter(R.layout.item_scenes, mPresenter, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(mAdapter);
        ((DefaultItemAnimator) rv.getItemAnimator()).setSupportsChangeAnimations(false);
        rv.getItemAnimator().setChangeDuration(0);// 通过设置动画执行时间为0来解决闪烁问题
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mPresenter.acceptData("mAdapter", mAdapter);

        ioAdapter = new IOAdapter(R.layout.item_io, mPresenter, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_io.setLayoutManager(gridLayoutManager);
        rv_io.setAdapter(ioAdapter);
        ((DefaultItemAnimator) rv.getItemAnimator()).setSupportsChangeAnimations(false);
        rv_io.getItemAnimator().setChangeDuration(0);// 通过设置动画执行时间为0来解决闪烁问题

        //设置刷新监听器
        swipe_refresh_layout.setOnRefreshListener(() -> {
            //模拟耗时操作
            new Handler().postDelayed(() -> {
                swipe_refresh_layout.setRefreshing(false);//取消刷新
                ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnMainPagePermissionResultEvent()), 750);
            }, 2000);
        });
        //设置刷新时旋转图标的颜色，这是一个可变参数，当设置多个颜色时，旋转一周改变一次颜色。
        swipe_refresh_layout.setColorSchemeResources(R.color.main_color_new, R.color.bg_device_detail_yellow, R.color.bg_device_detail_top);

        iv_videobg.setOnTouchListener(new View.OnTouchListener() {
            float posX;
            float posY;
            float curPosX;
            float curPosY;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        posX = event.getX();
                        posY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        curPosX = event.getX();
                        curPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if ((curPosX - posX > 0) && (Math.abs(curPosX - posX) > 25)) {
                            Logger.e("----", "向右滑动" + (curPosX - posX));
                        } else if ((curPosX - posX < 0) && (Math.abs(curPosX - posX) > 25)) {
                            Logger.e("----", "向左滑动" + (curPosX - posX));
                        }
                        if ((curPosY - posY > 0) && (Math.abs(curPosY - posY) > 25)) {
                            Logger.e("----", "向下滑动" + (curPosY - posY));
                        } else if ((curPosY - posY < 0) && (Math.abs(curPosY - posY) > 25)) {
                            Logger.e("----", "向上滑动" + (curPosY - posY));
                        }
                        float x = (curPosX - posX)/2000;
                        float y = (curPosY - posY)/1000;
                        if (isFullScreen && easyPlayerClient != null) {
                            doCamsMove(x, y);
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void doCamsMove(float x, float y) {
        CamsMove camsMove = new CamsMove();
        camsMove.setX(x);
        camsMove.setY(y);
        mPresenter.doCamsMove(macAddress, camsKey, camsMove);
    }

    /**
     * 操作鱼塘弹窗
     */
    private void showOperationDialog() {
        MainOperationDialog operationDialog = new MainOperationDialog(getActivity(), R.style.MyDialog);
        operationDialog.setOnResultListener(new MainOperationDialog.OnResultListener() {

            @Override
            public void Add() {
                operationDialog.dismiss();
                addByScanCode();
            }

            @Override
            public void Remove() {
                operationDialog.dismiss();
                mPresenter.removeScenes(macAddress);
            }

            @Override
            public void Rename() {
                operationDialog.dismiss();
                showRenameDialog(macAddress, "", 2);
            }

            @Override
            public void Config() {
                operationDialog.dismiss();
                Router.build(Path.Main.IO_CONFIG).withString("DEVICE_MAC", macAddress).withInt("SCENE_SELECTED", sceneSelected).navigation(getActivity());
            }

            @Override
            public void Cancel() {
                operationDialog.dismiss();
            }
        });
        Window dialogWindow = operationDialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        operationDialog.show();
    }

    private void addByScanCode() {
        AndPermission.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        startActivityForResult(new Intent(getActivity(), CaptureScanActivity.class), REQUEST_CODE_SCAN);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Uri packageURI = Uri.parse("package:" + "com.ypcxpt.fish");
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                        Toast.makeText(getActivity(), "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                    }
                }).start();
    }

    @OnClick(R.id.iv_main_add)
    public void onClickAdd() {
        showOperationDialog();
    }

    @Override
    public void onGetWhetherResult(WeatherInfo weatherInfo) {
        if (weatherInfo != null && weatherInfo.getData() != null) {
            tv_location.setText(weatherInfo.getData().getCity());
            if (weatherInfo.getData().getData() != null) {
                if (weatherInfo.getData().getData().size() > 0) {
                    tv_date.setText(weatherInfo.getData().getData().get(0).getDate() + " " + weatherInfo.getData().getData().get(0).getWeek());
                    iv_weather.setImageResource(getWeatherIcon(weatherInfo.getData().getData().get(0)));
                    tv_curr_temp.setText(weatherInfo.getData().getData().get(0).getTem() + "");
                    tv_weather_info.setText(weatherInfo.getData().getData().get(0).getWea());
                    tv_secondary_weather_collection.setText(getWeatherCollections(weatherInfo.getData().getData().get(0)));
                }
                if (weatherInfo.getData().getData().size() > 1) {
                    iv_weather2.setImageResource(getWeatherIcon(weatherInfo.getData().getData().get(1)));
                    tv_weather_info2.setText(weatherInfo.getData().getData().get(1).getWea());
                    tv_secondary_weather_collection2.setText(getWeatherCollections(weatherInfo.getData().getData().get(1)));
                }
            }
        }
    }

    /**
     * @description 展示场景
     * @param scenes 场景数组
     * @author xulailing
     * @date 2019/12/25 0025 11:56
     **/
    @Override
    public void showScenes(List<Scenes> scenes, int selected) {
        Logger.e("CCC", "showScenes" + scenes);
        mScenes = scenes;
        mAdapter.setNewData(scenes);

        if (scenes.size() > 0) {
            /* 设置选中状态为默认的第一个 */
            mAdapter.setIndex(selected);
            mAdapter.notifyDataSetChanged();

            /* 获取设备IO配置状态，默认第一个 */
            mPresenter.getIoStatus(scenes.get(selected).macAddress);
            sceneSelected = selected;
            macAddress = scenes.get(selected).macAddress;
            sceneName = scenes.get(selected).scene_name;

            /* 获取摄像头配置 */
            mPresenter.getCamsConfig(scenes.get(selected).macAddress);

            /* 获取天气 */
            mPresenter.getWeather(scenes.get(selected).macAddress);
        }
    }

    private List<IoInfoCurrent> ioInfoCurrents = new ArrayList<>();
    private List<IoInfo> mIoInfos;
    @Override
    public void showIoStatus(List<IoInfo> ioInfos) {
        /* 这里创建Event通知TimingPlanFragment List<Scenes> scenes和当前sceneName */
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnSceneInfoEvent(mScenes, macAddress, sceneName)), 500);

        mIoInfos = ioInfos;
        /* 建立websocket */
        initSocketClient();
        /* 开启心跳检测 */
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
    }

    @OnClick(R.id.tv_videoLabel)
    public void onSelectLabel() {
        if (profiles == null || profiles.size() < 2) {
            return;
        }

        PopupWindow popupWindow = new PopupWindow();

        popupWindow.setWidth(getResources().getDimensionPixelSize(R.dimen.dp50));
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getActivity(), R.layout.layout_popup, null);
        LinearLayout ll_label01 = view.findViewById(R.id.ll_label01);
        LinearLayout ll_label02 = view.findViewById(R.id.ll_label02);
        LinearLayout ll_label03 = view.findViewById(R.id.ll_label03);
        TextView tv_label01 = view.findViewById(R.id.tv_label01);
        TextView tv_label02 = view.findViewById(R.id.tv_label02);
        TextView tv_label03 = view.findViewById(R.id.tv_label03);
        popupWindow.setContentView(view);

        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);

        int[] location = new int[2];
        tv_videoLabel.getLocationOnScreen(location);
        if (profiles.size() == 2) {
            ll_label01.setVisibility(View.VISIBLE);
            ll_label02.setVisibility(View.VISIBLE);
            ll_label03.setVisibility(View.GONE);
            tv_label01.setText(profiles.get(0).label);
            tv_label02.setText(profiles.get(1).label);
            popupWindow.showAtLocation(tv_videoLabel, Gravity.NO_GRAVITY, location[0], location[1] - getResources().getDimensionPixelSize(R.dimen.dp75));

            ll_label01.setOnClickListener(v -> {
                popupWindow.dismiss();
                mPresenter.changeProfile(macAddress, camsKey, profiles.get(0).token, profiles.get(0).label);
            });
            ll_label02.setOnClickListener(v -> {
                popupWindow.dismiss();
                mPresenter.changeProfile(macAddress, camsKey, profiles.get(1).token, profiles.get(1).label);
            });
        } else if (profiles.size() == 3) {
            ll_label01.setVisibility(View.VISIBLE);
            ll_label02.setVisibility(View.VISIBLE);
            ll_label03.setVisibility(View.VISIBLE);
            tv_label01.setText(profiles.get(0).label);
            tv_label02.setText(profiles.get(1).label);
            tv_label03.setText(profiles.get(2).label);
            popupWindow.showAtLocation(tv_videoLabel, Gravity.NO_GRAVITY, location[0], location[1] - getResources().getDimensionPixelSize(R.dimen.dp110));

            ll_label01.setOnClickListener(v -> {
                popupWindow.dismiss();
                mPresenter.changeProfile(macAddress, camsKey, profiles.get(0).token, profiles.get(0).label);
            });
            ll_label02.setOnClickListener(v -> {
                popupWindow.dismiss();
                mPresenter.changeProfile(macAddress, camsKey, profiles.get(1).token, profiles.get(1).label);
            });
            ll_label03.setOnClickListener(v -> {
                popupWindow.dismiss();
                mPresenter.changeProfile(macAddress, camsKey, profiles.get(2).token, profiles.get(2).label);
            });
        }
    }

    private void setCamsBgDefault() {
        tv_cams01.setBackgroundResource(R.mipmap.icon_cams_off);
        tv_cams02.setBackgroundResource(R.mipmap.icon_cams_off);
        tv_cams03.setBackgroundResource(R.mipmap.icon_cams_off);
    }

    @OnClick({R.id.tv_cams01, R.id.tv_cams02, R.id.tv_cams03})
    public void onCheckCams(View view) {
        switch (view.getId()) {
            case R.id.tv_cams01:
                if (usableCams != null && usableCams.size() > 0) {
                    setCamsBgDefault();
                    tv_cams01.setBackgroundResource(R.mipmap.icon_cams_on);

                    //设置背景图片
                    Glide.with(getActivity())
                            .load(usableCams.get(0).preview_image)
                            .into(iv_videobg);

                    camsIndex = 0;
                    camsKey = usableCams.get(0).key;
                    profiles = usableCams.get(0).profiles;

                    if (easyPlayerClient != null) {
                        texture_view.setVisibility(View.GONE);
                        easyPlayerClient.stop();
                    }
                    iv_play.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_cams02:
                if (usableCams != null && usableCams.size() > 1) {
                    setCamsBgDefault();
                    tv_cams02.setBackgroundResource(R.mipmap.icon_cams_on);

                    //设置背景图片
                    Glide.with(getActivity())
                            .load(usableCams.get(1).preview_image)
                            .into(iv_videobg);

                    camsIndex = 1;
                    camsKey = usableCams.get(1).key;
                    profiles = usableCams.get(1).profiles;

                    if (easyPlayerClient != null) {
                        texture_view.setVisibility(View.GONE);
                        easyPlayerClient.stop();
                    }
                    iv_play.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_cams03:
                if (usableCams != null && usableCams.size() > 2) {
                    setCamsBgDefault();
                    tv_cams03.setBackgroundResource(R.mipmap.icon_cams_on);

                    //设置背景图片
                    Glide.with(getActivity())
                            .load(usableCams.get(2).preview_image)
                            .into(iv_videobg);

                    camsIndex = 2;
                    camsKey = usableCams.get(2).key;
                    profiles = usableCams.get(2).profiles;

                    if (easyPlayerClient != null) {
                        texture_view.setVisibility(View.GONE);
                        easyPlayerClient.stop();
                    }
                    iv_play.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void displayCamsCount(List<CamsUseable> usable_cams, String passKey, String pass) {
        mPassKey = passKey;
        mPass = pass;
        if (easyPlayerClient != null) {
            texture_view.setVisibility(View.GONE);
            easyPlayerClient.stop();
        }
        iv_play.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        iv_enableAudio.setImageResource(R.mipmap.icon_main_voice_u);
        Logger.e("展示cams个数","size:" + usable_cams.size());
        usableCams = usable_cams;
        /* 展示摄像头个数 */
        if (usable_cams.size() > 0) {
            camsIndex = 0;
            camsKey = usable_cams.get(0).key;
            profiles = usable_cams.get(0).profiles;
            rl_videobg.setVisibility(View.VISIBLE);
            rl_weather.setVisibility(View.GONE);
            if (usable_cams.size() == 1) {
                tv_cams01.setVisibility(View.VISIBLE);
                tv_cams02.setVisibility(View.GONE);
                tv_cams03.setVisibility(View.GONE);
            } else if (usable_cams.size() == 2) {
                tv_cams01.setVisibility(View.VISIBLE);
                tv_cams02.setVisibility(View.VISIBLE);
                tv_cams03.setVisibility(View.GONE);
            } else if (usable_cams.size() == 3) {
                tv_cams01.setVisibility(View.VISIBLE);
                tv_cams02.setVisibility(View.VISIBLE);
                tv_cams03.setVisibility(View.VISIBLE);
            }

            Glide.with(getActivity())
                    .load(usable_cams.get(0).preview_image)
                    .into(iv_videobg);

            /* 请求推流 */
//            mPresenter.doCamsPlay(macAddress, usable_cams, usable_cams.get(0).key, 0);
        } else {
            /* 展示天气预报 */
            rl_videobg.setVisibility(View.GONE);
            rl_weather.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showVLCVideo(List<CamsUseable> usable_cams, String playKey, int camsIndex) {
        camsKey = playKey;
        //设置背景图片
        Glide.with(getActivity())
                .load(usable_cams.get(camsIndex).preview_image)
                .into(iv_videobg);

        profiles = usable_cams.get(camsIndex).profiles;
        String rtsp_url = "";
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).selected) {
                rtsp_url = profiles.get(i).rtsp_url;
                tv_videoLabel.setText(profiles.get(i).label);
            }
        }
        //播放rtsp_url
        Logger.e("播放地址","rtspUrl:" + rtsp_url);
//        vlcMediaView.setVisibility(View.VISIBLE);
//        vlcMediaView.playVideo(rtsp_url);

        texture_view.setVisibility(View.VISIBLE);
        /**
         * 参数说明
         * 第一个参数为Context,第二个参数为KEY
         * 第三个参数为的textureView,用来显示视频画面
         * 第四个参数为一个ResultReceiver,用来接收SDK层发上来的事件通知;
         * 第五个参数为I420DataCallback,如果不为空,那底层会把YUV数据回调上来.
         */
        easyPlayerClient = new EasyPlayerClient(getActivity(), BuildConfig.RTSP_KEY, texture_view, mResultReceiver, null);
        progress.setVisibility(View.VISIBLE);
        easyPlayerClient.play(rtsp_url);
        easyPlayerClient.setAudioEnable(false);
    }

    @Override
    public void showVLCVideoLabel(String label) {
        tv_videoLabel.setText(label);
    }

    @Subscribe
    public void onEventReceived(OnGetScenesEvent event) {
        mPresenter.getScenes(event.selected);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra("QRCODE_RESULT");
                //获取fishMac值
                String fishMac = "";
                if (content.contains("/#")) {
                    fishMac = content.substring(content.lastIndexOf("/#") + 2);
                }
                Logger.e("扫码获取到的mac", fishMac);
                if(StringUtils.isEmpty(fishMac)){
                    Toaster.showShort("请扫描正确的二维码");
                } else {
                    showRenameDialog(fishMac, "渔场", 1);
//                    //添加场景
//                    mPresenter.addScenes(fishMac, "渔场");
                }
            }
        }
    }

    /**
     * 扫码成功后让用户输入鱼塘名称
     * @param fishMac 鱼塘唯一标识
     * @param sceneName 名称
     * @param type 1:添加 2：重命名
     */
    private void showRenameDialog(String fishMac, String sceneName, int type) {
        ScenesRenameDialog scenesRenameDialog = new ScenesRenameDialog(getActivity(), sceneName, R.style.MyDialog);
        scenesRenameDialog.setCancelable(false);
        scenesRenameDialog.setOnResultListener(new ScenesRenameDialog.OnResultListener() {
            @Override
            public void Ok(String bark) {
                if (StringUtils.isTrimEmpty(bark)) {
                    Toaster.showShort("请输入渔场名称");
                } else {
                    if (type == 1) {
                        //添加场景
                        mPresenter.addScenes(fishMac, bark);
                    } else {
                        //重命名场景
                        mPresenter.renameScenes(fishMac, bark);
                    }
                    scenesRenameDialog.dismiss();
                }
            }

            @Override
            public void Cancel() {
                scenesRenameDialog.dismiss();
            }
        });
        scenesRenameDialog.show();
    }

    private JWebSocketClient client;
    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Logger.d("JWebSocketClientService", "心跳包检测websocket连接状态");
            if (client != null) {
                if (client.isClosed()) {
                    reconnectWs();
                }
            } else {
                //如果client已为空，重新初始化连接
                client = null;
                initSocketClient();
            }
            //每隔一定的时间，对长连接进行一次心跳检测
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    private void initSocketClient() {
        URI uri = URI.create(WEBSOCKET_URI);
        client = new JWebSocketClient(uri) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                Logger.i("JWebSocketClientService", "websocket连接成功");

                getActivity().runOnUiThread(() -> {
                    //客户端可以发消息给服务端
                });
            }

            @Override
            public void onMessage(String message) {
                //message就是接收到的消息
                Logger.i("接收到的消息", message);

                getActivity().runOnUiThread(() -> {
                    Gson gson = new Gson();
                    WebSocketInfo webSocketInfo = gson.fromJson(message, WebSocketInfo.class);
                    if (webSocketInfo.device_mac.equals(macAddress)) {
                        IoStatusAll ioStatusAll = webSocketInfo.data;
                        Logger.d("onMessage", "水温：" + ioStatusAll.water_temperature + ",PH：" + ioStatusAll.ph + ",溶氧量：" + ioStatusAll.o2);

                        if (ioStatusAll.water_temperature == 0) {
                            tv_temperature.setText("无");
                        } else {
                            tv_temperature.setText(ioStatusAll.water_temperature + "℃");
                        }
                        if (ioStatusAll.ph == 0) {
                            tv_ph.setText("无");
                        } else {
                            tv_ph.setText(ioStatusAll.ph + "");
                        }
                        if (ioStatusAll.o2 == 0) {
                            tv_oxygen.setText("无");
                        } else {
                            tv_oxygen.setText(ioStatusAll.o2 + "");
                        }

                        setStatusColor(ioStatusAll.water_temperature, ioStatusAll.ph, ioStatusAll.o2);

                        /* 实时状态IO数组 */
                        List<IoStatus> ioStatuses = webSocketInfo.data.status;

                        ioInfoCurrents.clear();//先清空组装的数据
                        for (int i = 0; i < mIoInfos.size(); i++) {
                            IoInfoCurrent ioInfoCurrent
                                    = new IoInfoCurrent(mIoInfos.get(i).enabled,
                                    mIoInfos.get(i).code,
                                    mIoInfos.get(i).type,
                                    mIoInfos.get(i).name,
                                    mIoInfos.get(i).weight_per_second,
                                    mIoInfos.get(i).pin,
                                    mIoInfos.get(i).power_w,
                                    ioStatuses.get(i).opened,
                                    ioStatuses.get(i).duration,
                                    ioStatuses.get(i).start_time);
                            ioInfoCurrents.add(ioInfoCurrent);
                        }
                        Logger.d("组装的数据", ioInfoCurrents.toString() + "");

                        for (int i = 0; i < ioInfoCurrents.size(); i++) {
                            if (!ioInfoCurrents.get(i).enabled) {
                                ioInfoCurrents.remove(ioInfoCurrents.get(i));
                            }
                        }

                        ioAdapter.setNewData(ioInfoCurrents);
                    }
                });

            }
        };

        connect();
    }

    /**
     * 根据实时的数据动态设置属性颜色
     * @param water_temperature 水温
     * @param ph PH值
     * @param o2 溶氧量
     */
    private void setStatusColor(double water_temperature, double ph, double o2) {
        /**
         * 水温颜色动态设置
         */
        GradientDrawable drawableWater = new GradientDrawable();
        drawableWater.setShape(GradientDrawable.OVAL);
        if (water_temperature <= 0) {
            drawableWater.setColor(getActivity().getResources().getColor(R.color.temperature00));
        } else if (water_temperature <= 15) {
            drawableWater.setColor(getActivity().getResources().getColor(R.color.temperature15));
        } else if (water_temperature <= 26) {
            drawableWater.setColor(getActivity().getResources().getColor(R.color.temperature26));
        } else {
            drawableWater.setColor(getActivity().getResources().getColor(R.color.temperature27));
        }
        ll_temperature.setBackground(drawableWater);

        /**
         * PH值颜色动态设置
         */
        GradientDrawable drawablePH = new GradientDrawable();
        drawablePH.setShape(GradientDrawable.OVAL);
        if (ph < 4.6) {
            drawablePH.setColor(getActivity().getResources().getColor(R.color.ph_46));
        } else if (ph < 5.6) {
            drawablePH.setColor(getActivity().getResources().getColor(R.color.ph_56));
        } else if (ph < 6.4) {
            drawablePH.setColor(getActivity().getResources().getColor(R.color.ph_64));
        }  else if (ph < 6.9) {
            drawablePH.setColor(getActivity().getResources().getColor(R.color.ph_69));
        } else if (ph < 7.4) {
            drawablePH.setColor(getActivity().getResources().getColor(R.color.ph_74));
        } else if (ph < 7.9) {
            drawablePH.setColor(getActivity().getResources().getColor(R.color.ph_79));
        } else if (ph < 8.8) {
            drawablePH.setColor(getActivity().getResources().getColor(R.color.ph_88));
        } else if (ph < 9.3) {
            drawablePH.setColor(getActivity().getResources().getColor(R.color.ph_93));
        } else if (ph < 9.8) {
            drawablePH.setColor(getActivity().getResources().getColor(R.color.ph_98));
        } else {
            drawablePH.setColor(getActivity().getResources().getColor(R.color.ph_99));
        }
        ll_ph.setBackground(drawablePH);

        /**
         * 溶氧量颜色动态设置
         */
        GradientDrawable drawableO2 = new GradientDrawable();
        drawableO2.setShape(GradientDrawable.OVAL);
        if (o2 < 1.5) {
            drawableO2.setColor(getActivity().getResources().getColor(R.color.o2_00));
        } else if (o2 < 4) {
            drawableO2.setColor(getActivity().getResources().getColor(R.color.o2_04));
        } else if (o2 < 8) {
            drawableO2.setColor(getActivity().getResources().getColor(R.color.o2_08));
        } else {
            drawableO2.setColor(getActivity().getResources().getColor(R.color.o2_09));
        }
        ll_oxygen.setBackground(drawableO2);
    }

    /**
     * 连接websocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        if (null != client) {
            Logger.i("JWebSocketClientService", "发送的消息：" + msg);
            client.send(msg);
        }
    }

    /**
     * 断开连接
     */
    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Logger.i("JWebSocketClientService", "开启重连");
                    client.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(heartBeatRunnable);
        closeConnect();
        super.onDestroy();
    }
}
