package com.ypcxpt.fish.main.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ypcxpt.fish.App;

/**
 * @author xulailing on 2016/7/26.
 * @Description SharedPreference工具类
 */
public class SharedPreferenceUtil {
    public final static String SP_NAME = "SP_NAME_INFO";
    private static SharedPreferences preferences = App.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

    public static boolean setInfoToShared(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }

    public static String getInfoFromShared(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public static boolean setInfoToShared(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
        return true;
    }

    public static boolean getInfoFromShared(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }
}
