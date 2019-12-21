package com.ypcxpt.fish.main.view.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.VpSwipeRefreshLayout;
import com.ypcxpt.fish.main.model.Scenes;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.view.fragment.BaseFragment;
import com.ypcxpt.fish.main.adapter.SceneAdapter;
import com.ypcxpt.fish.main.contract.EarlyWarningContract;
import com.ypcxpt.fish.main.event.OnMainPagePermissionResultEvent;
import com.ypcxpt.fish.main.presenter.EarlyWarningPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class EarlyWarningFragment extends BaseFragment implements EarlyWarningContract.View {
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

    @BindView(R.id.swipe_refresh_layout)
    VpSwipeRefreshLayout swipe_refresh_layout;

    @BindView(R.id.tv_timeStatus) TextView tv_timeStatus;
    @BindView(R.id.tv_desc) TextView tv_desc;

    private EarlyWarningContract.Presenter mPresenter;

    private SceneAdapter mAdapter;

    @Override
    protected int layoutResID() {
        return R.layout.fragment_early_warning;
    }

    @Override
    protected void initData() {
        mPresenter = new EarlyWarningPresenter();
        addPresenter(mPresenter);
    }

    @Override
    protected void initViews() {
//        mAdapter = new SceneAdapter(R.layout.item_scenes, mPresenter, getActivity());
//        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rv.setAdapter(mAdapter);
//        ((DefaultItemAnimator) rv.getItemAnimator()).setSupportsChangeAnimations(false);
//        rv.getItemAnimator().setChangeDuration(0);// 通过设置动画执行时间为0来解决闪烁问题
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//        mPresenter.acceptData("mAdapter", mAdapter);

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//设置刷新监听器
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {//模拟耗时操作
                    @Override
                    public void run() {
                        swipe_refresh_layout.setRefreshing(false);//取消刷新
                        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnMainPagePermissionResultEvent()), 750);
//                        getBanner();
                    }
                }, 2000);
            }
        });
        //设置刷新时旋转图标的颜色，这是一个可变参数，当设置多个颜色时，旋转一周改变一次颜色。
        swipe_refresh_layout.setColorSchemeResources(R.color.main_color_new, R.color.bg_device_detail_yellow, R.color.bg_device_detail_top);

//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.include_footview_scan, null);
//        RelativeLayout rl_manually_scan = view.findViewById(R.id.rl_manually_scan);
//        rl_manually_scan.setOnClickListener(v -> {
//            /* 并不是直接开始扫描，而是先检查蓝牙是否开启 */
//            checkBluetoothState();
//        });
//        mAdapter.addFooterView(view);

//        setTopData();
    }

    @Override
    public void showScenes(List<Scenes> scenes) {

    }

}
