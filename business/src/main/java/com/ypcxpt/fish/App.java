package com.ypcxpt.fish;

import android.app.Activity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;
import com.ypcxpt.fish.core.ble.BLEClient;
import com.ypcxpt.fish.library.BaseApp;
import com.ypcxpt.fish.library.net.NetManager;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.SPHelper;
import com.ypcxpt.fish.library.util.Toaster;

import org.xutils.x;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class App extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        commonInit();

        disableAPIDialog();
        setRxJavaErrorHandler();

        /**
         * 时间紧，此段代码为了快速解决问题：APP版本升级下载完成后不安装直接退出
         */
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();
    }

    private void commonInit() {
        routerInit();
        NetManager.getInstance().setBaseUrl(BuildConfig.BASE_URL);
//        NetManager.getInstance().setBaseUrl("https://smart.reead.net/app/");
        SPHelper.init(this);
        BLEClient.init(this);
        Utils.init(this);
        Toaster.init(this);

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);  // 初始化 JPush

        x.Ext.init(this);
    }

    private void routerInit() {
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);
    }

    protected static App mApp;

    public static App getInstance() {
        return mApp;
    }

    /**
     * 反射 android9.0禁止弹窗
     */
    private void disableAPIDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * RxJava2 当取消订阅后(dispose())，RxJava抛出的异常后续无法接收(此时后台线程仍在跑，可能会抛出IO等异常),全部由RxJavaPlugin接收，需要提前设置ErrorHandler
     * 详情：http://engineering.rallyhealth.com/mobile/rxjava/reactive/2017/03/15/migrating-to-rxjava-2.html#Error Handling
     */
    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Logger.d("App", "throw test");
            }
        });
    }

    private List<Activity> activityList = new LinkedList<Activity>();

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }
}
