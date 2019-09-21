package com.ypcxpt.fish.library.util;


import com.ypcxpt.fish.library.BuildConfig;

public class Logger {
    private final static boolean DEBUG = !"release".equals(BuildConfig.BUILD_TYPE);

    public static void v(String tag, String msg) {
        if (DEBUG) android.util.Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG) android.util.Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (DEBUG) android.util.Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG) android.util.Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG) android.util.Log.e(tag, msg);
    }
}
