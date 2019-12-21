package com.ypcxpt.fish.main.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.main.model.Scenes;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.main.adapter.SceneDialogAdapter;

import java.util.List;

public class SelectScenesDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout rl_bg;
    private RecyclerView rv;

    private List<Scenes> mScenes;
    private SceneDialogAdapter mAdapter;

    private Context context;
    private OnResultListener mListener;// 回调

    public SelectScenesDialog(Context context) {
        super(context);
        init();
    }

    public SelectScenesDialog(Context context, List<Scenes> scenes, int theme) {
        super(context, theme);
        this.context = context;
        this.mScenes = scenes;

        init();
    }

    public SelectScenesDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    public void show() {
        super.show();
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        getWindow().setAttributes(lp);
    }

    /**
     * 初始化数据
     */
    private void init() {
        setContentView(R.layout.dialog_select_scenes);
        rl_bg = findViewById(R.id.rl_bg);
        rv = findViewById(R.id.rv);
        rl_bg.setOnClickListener(this);

        mAdapter = new SceneDialogAdapter(R.layout.item_scenes_dialog);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(mAdapter);
        ((DefaultItemAnimator) rv.getItemAnimator()).setSupportsChangeAnimations(false);
        rv.getItemAnimator().setChangeDuration(0);// 通过设置动画执行时间为0来解决闪烁问题

        mAdapter.setNewData(mScenes);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.i("选择场景", "mac:" + mScenes.get(position).macAddress
                        + ",name:" + mScenes.get(position).scene_name);
                mListener.SelectScenes(mScenes.get(position).macAddress, mScenes.get(position).scene_name);
            }
        });

        setOnDismissListener(dialogInterface -> mListener.Cancel());
    }

    /**
     * 监听回调
     */
    public interface OnResultListener {
        void SelectScenes(String macAddress, String scene_name);

        void Cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_bg:
                mListener.Cancel();
                break;
        }
    }

    public void setOnResultListener(OnResultListener mListener) {
        this.mListener = mListener;
    }
}