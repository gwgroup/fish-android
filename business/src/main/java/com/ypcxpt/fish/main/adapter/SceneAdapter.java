package com.ypcxpt.fish.main.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.StringHelper;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.view.fragment.MyDeviceFragment;

public class SceneAdapter extends BaseQuickAdapter<Scenes, BaseViewHolder> {
    private MyDeviceContract.Presenter mPresenter;
    private Activity activity;

    private int index = 0;

    public SceneAdapter(int layoutResId, MyDeviceContract.Presenter presenter) {
        super(layoutResId);
        mPresenter = presenter;
    }

    public SceneAdapter(int layoutResId, MyDeviceContract.Presenter presenter, Activity activity) {
        super(layoutResId);
        mPresenter = presenter;
        this.activity = activity;
    }

    public void setIndex(int itemIndex) {
        index = itemIndex;
    }

    @Override
    protected void convert(BaseViewHolder helper, Scenes item) {
        String name = StringHelper.nullToDefault(item.scene_name, "");

        if (index == helper.getLayoutPosition()) {
            helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
            TextView textView = helper.getView(R.id.tv_name);
            textView.setTextColor(mContext.getResources().getColor(R.color.main_color_new));
        } else {
            helper.getView(R.id.view_line).setVisibility(View.INVISIBLE);
            TextView textView = helper.getView(R.id.tv_name);
            textView.setTextColor(mContext.getResources().getColor(R.color.common_141515));
        }

        helper.setText(R.id.tv_name, name);
        helper.getView(R.id.rl_item).setOnClickListener(v -> {
            //获取下标更新UI
            index = helper.getLayoutPosition();
            notifyDataSetChanged();

            /**
             * 获取mac下的IO信息
             */
            mPresenter.getIoStatus(item.macAddress);
            MyDeviceFragment.macAddress = item.macAddress;
            Logger.i("当前mac", MyDeviceFragment.macAddress);
        });
    }
}