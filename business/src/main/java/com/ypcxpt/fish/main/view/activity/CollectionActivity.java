package com.ypcxpt.fish.main.view.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.main.adapter.CollectionAdapter;
import com.ypcxpt.fish.main.contract.CollectionContract;
import com.ypcxpt.fish.main.event.OnGetCollectionsEvent;
import com.ypcxpt.fish.main.model.CollectionInfo;
import com.ypcxpt.fish.main.presenter.CollectionPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = Path.Main.COLLECTION)
public class CollectionActivity extends BaseActivity implements CollectionContract.View {
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rv) RecyclerView rv;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipe_refresh_layout;

    private CollectionContract.Presenter mPresenter;
    private CollectionAdapter mAdapter;

    //收藏数据源
    private CollectionInfo collection;
    private int pageIndex = 1;
    private int pageNum = 10;

    @Override
    protected int layoutResID() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initData() {
        mPresenter = new CollectionPresenter();
        addPresenter(mPresenter);
    }

    @Override
    protected void initViews() {
        tv_title.setText("我的收藏");
        mAdapter = new CollectionAdapter(R.layout.item_collection, mPresenter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        mAdapter.bindToRecyclerView(rv);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMore();
                    }
                },1500);
            }
        }, rv);
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setEmptyView(R.layout.include_d_m_nomsg);
        rv.setAdapter(mAdapter);

        mPresenter.acceptData("mAdapter", mAdapter);

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//设置刷新监听器
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {//模拟耗时操作
                    @Override
                    public void run() {
                        pageIndex = 1;
                        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载

                        mPresenter.getCollections(pageIndex + "", pageNum + "");
                        mAdapter.setEnableLoadMore(true);
                        swipe_refresh_layout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    private void loadMore() {
        if (collection.getTotalCount() > pageNum * pageIndex) {
            pageIndex++;
            mPresenter.getCollections(pageIndex + "", pageNum + "");
            mAdapter.loadMoreComplete();
            mAdapter.loadMoreEnd(false);
        } else {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(true);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetCollectionsEvent()), 500);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        onBackPressed();
    }

    @Subscribe
    public void onEventReceived(OnGetCollectionsEvent event) {
        //收到通知调用接口获取设备列表
        pageIndex = 1;
        mPresenter.getCollections(pageIndex + "", pageNum + "");
    }

    @Override
    public void showCollections(CollectionInfo collectionInfo) {
        collection = collectionInfo;
        if (pageIndex == 1) {
            mAdapter.setNewData(collectionInfo.getRows());
            mAdapter.disableLoadMoreIfNotFullPage(rv);
        } else {
            mAdapter.addData(collection.getRows());
        }
    }
}
