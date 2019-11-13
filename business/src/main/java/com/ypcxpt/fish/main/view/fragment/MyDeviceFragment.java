package com.ypcxpt.fish.main.view.fragment;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
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
import com.ypcxpt.fish.main.event.OnProfileUpdatedEvent;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.WeatherInfo;
import com.ypcxpt.fish.main.presenter.MyDevicePresenter;
import com.ypcxpt.fish.main.presenter.WeatherPresenter;
import com.ypcxpt.fish.main.util.MainOperationDialog;
import com.ypcxpt.fish.main.util.ScenesRenameDialog;
import com.ypcxpt.fish.main.view.activity.CaptureScanActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
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

    public static String macAddress;
    @Override
    public void showScenes(List<Scenes> scenes) {
        Logger.e("CCC", "showScenes" + scenes);
        mAdapter.setNewData(scenes);

        if (scenes.size() > 0) {
            /* 获取设备IO，默认第一个 */
            mPresenter.getIoinfos(scenes.get(0).macAddress);
            macAddress = scenes.get(0).macAddress;

            mAdapter.setIndex(0);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showIoInfos(List<IoInfo> ioInfos) {
        for (int i = 0; i < ioInfos.size(); i++) {
            if (!ioInfos.get(i).enabled) {
                ioInfos.remove(ioInfos.get(i));
            }
        }
        ioAdapter.setNewData(ioInfos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(Object event) {
        if (event instanceof OnProfileUpdatedEvent) {
//            if (((OnProfileUpdatedEvent) event).userProfile.scenes != null) {
//                mAdapter.setNewData(((OnProfileUpdatedEvent) event).userProfile.scenes);
//            }
        }
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
}
