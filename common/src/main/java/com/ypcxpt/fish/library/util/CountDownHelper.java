package com.ypcxpt.fish.library.util;

import android.os.CountDownTimer;

public class CountDownHelper {
    /* 倒计时频率 */
    public static final long COUNT_DOWN_INTERVAL = 450;

    /* 倒计时总时长 */
    public static final long TOTAL_TIME = 60 * 1000;

    /* 全局倒计时时间的最短保留时间 */
    public static final long MIN_RETAIN_TIME = 2 * 1000;

    /* 全局倒计时的时间 */
    private static long sUnfinishedTime;

    private CountDownTimer mTimer;

    private OnCountDownListener mListener;

    private static class Singleton {

        private static final CountDownHelper sCountDownHelper = new CountDownHelper();
    }

    private CountDownHelper() {
    }

    public static CountDownHelper getSelf() {
        return Singleton.sCountDownHelper;
    }

    /**
     * 开始倒计时.
     *
     * @param reset    是否重置倒计时.
     * @param listener 回调.
     */
    public void start(boolean reset, OnCountDownListener listener) {
        if (listener == null) return;
        mListener = listener;

        if (reset || sUnfinishedTime < MIN_RETAIN_TIME) {
            /* 重置或者倒计时时间剩余太少 */
            mTimer = getCountDownTimer(TOTAL_TIME, listener);
        } else {
            /* 无需重置 */
            mTimer = getCountDownTimer(sUnfinishedTime, listener);
        }

        mTimer.start();
    }

    /**
     * 取消倒计时.
     */
    public void cancel() {
        if (mTimer != null) mTimer.cancel();
        sUnfinishedTime = 0;
        mTimer = null;
    }

    public boolean isCountDowning() {
        return sUnfinishedTime > MIN_RETAIN_TIME;
    }

    private CountDownTimer getCountDownTimer(long totalTime, OnCountDownListener listener) {
        return new CountDownTimer(totalTime, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                sUnfinishedTime = millisUntilFinished;
                if (listener != null) listener.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                sUnfinishedTime = 0;
                if (listener != null) listener.onFinish();
            }
        };
    }

    public interface OnCountDownListener {
        void onTick(long millisUntilFinished);

        void onFinish();
    }

}
