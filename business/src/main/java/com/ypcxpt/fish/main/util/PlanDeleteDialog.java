package com.ypcxpt.fish.main.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ypcxpt.fish.R;

public class PlanDeleteDialog extends Dialog implements View.OnClickListener {

    private Button bt_cancel;
    private Button bt_ok;

    private OnResultListener mListener;// 回调

    private boolean isCenter = true;
    private Context context;

    public PlanDeleteDialog(Context context) {
        super(context);
        init();
    }

    public PlanDeleteDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }

    public PlanDeleteDialog(Context context, int theme, boolean isCenter) {
        super(context, theme);
        this.isCenter = isCenter;
        init();
    }

    public PlanDeleteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
        setContentView(R.layout.dialog_plan_delete);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_cancel.setOnClickListener(this);
        bt_ok.setOnClickListener(this);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mListener.Cancel();
            }
        });
    }

    /**
     * 监听回调
     */
    public interface OnResultListener {
        void Ok();

        void Cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                mListener.Ok();
                break;
            case R.id.bt_cancel:
                mListener.Cancel();
                break;
        }
    }

    public void setOnResultListener(OnResultListener mListener) {
        this.mListener = mListener;
    }
}