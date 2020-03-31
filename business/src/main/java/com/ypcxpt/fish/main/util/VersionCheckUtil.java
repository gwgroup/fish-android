package com.ypcxpt.fish.main.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.google.gson.Gson;
import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.SPHelper;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.login.contract.LoginContract;
import com.ypcxpt.fish.main.event.OnRefreshUserEvent;
import com.ypcxpt.fish.main.model.VersionInfo;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: 版本更新
 * @author: xulailing on 2017/8/21 10:55.
 */

public class VersionCheckUtil extends BasePresenter<LoginContract.View> {

    private boolean isDowning = false;
    private static Context mcontext;
    private boolean isshow = false;//是否强制展示
    private boolean isShowToaster = false;//没有新版本是否提示
    long percent = 0, lastper = 0;
    VersionUpdateDialog mDialog;
    final String fileurl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/apks";//下载文件地址
//    final String fileurl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dxDownload";//下载文件地址

    private static VersionCheckUtil instance;

    public static VersionCheckUtil getInstance(Context context) {

        if (instance == null) {
            instance = new VersionCheckUtil();
            mcontext = context.getApplicationContext();
        }
        return instance;
    }

    private VersionCheckUtil() {
    }

    /**
     * 检查版本优化版
     *
     * @param activity 主依赖的activity
     * @param isshow   是否跳过版本忽略
     * @param isShowToaster 没有新版本是否提示
     */
    public void StartCheckVersion(final Activity activity, boolean isshow, boolean isShowToaster) {
        this.isshow = isshow;
        this.isShowToaster = isShowToaster;

        getVersion(activity)
                .flatMap((Function<VersionInfo, ObservableSource<String>>) versionDetailInfo -> ShowWindow(activity, versionDetailInfo))
                .flatMap((Function<String, ObservableSource<Integer>>) s -> DownLoadApk(s, activity))//这里不该写成完成式
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {//这里可以展示进度
                        Logger.i("当前进度", "----" + value);
                        mDialog.setProgressBarShow(true);
                        mDialog.setPercent(value);
                        if (value == 100) {
                            mDialog.setButtonOk("下载完成");
                            DownLoadNotification.getInstance(activity, fileurl + "/Fish.apk", false).installApk();
                            mDialog.setButtonOk("重新下载");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mDialog != null && mDialog.isShowing()) mDialog.hide();
                    }

                    @Override
                    public void onComplete() {//这里需要做的优化是
                        if (mDialog != null && mDialog.isShowing()) mDialog.hide();
                    }
                });
    }

    public Observable<VersionInfo> getVersion(Activity activity) {
        return Observable.create(new ObservableOnSubscribe<VersionInfo>() {
            @Override
            public void subscribe(final ObservableEmitter<VersionInfo> e) throws Exception {

                RequestParams params = new RequestParams("https://www.smartbreed.cn/apks/version.json");
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Logger.e("VersionCheck", result);
                        Gson gson = new Gson();
                        VersionInfo versionInfo = gson.fromJson(result, VersionInfo.class);
//                        if (versionInfo.getCode() == 0) {
                            e.onNext(versionInfo);
//                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }
                });

            }
        }).throttleFirst(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io());
    }

    private Observable<String> ShowWindow(final Activity activity, final VersionInfo data) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                String version = getAPKVersion(mcontext);
                String versionClient = version.replace(".", "");
                String versionServer = data.getVersion().replace(".", "");
                int vC = Integer.parseInt(versionClient);
                int vS = Integer.parseInt(versionServer);

                Logger.i("Version", "vc-->" + vC + ",vs-->" + vS);
                //如果本地版本>=服务器版本，则不升级
                if (vC >= vS) {
                    e.onComplete();
                    if (isShowToaster) {
                        Toaster.showLong("当前已是最新版本");
                    }
                    return;
                }
