package com.ypcxpt.fish.main.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.RippleLayout;
import com.ypcxpt.fish.app.util.VpSwipeRefreshLayout;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.view.fragment.BaseFragment;
import com.ypcxpt.fish.main.adapter.SceneAdapter;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.event.OnBluetoothPreparedEvent;
import com.ypcxpt.fish.main.event.OnGetScenesEvent;
import com.ypcxpt.fish.main.event.OnMainPagePermissionResultEvent;
import com.ypcxpt.fish.main.event.OnProfileUpdatedEvent;
import com.ypcxpt.fish.main.model.WeatherInfo;
import com.ypcxpt.fish.main.presenter.MyDevicePresenter;
import com.ypcxpt.fish.main.presenter.WeatherPresenter;
import com.ypcxpt.fish.main.util.MainOperationDialog;
import com.yzq.zxinglibrary.common.Constant;

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

    @BindView(R.id.swipe_refresh_layout)
    VpSwipeRefreshLayout swipe_refresh_layout;

    private MyDeviceContract.Presenter mPresenter;

    private WeatherPresenter mWeatherPresenter;

    private SceneAdapter mAdapter;

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

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.e("ppp", position + "");
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.e("p", position + "");
            }
        });

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
            }

            @Override
            public void Remove() {
                operationDialog.dismiss();
            }

            @Override
            public void Rename() {
                operationDialog.dismiss();
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

    @Override
    public void showScenes(List<Scenes> scenes) {
        Logger.e("CCC", "showScenes" + scenes);
        mAdapter.setNewData(scenes);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(Object event) {
        if (event instanceof OnProfileUpdatedEvent) {
//            if (((OnProfileUpdatedEvent) event).userProfile.scenes != null) {
//                mAdapter.setNewData(((OnProfileUpdatedEvent) event).userProfile.scenes);
//            }
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
    public void onEventReceived(OnGetScenesEvent event) {
        mPresenter.getScenes();
    }
}
