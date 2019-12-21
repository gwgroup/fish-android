package com.ypcxpt.fish.main.view.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.VpSwipeRefreshLayout;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.main.event.OnScreenEvent;
import com.ypcxpt.fish.main.model.Scenes;
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
import com.ypcxpt.fish.main.model.CamsUseable;
import com.ypcxpt.fish.main.model.CamsUseableProfiles;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.IoInfoCurrent;
import com.ypcxpt.fish.main.model.IoStatus;
import com.ypcxpt.fish.main.model.IoStatusAll;
import com.ypcxpt.fish.main.model.WeatherInfo;
import com.ypcxpt.fish.main.model.WebSocketInfo;
import com.ypcxpt.fish.main.presenter.MyDevicePresenter;
import com.ypcxpt.fish.main.presenter.WeatherPresenter;
import com.ypcxpt.fish.main.util.JWebSocketClient;
import com.ypcxpt.fish.main.util.MainOperationDialog;
import com.ypcxpt.fish.main.util.ScenesRenameDialog;
import com.ypcxpt.fish.main.view.activity.CaptureScanActivity;

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
    @BindView(R.id.iv_weather)
    ImageView ivWeather;
    @BindView(R.id.tv_curr_temp)
    TextView tvTemp;
    @BindView(R.id.tv_weather_info)
    TextView tvWeather;
    @BindView(R.id.tv_secondary_weather_collection)
    TextView tvLeftWeatherInfo;

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

    @BindView(R.id.tv_videoLabel)
    TextView tv_videoLabel;

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

    private WeatherPresenter mWeatherPresenter;

    private SceneAdapter mAdapter;
    private IOAdapter ioAdapter;

    private int REQUEST_CODE_SCAN = 111;

    /* 可用的摄像头 */
    private List<CamsUseable> usableCams;
    private String camsKey;
    /* 每个摄像头中的播放信息(清晰度) */
    private List<CamsUseableProfiles> profiles;

    private EasyPlayerClient easyPlayerClient;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
//            getSupportActionBar().hide();
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
//            getSupportActionBar().show();
            rl_hide01.setVisibility(View.VISIBLE);
            ll_hide02.setVisibility(View.VISIBLE);
            rv_io.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_videobg.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = getResources().getDimensionPixelSize(R.dimen.dp200);
//            params.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics()));;
            rl_videobg.setLayoutParams(params);

            EventBus.getDefault().post(new OnScreenEvent(false));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            //隐藏的操作
//            vlcMediaView.stopVideo(false);

        } else {
//            vlcMediaView.stopVideo(false);
//            //显示的操作
//            vlcMediaView.onAttached(getActivity());
            /* 获取摄像头配置 */
            mPresenter.getCamsConfig(macAddress);
        }
    }

    @Override
    protected int layoutResID() {
        return R.layout.fragment_my_device;
    }

    @Override
    protected void initData() {
        mPresenter = new MyDevicePresenter();
//        mWeatherPresenter = new WeatherPresenter();
        addPresenter(mPresenter);
//        addPresenter(mWeatherPresenter);
    }

    @Override
    protected void initViews() {
//        vlcMediaView.onAttached(getActivity());
        /**
         * 参数说明
         * 第一个参数为Context,第二个参数为KEY
         * 第三个参数为的textureView,用来显示视频画面
         * 第四个参数为一个ResultReceiver,用来接收SDK层发上来的事件通知;
         * 第五个参数为I420DataCallback,如果不为空,那底层会把YUV数据回调上来.
         */
        easyPlayerClient = new EasyPlayerClient(getActivity(), "6D75724D7A4A36526D343241646274646F6B534B512B5A76636D63755A57467A65575268636E64706269356C59584E356347786865575679567778576F502F44346B566863336C4559584A33615735555A57467453584E55614756435A584E30497A49774D546B355A57467A65513D3D", texture_view, null, null);

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

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//设置刷新监听器
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {//模拟耗时操作
                    @Override
                    public void run() {
                        swipe_refresh_layout.setRefreshing(false);//取消刷新
                        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnMainPagePermissionResultEvent()), 750);
                    }
                }, 2000);
            }
        });
        //设置刷新时旋转图标的颜色，这是一个可变参数，当设置多个颜色时，旋转一周改变一次颜色。
        swipe_refresh_layout.setColorSchemeResources(R.color.main_color_new, R.color.bg_device_detail_yellow, R.color.bg_device_detail_top);
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
                Router.build(Path.Main.IO_CONFIG).withString("DEVICE_MAC", macAddress).navigation(getActivity());
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
        if (weatherInfo == null) {
//            tvLocation.setText("未定位");
        } else {
//            tvLocation.setText(weatherInfo.city);
            ivWeather.setImageResource(getWeatherIcon(weatherInfo));
            tvTemp.setText(weatherInfo.currTemp + "℃");
            tvWeather.setText(weatherInfo.getTodayWeather().weather);
            tvLeftWeatherInfo.setText(getWeatherCollections(weatherInfo));
        }
    }

    private List<Scenes> mScenes;
    public static String macAddress;//场景mac
    public static String sceneName;//场景名称
    @Override
    public void showScenes(List<Scenes> scenes) {
        Logger.e("CCC", "showScenes" + scenes);
        mScenes = scenes;
        mAdapter.setNewData(scenes);

        if (scenes.size() > 0) {
            /* 设置选中状态为默认的第一个 */
            mAdapter.setIndex(0);
            mAdapter.notifyDataSetChanged();

            /* 获取设备IO配置状态，默认第一个 */
            mPresenter.getIoStatus(scenes.get(0).macAddress);
            macAddress = scenes.get(0).macAddress;
            sceneName = scenes.get(0).scene_name;

            /* 获取摄像头配置 */
            mPresenter.getCamsConfig(scenes.get(0).macAddress);
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

                    camsKey = usableCams.get(0).key;
                    profiles = usableCams.get(0).profiles;

                    /* 请求推流播放rtsp_url */
                    mPresenter.doCamsPlay(macAddress, usableCams, camsKey, 0);
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

                    camsKey = usableCams.get(1).key;
                    profiles = usableCams.get(1).profiles;

                    /* 请求推流播放rtsp_url */
                    mPresenter.doCamsPlay(macAddress, usableCams, camsKey, 1);
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

                    camsKey = usableCams.get(2).key;
                    profiles = usableCams.get(2).profiles;

                    /* 请求推流播放rtsp_url */
                    mPresenter.doCamsPlay(macAddress, usableCams, camsKey, 2);
                }
                break;
        }
    }

    @Override
    public void displayCamsCount(List<CamsUseable> usable_cams) {
        easyPlayerClient.stop();
        Logger.e("展示cams个数","size:" + usable_cams.size());
        usableCams = usable_cams;
        /* 展示摄像头个数 */
        if (usable_cams.size() > 0) {
            camsKey = usable_cams.get(0).key;
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

            /* 请求推流 */
            mPresenter.doCamsPlay(macAddress, usable_cams, usable_cams.get(0).key, 0);
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
        easyPlayerClient.play(rtsp_url);
    }

    @Subscribe
    public void onEventReceived(OnGetScenesEvent event) {
        mPresenter.getScenes();
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
