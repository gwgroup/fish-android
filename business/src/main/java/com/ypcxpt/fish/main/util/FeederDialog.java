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

public class FeederDialog extends Dialog implements View.OnClickListener {

    private XEditText et_feeder;
    private TextView tv_calibration;
    private Button bt_cancel;
    private Button bt_ok;

    private OnResultListener mListener;// 回调

    private boolean isCenter = true;
    private Context context;

    public FeederDialog(Context context) {
        super(context);
        init();
    }

    public FeederDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }

    public FeederDialog(Context context, int theme, boolean isCenter) {
        super(context, theme);
        this.isCenter = isCenter;
        init();
    }

    public FeederDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
        setContentView(R.layout.dialog_feeder);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        et_feeder = findViewById(R.id.et_feeder);
        tv_calibration = findViewById(R.id.tv_calibration);
        bt_cancel.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        tv_calibration.setOnClickListener(this);

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
        void Ok(int feeder);

        void Cancel();

        void CalibrationFeeder();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                mListener.Ok(Integer.parseInt(et_feeder.getText().toString()));
                break;
            case R.id.bt_cancel:
                mListener.Cancel();
                break;
            case R.id.tv_calibration:
                mListener.CalibrationFeeder();
                break;
        }
    }

    public void setOnResultListener(OnResultListener mListener) {
        this.mListener = mListener;
    }
}