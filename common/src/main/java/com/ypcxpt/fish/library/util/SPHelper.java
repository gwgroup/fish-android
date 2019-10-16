package com.ypcxpt.fish.library.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import io.reactivex.annotations.Nullable;

public class SPHelper {
    private static final String SP_NAME = "FISH_MAIN";

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

    public static Integer getInt(String key, int defValue) {
        return SP.getInt(key, defValue);
    }

    public static Integer getInt(String key) {
        return getInt(key, DEF_INT_VALUE);
    }

    public static void putBoolean(String key, @Nullable boolean value) {
        SP.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return SP.getBoolean(key, defValue);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
}
