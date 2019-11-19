package com.ypcxpt.fish.main.view.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.VpSwipeRefreshLayout;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.view.fragment.BaseFragment;
import com.ypcxpt.fish.main.adapter.SceneAdapter;
import com.ypcxpt.fish.main.contract.TimingPlanContract;
import com.ypcxpt.fish.main.event.OnGetScenesEvent;
import com.ypcxpt.fish.main.event.OnMainPagePermissionResultEvent;
import com.ypcxpt.fish.main.event.OnSceneInfoEvent;
import com.ypcxpt.fish.main.presenter.TimingPlanPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

public class TimingPlanFragment extends BaseFragment implements TimingPlanContract.View {
    @BindView(R.id.tv_scene)
    TextView tv_scene;
    @BindView(R.id.iv_arrow)
    ImageView iv_arrow;

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.swipe_refresh_layout)
    VpSwipeRefreshLayout swipe_refresh_layout;

    private List<Scenes> mScenes;

    private TimingPlanContract.Presenter mPresenter;

    private SceneAdapter mAdapter;

    @Override
    protected int layoutResID() {
        return R.layout.fragment_timing_plan;
    }

    @Override
    protected void initData() {
        mPresenter = new TimingPlanPresenter();
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
    }

    @Override
    public void showScenes(List<Scenes> scenes) {

    }

    @Subscribe
    public void onEventReceived(OnGetScenesEvent event) {
        mPresenter.getScenes();
    }

    @Subscribe
    public void onEventReceived(OnSceneInfoEvent event) {
        tv_scene.setText(event.sceneName);
        mScenes = event.scenes;
    }
}
