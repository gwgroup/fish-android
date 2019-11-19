package com.ypcxpt.fish.main.view.fragment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.VpSwipeRefreshLayout;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.device.model.Scenes;
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
        }
    }

    private List<IoInfoCurrent> ioInfoCurrents = new ArrayList<>();
    private List<IoInfo> mIoInfos;
    @Override
    public void showIoStatus(List<IoInfo> ioInfos) {
        /* 这里创建Event通知TimingPlanFragment List<Scenes> scenes和当前sceneName */
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnSceneInfoEvent(mScenes, sceneName)), 500);

        mIoInfos = ioInfos;
        /* 建立websocket */
        initSocketClient();
        /* 开启心跳检测 */
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
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
                        tv_temperature.setText(ioStatusAll.water_temperature + "℃");
                        tv_ph.setText(ioStatusAll.ph + "");
                        tv_oxygen.setText(ioStatusAll.o2 + "");

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
