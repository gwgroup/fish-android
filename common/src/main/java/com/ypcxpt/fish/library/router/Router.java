package com.ypcxpt.fish.library.router;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

import com.ypcxpt.fish.library.util.ThreadHelper;

/**
 * Router for {@link Activity} jumping
 */
public class Router {
    private static MockPostcard mMockPostcard;

    public static class MockPostcard {

        private Postcard mPostcard;

        private boolean withFinish;

        private StartStyle startStyle = StartStyle.COMMON;

        /**
         * @param path
         */
        void build(String path) {
            mPostcard = ARouter.getInstance().build(path);
        }

        public MockPostcard withString(@Nullable String key, @Nullable String value) {
            mPostcard.withString(key, value);
            return this;
        }

        public MockPostcard withBoolean(@Nullable String key, boolean value) {
            mPostcard.withBoolean(key, value);
            return this;
        }

        public MockPostcard withByte(@Nullable String key, byte value) {
            mPostcard.withByte(key, value);
            return this;
        }

        public MockPostcard withShort(@Nullable String key, short value) {
            mPostcard.withShort(key, value);
            return this;
        }

        public MockPostcard withInt(@Nullable String key, int value) {
            mPostcard.withInt(key, value);
            return this;
        }

        public MockPostcard withLong(@Nullable String key, long value) {
            mPostcard.withLong(key, value);
            return this;
        }

        public MockPostcard withFloat(@Nullable String key, float value) {
            mPostcard.withFloat(key, value);
            return this;
        }

        public MockPostcard withDouble(@Nullable String key, double value) {
            mPostcard.withDouble(key, value);
            return this;
        }

        public MockPostcard withParcelable(@Nullable String key, Parcelable value) {
            mPostcard.withParcelable(key, value);
            return this;
        }

        /**
         * In most cases, you will not use this method, cause there are default animation styles.
         *
         * @param startType Animation style when starting Activity.
         */
        public MockPostcard startStyle(StartStyle startType) {
            this.startStyle = startType;
            return this;
        }

        /**
         * Only works fine with {@link #navigation(Activity)}
         */
        public MockPostcard withFinish() {
            withFinish = true;
            return this;
        }

        public MockPostcard clearTask() {
            startStyle(StartStyle.REVERSE);
            mPostcard.withFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            return this;
        }

        public MockPostcard clearTop() {
            mPostcard.withFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return this;
        }

        public MockPostcard singleTop() {
            mPostcard.withFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            return this;
        }

        public MockPostcard withFlag(@Nullable String key, float value) {
            mPostcard.withFloat(key, value);
            return this;
        }

        /**
         * 跳转activity
         * @param activity From activity
         * @param enterAnim
         * @param exitAnim
         */
        public void navigation(Activity activity, int enterAnim, int exitAnim) {
            mPostcard.withTransition(enterAnim, exitAnim).navigation(activity);
            if (withFinish) {
                ThreadHelper.postDelayed(() -> activity.finish(),500);
            }
            clear();
        }

        /**
         * @param activity From activity
         */
        public void navigation(Activity activity) {
            mPostcard.withTransition(getEnterAnim(), getExitAnim()).navigation(activity);
            if (withFinish) {
                ThreadHelper.postDelayed(() -> activity.finish(),500);
            }
            clear();
        }

        /**
         * Eg: From {@link Notification} or {@link BroadcastReceiver},
         * which has no {@link Activity} or {@link View} references.
         *
         * @param context
         */
        public void navigation(Context context) {
            if (context instanceof Activity) {
                navigation((Activity) context);
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mPostcard = mPostcard.withOptionsCompat(getCompatWithoutView(context));
            }
            mPostcard.navigation(context);
            clear();
        }

        /**
         * @param source Trigger of the jump
         */
        public void navigation(View source) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mPostcard = mPostcard.withOptionsCompat(getCompatWithView(source));
            }
            mPostcard.navigation();
            clear();
        }

        /**
         * @param activity    From activity
         * @param requestCode requestCode for {@link Activity#startActivityForResult(Intent, int)}
         */
        public void navigation(Activity activity, int requestCode) {
            mPostcard.withTransition(getEnterAnim(), getExitAnim()).navigation(activity, requestCode);
            clear();
        }

        /**
         * clear static data
         */
        private void clear() {
            withFinish = false;
            startStyle = StartStyle.COMMON;
            mPostcard = null;
            Router.mMockPostcard = null;
        }
    }

    /**
     * @param path Where you go
     */
    public static MockPostcard build(String path) {
        getMockPostcard().build(path);
        return mMockPostcard;
    }

    /**
     * @return
     */
    private static MockPostcard getMockPostcard() {
        return mMockPostcard == null ? (mMockPostcard = new MockPostcard()) : mMockPostcard;
    }

    /**
     * @param object
     */
    public static void inject(Object object) {
        ARouter.getInstance().inject(object);
    }

    /**
     * @return
     */
    private static StartStyle getStartStyle() {
        return mMockPostcard == null ? StartStyle.COMMON : mMockPostcard.startStyle;
    }

    /**
     * @param source
     */
    private static ActivityOptionsCompat getCompatWithView(View source) {
        return ActivityTransitionAnim.getCompatWithView(source, getStartStyle());
    }

    /**
     * @param context
     */
    private static ActivityOptionsCompat getCompatWithoutView(Context context) {
        return ActivityTransitionAnim.getCompatWithoutView(context, getStartStyle());
    }

    /**
     * @return
     */
    private static int getEnterAnim() {
        return ActivityTransitionAnim.getStartEnterAnim(getStartStyle());
    }

    /**
     * @return
     */
    private static int getExitAnim() {
        return ActivityTransitionAnim.getStartExitAnim(getStartStyle());
    }

}
