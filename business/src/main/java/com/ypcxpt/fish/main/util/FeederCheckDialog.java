package com.ypcxpt.fish.main.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ypcxpt.fish.R;

public class FeederCheckDialog extends Dialog implements View.OnClickListener {

    private TextView tv_feederCheck;
    private OnResultListener mListener;// 回调

    private final int ADVERTISE_TIME = 10 * 1000;
    private final int COUNT_DOWN_INTERVAL = 200;
    private CountDownTimer mTimer = new CountDownTimer(ADVERTISE_TIME, COUNT_DOWN_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {
            long untilFinishedSeconds = millisUntilFinished / 1000;
            tv_feederCheck.setText(untilFinishedSeconds + "秒");
        }

        @Override
        public void onFinish() {
            mTimer.cancel();
            mListener.Cancel();
        }
    };

    private Context context;

    public FeederCheckDialog(Context context) {
        super(context);
        init();
    }

    public FeederCheckDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }

    public FeederCheckDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    public void show() {
        super.show();

        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //设置alterdialog全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        getWindow().setAttributes(lp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    /**
     * 初始化数据
     */
    private void init() {
        setContentView(R.layout.dialog_feeder_check);
        tv_feederCheck = findViewById(R.id.tv_feederCheck);

        tv_feederCheck.setOnClickListener(this);

//        setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                mListener.Cancel();
//            }
//        });

    }

    /**
     * 监听回调
     */
    public interface OnResultListener {
        void Cancel();
        void Feeder();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_feederCheck:
                mTimer.start();

                mListener.Feeder();
                break;
        }
    }

    public void setOnResultListener(OnResultListener mListener) {
        this.mListener = mListener;
    }
}