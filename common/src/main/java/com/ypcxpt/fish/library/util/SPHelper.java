package com.ypcxpt.fish.library.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import io.reactivex.annotations.Nullable;

public class SPHelper {
    private static final String SP_NAME = "REEAD_MAIN";

    private static final int DEF_INT_VALUE = -1;

    private static SharedPreferences SP;

    public static void init(Application app) {
        SP = app.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static void putString(String key, @Nullable String value) {
        SP.edit().putString(key, value).commit();
    }

    public static String getString(String key, @Nullable String defValue) {
        return SP.getString(key, defValue);
    }

    public static String getString(String key) {
        return getString(key, null);
    }

    public static void putInt(String key, int value) {
        SP.edit().putInt(key, value).commit();
    }

    public static void getInt(String key, int defValue) {
        SP.getInt(key, defValue);
    }

    public static void getInt(String key) {
        getInt(key, DEF_INT_VALUE);
    }

}