//                /**
//                 * 不需要更新
//                 */
//                if (!data.getData().isUpdate()) {
//                    SPHelper.putBoolean("FQZ_UPDATE", false);
//                    ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnRefreshUserEvent()), 200);
//                    e.onComplete();
//                    if (isShowToaster) {
//                        Toaster.showLong("当前已是最新版本");
//                    }
//                    return;
//                }

                mDialog = new VersionUpdateDialog(activity, R.style.MyDialog);
                mDialog.setTextTitle("发现新版本");
                mDialog.setVersionCode(data.getVersion());
//                mDialog.setDesccontent(data.getDescribe());
                Logger.e("VersionCheckUtil", "版本更新描述：" + data.getDescribe());
                if (getNotNullStr(data.getDescribe()).length() != 0)
                    mDialog.addTextContent(data.getDescribe());
                mDialog.setCancelable(false);
//                if (data.getData().getType() == 1) {
                    /**
                     * 非强制更新
                     */
                    mDialog.setOnlyOk(false);
                    SPHelper.putBoolean("FQZ_UPDATE", true);

                    ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnRefreshUserEvent()), 200);

                    mDialog.setProgressBarShow(false);
//                } else {
//                    /**
//                     * 强制更新
//                     */
//                    mDialog.setOnlyOk(true);
//                    isshow = true;
//
//                    mDialog.setProgressBarShow(false);
//                }
                mDialog.setOnResultListener(new VersionUpdateDialog.OnResultListener() {

                    @Override
                    public void Ok() {
                        if (!isDowning) {
                            //DownLoadApk(activity,msg.obj.toString());考虑到必须要分开 我觉得这里弄个handler来解决自定义操作而不是简单直接下载
//                            if (data.getData().getType() == 1) {
                                mDialog.dismiss();
                                Toaster.showLong("正在后台下载...请保持网络良好");
//                            } else {
//                                mDialog.setButtonOk("下载中..");
//                            }
                            e.onNext("https://www.smartbreed.cn/apks/fish.apk");
                            isDowning = true;
                        }
                    }

                    @Override
                    public void Cancel(boolean is) {
                        if (is)
                            SPHelper.putString("version", data.getVersion());
                        mDialog.dismiss();
                        e.onComplete();
                    }
                });
