package com.ypcxpt.fish.main.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xw.repo.XEditText;

import com.ypcxpt.fish.R;

public class DurationSelectDialog extends Dialog implements View.OnClickListener {

    private TextView tv_title;
    private XEditText et_hour;
    private XEditText et_minute;
    private XEditText et_second;
    private Button bt_cancel;
    private Button bt_ok;

    private OnResultListener mListener;// 回调

    private boolean isCenter = true;
    private Context context;

    public DurationSelectDialog(Context context) {
        super(context);
        init();
    }

    public DurationSelectDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }

    public DurationSelectDialog(Context context, int theme, boolean isCenter) {
        super(context, theme);
        this.isCenter = isCenter;
        init();
    }

    public DurationSelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
        setContentView(R.layout.dialog_duration_select);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        tv_title = findViewById(R.id.tv_title);
        et_hour = findViewById(R.id.et_hour);
        et_minute = findViewById(R.id.et_minute);
        et_second = findViewById(R.id.et_second);
        bt_cancel.setOnClickListener(this);
        bt_ok.setOnClickListener(this);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mListener.Cancel();
            }
        });
    }

    public void setTitle(String title) {
        tv_title.setText("选择" + "\"" + title + "\"" + "打开时长");
    }

    /**
     * 监听回调
     */
    public interface OnResultListener {
        void Ok(int hour, int minute, int second);

        void Cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                mListener.Ok(Integer.parseInt(et_hour.getText().toString()), Integer.parseInt(et_minute.getText().toString()), Integer.parseInt(et_second.getText().toString()));
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