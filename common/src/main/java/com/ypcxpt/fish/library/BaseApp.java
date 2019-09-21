package com.ypcxpt.fish.library;

import android.app.Application;

public class BaseApp extends Application {
    private static BaseApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static BaseApp getApp() {
        return sInstance;
    }
}