//                Log.e("有无忽略版本存在", SharedPreferenceUtil.getInfoFromShared("version", ""));
                if (isshow || !SPHelper.getString("version", "").equals(data.getVersion()) || isShowToaster)
                    mDialog.show();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                percent = 0;//归零操作
            }
        });
    }

    private Observable<Integer> DownLoadApk(final String url, final Activity activity) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final ObservableEmitter<Integer> e) throws Exception {

                final String downurl = url;
                Logger.e("CCC", "fileurl-->" + fileurl);
//                HttpUtils.getInstance().downLoadFile(downurl, fileurl, "Fish.apk", new MyFileRequestCallback<File>() {
//
//                    @Override
//                    public void OnSuccess(File file) {
//                        Logger.e("OnSuccess", "" + file.getAbsolutePath());
//                        isDowning = false;
//                        percent = 1;
//                        if (activity != null && !activity.isFinishing()) {
//                            e.onNext(100);
//                            DownLoadNotification.getInstance(activity, fileurl + "/Fish.apk", false).Show(100);//其实很郁闷这里的被观察操作按理说不在订阅里操作 应该是非ui操作异常的
//                        }
//                        e.onComplete();
//                    }
//
//                    @Override
//                    public void onPogress(long total, long current) {
//
//                        BigDecimal x = BigDecimal.valueOf(total);
//                        final BigDecimal d = BigDecimal.valueOf(current).divide(x, new MathContext(2));
//                        Logger.e("onPogress", "" + d);
////                        if (activity != null && !activity.isFinishing()) {
//                        int percent = (int) (100 * d.floatValue());
//                        if (percent < 100) {
//                            e.onNext(percent);
//
//                            DownLoadNotification.getInstance(activity, fileurl + "/Fish.apk", false).Show((int) (100 * d.floatValue()));//其实很郁闷这里的被观察操作按理说不在订阅里操作 应该是非ui操作异常的
//                        }
////                        }
//                    }
//
//                    @Override
//                    public void OnFail(String arg0, String arg1) {
//                        Logger.e(arg0, arg1);
//                    }
//                });

                new AppFileDownUtils(downurl, "Fish.apk", new DownLoadListener() {
                    @Override
                    public void onDownLoadSuccess(File file) {
                        isDowning = false;
                        percent = 1;
                        if (activity != null && !activity.isFinishing()) {
                            e.onNext(100);
                            DownLoadNotification.getInstance(activity, fileurl + "/Fish.apk", false).Show(100);//其实很郁闷这里的被观察操作按理说不在订阅里操作 应该是非ui操作异常的
                        }
                        e.onComplete();
                    }

                    @Override
                    public void onDownLoadFailed(String msg) {
                        Logger.e("downloadfailed", msg);
                        isDowning = false;
                        DownLoadNotification.getInstance(activity, fileurl + "/Fish.apk", false).hideNotification();
                    }

                    @Override
                    public void onDownLoading(int progress) {
                        Logger.i("onDownLoading", ">>> progress =" + progress);
//                        if (activity != null && !activity.isFinishing()) {
//                        if (progress < 100) {
                        e.onNext(progress);

                        DownLoadNotification.getInstance(activity, fileurl + "/Fish.apk", false).Show(progress);
//                        }
//                        }
                    }
                }).start();

//                RequestParams params = new RequestParams(downurl);
//                params.setAutoResume(true);
//                //自动为文件命名
//                params.setAutoRename(false);
//                //自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
//                params.setSaveFilePath(fileurl + "/Fish.apk");
//
//                params.setExecutor(new PriorityExecutor(2, true));//自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
////                params.setCancelFast(true);//是否可以被立即停止.
//                x.http().get(params, new Callback.ProgressCallback<File>() {
//                    @Override
//                    public void onSuccess(File result) {
//                        Logger.e("OnSuccess", "" + result.getAbsolutePath());
//                        isDowning = false;
//                        percent = 1;
//                        if (activity != null && !activity.isFinishing()) {
//                            e.onNext(100);
//                            DownLoadNotification.getInstance(activity, fileurl + "/Fish.apk", false).Show(100);//其实很郁闷这里的被观察操作按理说不在订阅里操作 应该是非ui操作异常的
//                        }
//                        e.onComplete();
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                        Logger.e("ex", ex.getMessage());
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//                        Logger.e("onCancelled", cex.getMessage());
//                    }
//
//                    @Override
//                    public void onFinished() {
//                        Logger.e("onFinished", "onFinished");
//                    }
//
//                    //网络请求之前回调
//                    @Override
//                    public void onWaiting() {
//                        Logger.e("onWaiting", "onWaiting");
//                    }
//
//                    //网络请求开始的时候回调
//                    @Override
//                    public void onStarted() {
//                        Logger.e("onStarted", "onStarted");
//                    }
//
//                    //下载的时候不断回调的方法
//                    @Override
//                    public void onLoading(long total, long current, boolean isDownloading) {
//                        //当前进度和文件总大小
//                        Logger.i("JAVA", "current：" + current + "，total：" + total);
//
//                        int progress = (int) (current * 100 / total);
//                        if (isDownloading) {
//                            if (progress < 100) {
//                                e.onNext(progress);
//
//                                DownLoadNotification.getInstance(activity, fileurl + "/Fish.apk", false).Show(progress);
//                            }
//                        }
//
//                        BigDecimal x = BigDecimal.valueOf(total);
//                        final BigDecimal d = BigDecimal.valueOf(current).divide(x, new MathContext(2));
//                        Logger.e("onPogress", "" + d);
//                        int percent = (int) (100 * d.floatValue());
//                        if (percent < 100) {
//                            e.onNext(percent);
//
//                            DownLoadNotification.getInstance(activity, fileurl + "/Fish.apk", false).Show((int) (100 * d.floatValue()));//其实很郁闷这里的被观察操作按理说不在订阅里操作 应该是非ui操作异常的
//                        }
//                    }
//                });
            }
        }).debounce(200, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io());
    }

    public static String getNotNullStr(String str) {
        if (str == null || str == "null" || str == "")
            return "";
        return str;
    }

    /**
     * 取得应用的版本号
     */
    public static String getAPKVersion(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
