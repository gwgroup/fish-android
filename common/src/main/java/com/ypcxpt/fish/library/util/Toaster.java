package com.ypcxpt.fish.library.util;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.view.Gravity;

import com.ypcxpt.fish.library.ui.widget.ToasterView;

public class Toaster {

    private static Context sContext;

    private static ToasterView sToast;

    private static ToastHandler sToastHandler = new ToastHandler();

    public static final int TOAST_DURATION_SHORT = ToasterView.LENGTH_SHORT;

    public static final int TOAST_DURATION_LONG = ToasterView.LENGTH_LONG;

    public static void init(Application app) {
        if(sToast == null){
            sContext = app.getApplicationContext();
            sToast = new ToasterView(sContext);
        }
    }

    public static void showShort(@StringRes int resID) {
        showShort(ResourceUtils.getString(resID));
    }

    public static void showLong(@StringRes int resID) {
        showLong(ResourceUtils.getString(resID));
    }

    public static void showShort(String msg) {
        showShortToast(msg, false);
    }

    public static void showLong(String msg) {
        showLongToast(msg, false);
    }
    /**
     * choose whether to show the long toast delayed
     *
     * @param text
     */
    public static void showLongToast(CharSequence text, boolean immediateShow) {
        showToast(text, TOAST_DURATION_LONG, immediateShow);
    }
    /**
     * choose whether to show the short toast delayed
     *
     * @param text
     */
    public static void showShortToast(CharSequence text, boolean immediateShow) {
        showToast(text, TOAST_DURATION_SHORT, immediateShow);
    }

    private static class ToastHandler extends Handler {

        private static final int DELAYED_TOAST = 100;

        private static final int DELAYED_TIME_MILLS = 150;

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DELAYED_TOAST) {
                showSuperToast((CharSequence) msg.obj, msg.arg1);
            }
        }

        public void showDelayedSuperToast(CharSequence text, int duration) {
            Message msg = obtainMessage();
            msg.what = DELAYED_TOAST;
            msg.obj = text;
            msg.arg1 = duration;
            sendMessageDelayed(msg, DELAYED_TIME_MILLS);
        }
    }

    /**
     * 显示Toast的地方
     * @param text
     * @param duration
     */
    public static void showSuperToast(CharSequence text, int duration) {

        if(sToast == null) {
            sToast = new ToasterView(sContext);
        }
        sToast.setGravity(Gravity.CENTER,0,0);//中间显示
        sToast.setText(text);
        sToast.setDuration(duration);
        sToast.show();
    }

    public static void showToast(CharSequence text, int duration, boolean immediateShow) {
        if (immediateShow) {
            showSuperToast(text, duration);
        } else {
            sToastHandler.showDelayedSuperToast(text, duration);
        }
    }

}
