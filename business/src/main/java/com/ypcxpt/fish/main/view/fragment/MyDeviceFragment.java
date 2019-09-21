package com.ypcxpt.fish.main.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.ms.banner.Banner;
import com.ms.banner.holder.BannerViewHolder;
import com.ms.banner.holder.HolderCreator;
import com.ms.banner.listener.OnBannerClickListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import com.ypcxpt.fish.BuildConfig;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.FileUtil;
import com.ypcxpt.fish.app.util.RippleLayout;
import com.ypcxpt.fish.app.util.TimeUtil;
import com.ypcxpt.fish.app.util.VpSwipeRefreshLayout;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.view.fragment.BaseFragment;
import com.ypcxpt.fish.main.adapter.DetectedDeviceAdapter;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.event.OnBluetoothPreparedEvent;
import com.ypcxpt.fish.main.event.OnGetDeviceListEvent;
import com.ypcxpt.fish.main.event.OnGetDevicesEvent;
import com.ypcxpt.fish.main.event.OnMainPagePermissionResultEvent;
import com.ypcxpt.fish.main.event.OnProfileUpdatedEvent;
import com.ypcxpt.fish.main.model.BannerInfo;
import com.ypcxpt.fish.main.model.WeatherInfo;
import com.ypcxpt.fish.main.presenter.MyDevicePresenter;
import com.ypcxpt.fish.main.presenter.WeatherPresenter;
import com.ypcxpt.fish.sonic.BrowserActivity;
import com.ypcxpt.fish.sonic.SonicJavaScriptInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.ypcxpt.fish.app.util.DisplayUtils.getWeatherCollections;
import static com.ypcxpt.fish.app.util.DisplayUtils.getWeatherIcon;
import static com.ypcxpt.fish.sonic.BrowserActivity.MODE_SONIC;

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
    @BindView(R.id.tv_location)
    TextView tvLocation;

    @BindView(R.id.iv_manually_scan)
    ImageView iv;
    @BindView(R.id.iv_code_scan)
    ImageView iv_code_scan;

    //版本号
    @BindView(R.id.tv_version)
    TextView tv_version;

    @BindView(R.id.swipe_refresh_layout)
    VpSwipeRefreshLayout swipe_refresh_layout;

    @BindView(R.id.banner) Banner banner;

    @BindView(R.id.tv_timeStatus) TextView tv_timeStatus;
    @BindView(R.id.tv_desc) TextView tv_desc;

    private MyDeviceContract.Presenter mPresenter;

    private WeatherPresenter mWeatherPresenter;

    private DetectedDeviceAdapter mAdapter;

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

        tv_version.setText("v" + getAPKVersion(getActivity()));
    }

