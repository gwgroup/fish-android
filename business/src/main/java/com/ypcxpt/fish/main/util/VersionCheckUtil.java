package com.ypcxpt.fish.main.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.SPHelper;
import com.ypcxpt.fish.login.contract.LoginContract;
import com.ypcxpt.fish.main.model.VersionDetailInfo;
import com.ypcxpt.fish.main.util.okhttp.HttpUtils;
import com.ypcxpt.fish.main.util.okhttp.callback.filecallback.MyFileRequestCallback;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
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
    long percent = 0, lastper = 0;
    VersionUpdateDialog mDialog;
    final String fileurl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/apks";//下载文件地址

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
     * @param isshow   是否跳过版本忽略o
     */
    public void StartCheckVersion(final Activity activity, boolean isshow) {
        this.isshow = isshow;
        getVersion(activity)
                .flatMap(new Function<VersionDetailInfo, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(VersionDetailInfo versionDetailInfo) throws Exception {
                        return ShowWindow(activity, versionDetailInfo);
                    }
                })
                .flatMap(new Function<String, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(String s) throws Exception {
                        return DownLoadApk(s, activity);
                    }
                })//这里不该写成完成式
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {//这里可以展示进度
                        Log.e("当前进度", "----" + value);
                        mDialog.setPercent(value);
                        if (value == 100) {
                            mDialog.setButtonOk("下载完成");
                            DownLoadNotification.getInstance(activity, fileurl + "/ReeadHome.apk", false).installApk();
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

    public Observable<VersionDetailInfo> getVersion(Activity activity) {
        return Observable.create(new ObservableOnSubscribe<VersionDetailInfo>() {
            @Override
            public void subscribe(final ObservableEmitter<VersionDetailInfo> e) throws Exception {

                String url = "https://smart.reead.net/version.json";
                RequestQueue mQueue = Volley.newRequestQueue(mcontext);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Logger.i("VersionCheck", "版本更新Json-->" + response);

                        Gson gson = new Gson();
                        VersionDetailInfo versionDetailInfo = gson.fromJson(response, VersionDetailInfo.class);
//                        if (versionDetailInfo.getCode() == 0) {
                            e.onNext(versionDetailInfo);
//                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("", "");
                        return map;
                    }
                };
                mQueue.add(stringRequest);

            }
        }).throttleFirst(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io());
    }

    private Observable<String> ShowWindow(final Activity activity, final VersionDetailInfo data) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
//                if ("2".equals(data.getMust_update()) || "0".equals(data.getHas_version())) {//不需更新
//                    e.onComplete();
//                    return;
//                }


                String version = getAPKVersion(mcontext);
                String versionClient = version.replace(".", "");
                String versionServer = data.getVersion().replace(".", "");
                int vC = Integer.parseInt(versionClient);
                int vS = Integer.parseInt(versionServer);

                Logger.i("Version", "vc-->" + vC + ",vs-->" + vS);
                //如果本地版本>=服务器版本，则不升级
                if (vC >= vS) {
                    e.onComplete();
                    return;
                }


                mDialog = new VersionUpdateDialog(activity, R.style.MyDialog);
                mDialog.setTextTitle("温馨提示");
                mDialog.setVersionCode(data.getVersion());
//                mDialog.setDesccontent(data.getDescribe());
                Log.e("VersionCheckUtil", "版本更新描述：" + data.getIntrodution());
                if (getNotNullStr(data.getIntrodution()).length() != 0)
                    mDialog.addTextContent(data.getIntrodution());
                mDialog.setCancelable(false);
                if ("0".equals(data.getMust_update())) {
                    mDialog.setOnlyOk(false);
                } else {
                    mDialog.setOnlyOk(true);
                    isshow = true;
                }
                mDialog.setOnResultListener(new VersionUpdateDialog.OnResultListener() {

                    @Override
                    public void Ok() {
                        if (!isDowning) {
                            //DownLoadApk(activity,msg.obj.toString());考虑到必须要分开 我觉得这里弄个handler来解决自定义操作而不是简单直接下载
                            if ("0".equals(data.getMust_update()))
                                mDialog.dismiss();
                            else {
                                mDialog.setButtonOk("下载中..");
                            }
                            e.onNext(data.getDownload_url());
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
                if (isshow || !SPHelper.getString("version", "").equals(data.getVersion()))
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
                HttpUtils.getInstance().downLoadFile(downurl, fileurl, "ReeadHome.apk", new MyFileRequestCallback<File>() {

                    @Override
                    public void OnSuccess(File file) {
                        Log.e("OnSuccess", "" + file.getAbsolutePath());
                        isDowning = false;
                        percent = 1;
                        if (activity != null && !activity.isFinishing()) {
                            e.onNext(100);
                            DownLoadNotification.getInstance(activity, fileurl + "/ReeadHome.apk", false).Show(100);//其实很郁闷这里的被观察操作按理说不在订阅里操作 应该是非ui操作异常的
                        }
                        e.onComplete();
                    }

                    @Override
                    public void onPogress(long total, long current) {

                        BigDecimal x = BigDecimal.valueOf(total);
                        final BigDecimal d = BigDecimal.valueOf(current).divide(x, new MathContext(2));
                        Log.e("onPogress", "" + d);
                        if (activity != null && !activity.isFinishing()) {
                            int percent = (int) (100 * d.floatValue());
                            if (percent < 100) {
                                e.onNext(percent);

                                DownLoadNotification.getInstance(activity, fileurl + "/ReeadHome.apk", false).Show((int) (100 * d.floatValue()));//其实很郁闷这里的被观察操作按理说不在订阅里操作 应该是非ui操作异常的
                            }
                        }
                    }

                    @Override
                    public void OnFail(String arg0, String arg1) {
                        Log.e(arg0, arg1);
                    }
                });
            }
        }).debounce(200, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io());
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

    public static String getNotNullStr(String str){
        if(str==null||str=="null"||str=="")
            return "";
        return str;
    }
}
