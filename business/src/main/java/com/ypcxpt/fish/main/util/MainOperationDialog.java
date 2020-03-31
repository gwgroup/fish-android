package com.ypcxpt.fish.main.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypcxpt.fish.R;

public class MainOperationDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout rl_bg;
    private TextView tv_add_scene;
    private TextView tv_remove_scene;
    private TextView tv_rename_scene;
    private TextView tv_config_scene;
    private TextView tv_scan;

    private OnResultListener mListener;// 回调

    public MainOperationDialog(Context context) {
        super(context);
        init();
    }

    public MainOperationDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    public MainOperationDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
        setContentView(R.layout.dialog_main_operation);
        rl_bg = findViewById(R.id.rl_bg);
        tv_add_scene = findViewById(R.id.tv_add_scene);
        tv_remove_scene = findViewById(R.id.tv_remove_scene);
        tv_rename_scene = findViewById(R.id.tv_rename_scene);
        tv_config_scene = findViewById(R.id.tv_config_scene);
        tv_scan = findViewById(R.id.tv_scan);
        tv_add_scene.setOnClickListener(this);
        tv_remove_scene.setOnClickListener(this);
        tv_rename_scene.setOnClickListener(this);
        tv_config_scene.setOnClickListener(this);
        tv_scan.setOnClickListener(this);
        rl_bg.setOnClickListener(this);

        setOnDismissListener(dialogInterface -> mListener.Cancel());
    }

    /**
     * 监听回调
     */
    public interface OnResultListener {
        void Add();

        void Remove();

        void Rename();

        void Config();

        void Refresh();

        void Cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_scene:
                mListener.Add();
                break;
            case R.id.tv_remove_scene:
                mListener.Remove();
                break;
            case R.id.tv_rename_scene:
                mListener.Rename();
                break;
            case R.id.tv_config_scene:
                mListener.Config();
                break;
            case R.id.tv_scan:
                mListener.Refresh();
                break;
            case R.id.rl_bg:
                mListener.Cancel();
                break;
        }
    }

    public void setOnResultListener(OnResultListener mListener) {
        this.mListener = mListener;
    }
}