//    @OnClick(R.id.tv_version)
//    public void onBack() {
//        Router.build(Path.Device.T100)
//                .withString("characteristicUuid", "0000ff02-0000-1000-8000-00805f9b34fb")
//                .withString("macAddress", "78:04:73:CA:5B:8A")
//                .withString("chairName", "T100")
//                .navigation(getActivity());
//    }

    @Override
    protected void initViews() {
        mAdapter = new DetectedDeviceAdapter(R.layout.item_detected_deivce, mPresenter, getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(mAdapter);
        ((DefaultItemAnimator) rv.getItemAnimator()).setSupportsChangeAnimations(false);
        rv.getItemAnimator().setChangeDuration(0);// 通过设置动画执行时间为0来解决闪烁问题
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mPresenter.acceptData("mAdapter", mAdapter);

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//设置刷新监听器
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {//模拟耗时操作
                    @Override
                    public void run() {
                        swipe_refresh_layout.setRefreshing(false);//取消刷新
                        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnMainPagePermissionResultEvent()), 750);
                        getBanner();
                    }
                }, 2000);
            }
        });
        //设置刷新时旋转图标的颜色，这是一个可变参数，当设置多个颜色时，旋转一周改变一次颜色。
        swipe_refresh_layout.setColorSchemeResources(R.color.main_color_new, R.color.bg_device_detail_yellow, R.color.bg_device_detail_top);

        getBanner();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.include_footview_scan, null);
        RelativeLayout rl_manually_scan = view.findViewById(R.id.rl_manually_scan);
        rl_manually_scan.setOnClickListener(v -> {
            /* 并不是直接开始扫描，而是先检查蓝牙是否开启 */
            checkBluetoothState();
        });
        mAdapter.addFooterView(view);

        setTopData();
    }

    private void setTopData() {
        // Date d = new Date();
        // d.getHours()方法过时，使用Calendar代替
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 11) {
            tv_timeStatus.setText("早上好！");
        } else if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 13) {
            tv_timeStatus.setText("中午好！");
        } else if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 18) {
            tv_timeStatus.setText("下午好！");
        } else if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 24) {
            tv_timeStatus.setText("晚上好！");
        }
        tv_desc.setText("累了吧！使用按摩椅休息一下吧！");
    }

    private void getBanner() {
        String url = BuildConfig.BASE_URL + "article/get_banner";
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.i("banner", "bannerJson-->" + response);
                Gson gson = new Gson();
                BannerInfo bannerInfo = gson.fromJson(response, BannerInfo.class);
                if (bannerInfo.getData() == null || bannerInfo.getData().size() <= 0) {
                    banner.setVisibility(View.GONE);
                } else {
                    banner.setVisibility(View.VISIBLE);
                    showBanner(bannerInfo.getData());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e("VolleyError", "errorMessage-->" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("", "");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 user-token:AEUHY98QIASUDH
                headers.put("authorization", AppData.token());
                return headers;
            }
        };
        mQueue.add(stringRequest);
    }

    String imageUrl = "";
    private void showBanner(List<BannerInfo.DataBean> data) {
        banner.setAutoPlay(true)
                .setPages(data, new HolderCreator<BannerViewHolder>() {
                    @Override
                    public BannerViewHolder createViewHolder() {
                        return new BannerViewHolder<BannerInfo.DataBean>() {
                            ImageView iv_banner;
                            TextView tv_name;
                            TextView tv_time;
                            TextView tv_type;

                            @Override
                            public View createView(Context context) {
                                View view = LayoutInflater.from(context).inflate(R.layout.item_banner, null);
                                iv_banner = view.findViewById(R.id.iv_banner);
                                tv_name = view.findViewById(R.id.tv_name);
                                tv_time = view.findViewById(R.id.tv_time);
                                tv_type = view.findViewById(R.id.tv_type);
                                iv_banner.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                return view;
                            }

                            @Override
                            public void onBind(Context context, int position, BannerInfo.DataBean data) {
                                tv_name.setText(data.getTitle());
                                tv_time.setText("发布于" + TimeUtil.getTimeFormatText(data.getCreate_time()));
                                Gson gson = new Gson();
                                if (!StringUtils.isTrimEmpty(data.getSmall_image())) {
                                    BannerInfo.ImageBean bannerInfo = gson.fromJson(data.getSmall_image(), BannerInfo.ImageBean.class);
                                    Glide.with(context.getApplicationContext())
                                            .load(bannerInfo.getUrl())
                                            .into(iv_banner);
                                    imageUrl = bannerInfo.getUrl();
                                } else {
                                    Glide.with(context.getApplicationContext())
                                            .load(R.mipmap.ic_company_logo)
                                            .into(iv_banner);
                                }
                            }
                        };
                    }
                })
                .setOnBannerClickListener(new OnBannerClickListener() {
                    @Override
                    public void onBannerClick(int position) {
                        BannerInfo.DataBean dataBean = data.get(position);
                        String url = "https://smart.reead.net/article/index.html?token=" + AppData.token() + "&id=" + dataBean.getId();

                        if (FileUtil.isFastClick()) {
                            // 进行点击事件后的逻辑操作，防止连续点击
                            startBrowserActivity(url, dataBean, MODE_SONIC);
                        }

                    }
                })
                .setDelayTime(3000)
                .start();
    }

    private void startBrowserActivity(String webUrl, BannerInfo.DataBean dataBean, int mode) {
        Intent intent = new Intent(getActivity(), BrowserActivity.class);
        intent.putExtra("TITLE", "文章详情");
        intent.putExtra("ID_DATA", dataBean.getId());
        intent.putExtra("TITLE_DATA", dataBean.getTitle());
        intent.putExtra("IMAGE_DATA", imageUrl);
        intent.putExtra(BrowserActivity.PARAM_URL, webUrl);
        intent.putExtra(BrowserActivity.PARAM_MODE, mode);
        intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
        startActivityForResult(intent, -1);
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @OnClick(R.id.iv_code_scan)
    public void onClickCodeScan() {
        AndPermission.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Intent intent = new Intent(getActivity(), CaptureActivity.class);
                        /**
                         * ZxingConfig是配置类
                         * 可以设置是否显示底部布局，闪光灯，相册，
                         * 是否播放提示音  震动
                         * 设置扫描框颜色等
                         * 也可以不传这个参数
                         */
//                                ZxingConfig config = new ZxingConfig();
//                                config.setPlayBeep(false);//是否播放扫描声音 默认为true
//                                config.setShake(false);//是否震动  默认为true
//                                config.setDecodeBarCode(false);//是否扫描条形码 默认为true
//                                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
//                                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
//                                config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
//                                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
//                                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                        startActivityForResult(intent, REQUEST_CODE_SCAN);
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

    @OnClick(R.id.iv_manually_scan)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_manually_scan:
                /* 并不是直接开始扫描，而是先检查蓝牙是否开启 */
                checkBluetoothState();
                break;
        }
    }

    private void checkBluetoothState() {
        mPresenter.checkBluetoothState();
    }

    @Override
    public void onStopScan() {
//        dismissLoading();
        dialog.dismiss();
    }

    @Override
    public void onScanFailure() {
        dismissLoading();
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

    @Override
    public void showDevices(List<NetDevice> devices) {
        Logger.e("CCC", "showDevices" + devices);
        mAdapter.setNewData(devices);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(Object event) {
        if (event instanceof OnBluetoothPreparedEvent) {
            /* 状态已准备好，可以开始扫描 */
//            showLoading();
            mPresenter.startScan(true);

            showDialog();
        } else if (event instanceof OnGetDeviceListEvent) {
            List<NetDevice> deviceList = ((OnGetDeviceListEvent) event).netDeviceList;
            mAdapter.setNewData(deviceList);
        } else if (event instanceof OnProfileUpdatedEvent) {
            if (!StringUtils.isTrimEmpty(((OnProfileUpdatedEvent) event).userProfile.name)) {
                tvLocation.setText(((OnProfileUpdatedEvent) event).userProfile.name + "的家");
            } else {
                tvLocation.setText("我的家");
            }
        }
    }

    Dialog dialog;
    private void showDialog() {
        dialog = new Dialog(getActivity(), R.style.MyDialog);
        dialog.show();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View viewDialog = inflater.inflate(R.layout.dialog_layout_search, null);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        dialog.setContentView(viewDialog, layoutParams);
        RippleLayout ripple_layout = viewDialog.findViewById(R.id.ripple_layout);
        ripple_layout.startRippleAnimation();
    }

    @Subscribe
    public void onEventReceived(OnGetDevicesEvent event) {
        mPresenter.getDevices();
    }

    /**
     * 取得应用的版本号
     */
    public static String getAPKVersion(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                String type = content.substring(content.indexOf("net/") + 4, content.indexOf("/#"));
                String macAddress = content.substring(content.indexOf("#") + 1);
                Toaster.showShort(type + "-" + macAddress);

                mPresenter.startCodeScan(type, macAddress);
            }
        }
    }
}
