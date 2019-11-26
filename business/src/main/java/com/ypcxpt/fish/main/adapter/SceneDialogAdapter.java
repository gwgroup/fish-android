package com.ypcxpt.fish.main.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.util.StringHelper;

public class SceneDialogAdapter extends BaseQuickAdapter<Scenes, BaseViewHolder> {

    public SceneDialogAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Scenes item) {
        String name = StringHelper.nullToDefault(item.scene_name, "");
        helper.setText(R.id.tv_name, name);

        if (helper.getLayoutPosition() == 0) {
            helper.getView(R.id.vew_line).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.vew_line).setVisibility(View.VISIBLE);
        }
    }
}