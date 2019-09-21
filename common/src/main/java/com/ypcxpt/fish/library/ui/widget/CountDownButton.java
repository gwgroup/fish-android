package com.ypcxpt.fish.library.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.definition.JAction;
import com.ypcxpt.fish.library.util.CountDownHelper;

public class CountDownButton extends AppCompatTextView {

    /* 是否要重置之前的倒计时 */
    private boolean reset = true;

    /* XML里设置的text */
    private String mOrigText;

    /* 自己的回调 */
    private CountDownHelper.OnCountDownListener mListener;

    /* 抛给外面的“倒计时结束”的回调 */
    private JAction mOnFinish;

    public CountDownButton(Context context) {
        super(context);
        initViews();
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        mOrigText = getText().toString();
//        setBackgroundResource(R.drawable.bg_common_btn_disable);

        mListener = new CountDownHelper.OnCountDownListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                setText(1 + millisUntilFinished / 1000 + "秒后重发");
            }

            @Override
            public void onFinish() {
                setText(mOrigText);
                setEnabled(true);
                if (mOnFinish != null) {
                    post(() -> mOnFinish.run());
                }
            }
        };
    }

    /**
     * 必须的初始化.
     */
    public void init(boolean reset) {
        this.reset = reset;

        if (!reset && CountDownHelper.getSelf().isCountDowning()) {
            /* 不重置并且还在倒计时中，则继续倒计时 */
            CountDownHelper.getSelf().start(false, mListener);
            return;
        } else {
            CountDownHelper.getSelf().cancel();
//            setEnabled(true);
        }

    }

    /**
     * 开始倒计时.
     */
    public void start() {
        setEnabled(false);
        CountDownHelper.getSelf().cancel();
        CountDownHelper.getSelf().start(reset, mListener);
    }

    /**
     * 对外的结束回调.
     *
     * @param onFinish
     */
    public void setOnFinishCallback(JAction onFinish) {
        mOnFinish = onFinish;
    }

    /**
     * 是否正在倒计时.
     *
     * @return
     */
    public boolean isCountingDown() {
        return CountDownHelper.getSelf().isCountDowning();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (reset) CountDownHelper.getSelf().cancel();
        super.onDetachedFromWindow();
    }

    @Override
    public void setEnabled(boolean enabled) {
        /* 倒计时中，永远为false */
        if (CountDownHelper.getSelf().isCountDowning()) {
            setBackgroundResource(R.drawable.bg_common_btn_disable);
            super.setEnabled(false);
            return;
        }

        int resID;
        if (enabled) {
            resID = R.drawable.sl_bg_common_btn;
        } else {
            resID = R.drawable.bg_common_btn_disable;
        }
        setBackgroundResource(resID);

        super.setEnabled(enabled);
    }

}
