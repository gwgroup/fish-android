package com.ypcxpt.fish.main.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xw.repo.XEditText;
import com.ypcxpt.fish.R;

public class CalibrationFeederDialog extends Dialog implements View.OnClickListener {

    private TextView tv_title;
    private XEditText et_bark;
    private Button bt_cancel;
    private Button bt_ok;

    private OnResultListener mListener;// 回调

    private boolean isCenter = true;
    private Context context;

    private String bark;

    public CalibrationFeederDialog(Context context) {
        super(context);
        init();
    }

    public CalibrationFeederDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }

    public CalibrationFeederDialog(Context context, String bark, int theme) {
        super(context, theme);
        this.context = context;
        this.bark = bark;
        init();
    }

    public CalibrationFeederDialog(Context context, int theme, boolean isCenter) {
        super(context, theme);
        this.isCenter = isCenter;
        init();
    }

    public CalibrationFeederDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
        setContentView(R.layout.dialog_calibration_feeder);
        tv_title = findViewById(R.id.tv_title);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        et_bark = findViewById(R.id.et_bark);
        bt_cancel.setOnClickListener(this);
        bt_ok.setOnClickListener(this);

//        et_bark.setText(bark);
//        et_bark.setSelection(bark.length());
        et_bark.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

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
        void Ok(String bark);

        void Cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                mListener.Ok(et_bark.getText().toString());
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