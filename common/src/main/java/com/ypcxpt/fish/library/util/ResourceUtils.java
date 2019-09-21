package com.ypcxpt.fish.library.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.ypcxpt.fish.library.BaseApp;

public class ResourceUtils {
    private static Context getContext() {
        return BaseApp.getApp();
    }

    public static String getString(@StringRes int resID) {
        return getContext().getResources().getString(resID);
    }

    public static @ColorInt
    int getColor(@ColorRes int resID) {
        return ContextCompat.getColor(getContext(), resID);
    }

    public static int getDimension(@DimenRes int resID) {
        return (int) getContext().getResources().getDimension(resID);
    }

    public static int getDrawableIDByName(String resName) {
        return getContext().getResources().getIdentifier(resName, "mipmap", getContext().getPackageName());
    }

    public static Drawable getDrawableByName(String resName) {
        return getContext().getResources().getDrawable(getDrawableIDByName(resName));
    }

}
