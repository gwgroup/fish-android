package com.ypcxpt.fish.main.presenter;

import com.google.gson.Gson;
import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.AppData;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.library.util.SPHelper;
import com.ypcxpt.fish.main.model.CamsMove;
import com.ypcxpt.fish.main.model.Scenes;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.event.OnGetScenesEvent;
import com.ypcxpt.fish.main.model.Cams;
import com.ypcxpt.fish.main.model.CamsUseable;
import com.ypcxpt.fish.main.model.CommonInfo;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.WeatherInfo;
import com.ypcxpt.fish.main.view.fragment.MyDeviceFragment;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import io.reactivex.Flowable;

import static com.ypcxpt.fish.BaseUrlConstant.BASE_URL;

public class MyDevicePresenter extends BasePresenter<MyDeviceContract.View> implements MyDeviceContract.Presenter {
    private DataSource mDS;

    public MyDevicePresenter() {
        mDS = new DataRepository();
    }

    @Override
    public void addScenes(String mac, String name) {
        Flowable<Object> source = mDS.addScenes(mac, name);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "添加场景成功");
                    ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetScenesEvent(0)), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void removeScenes(String mac) {
        Flowable<Object> source = mDS.removeScenes(mac);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "移除场景成功");
                    ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetScenesEvent(0)), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void renameScenes(String mac, String name) {
        Flowable<Object> source = mDS.renameScenes(mac, name);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "重命名场景成功");
                    ThreadHelper.postDelayed(() -> EventBus.getDefault().post(new OnGetScenesEvent(0)), 500);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void getScenes(int selected) {
        Flowable<List<Scenes>> source = mDS.getScenes();
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
        .onSuccess(scenes -> {
            Logger.d("CCC", "scenes-->" + scenes.toString());
            mView.showScenes(scenes, selected);
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void getIoStatus(String mac) {
        Flowable<List<IoInfo>> source = mDS.getIoInfo(mac);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
                .onSuccess(ioInfos -> {
                    Logger.d("CCC", "ioInfos-->" + ioInfos.toString());
                    mView.showIoStatus(ioInfos);
                }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void openIO(String mac, String code, int duration) {
        Flowable<Object> source = mDS.openIO(mac, code, duration);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
                .onSuccess(o -> {
                    Logger.d("CCC", "打开成功");
//                    getIoStatus(mac);
                }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void closeIO(String mac, String code) {
        Flowable<Object> source = mDS.closeIO(mac, code);
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
                .onSuccess(o -> {
                    Logger.d("CCC", "关闭成功");
//                    getIoStatus(mac);
                }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void calibrationFeeder(String mac, String code, double feeder) {
        Flowable<Object> source = mDS.calibrationFeederIO(mac, code, feeder);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "校准投喂机成功");
                    SPHelper.putBoolean("FEEDER_CHECKED", true);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void getNotAvailableCams(String mac, String key, String pass) {
        Flowable<Cams> source = mDS.getCamsConfigAuth(mac, key, pass);
        silenceFetch(source)
                .onSuccess(cams -> {
                    Logger.i("CCC", "获取摄像头配置-->" + cams.toString());
                    /* 可直接访问的摄像头数组 */
                    mView.displayCamsCount(cams.usable_cams, "", "");
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void getCamsConfig(String mac) {
        MyDeviceFragment.mPass = "";
        Flowable<Cams> source = mDS.getCamsConfig(mac);
        silenceFetch(source)
                .onSuccess(cams -> {
                    Logger.i("CCC", "获取摄像头配置-->" + cams.toString());
                    if (cams.not_available_cams != null && cams.not_available_cams.size() > 0 && cams.usable_cams.size() == 0) {
                        /* 有口令的摄像头数组 */
                        mView.displayCamsCount(cams.usable_cams, cams.not_available_cams.get(0).key, cams.not_available_cams.get(0).hostname);
                    } else {
                        /* 可直接访问的摄像头数组 */
                        mView.displayCamsCount(cams.usable_cams, "", "");
                    }
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    /**
     * 请求服务器开始推流
     * @param usable_cams 可直接访问的摄像头数组
     */
    @Override
    public void doCamsPlay(String mac, List<CamsUseable> usable_cams, String playKey, int camsIndex) {
        Logger.i("推流请求", "mac:" + mac + ",key:" + playKey);
//        Flowable<Object> source = mDS.doPlay(mac, usable_cams.get(0).key);
//        silenceFetch(source)
//                .onSuccess(o -> {
//                    Logger.d("CCC", "推流成功");
//                    mView.showVLCVideo(usable_cams);
//                })
//                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
//                .onError(throwable -> Logger.d("CCC", throwable.toString()))
//                .start();

        RequestParams params = new RequestParams(BASE_URL + "api/cams/play");
        params.addParameter("device_mac", mac);
        params.addParameter("cam_key", playKey);

        params.addHeader("authorization", AppData.token()); //为当前请求添加一个头
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonInfo commonInfo = gson.fromJson(result, CommonInfo.class);
                if (commonInfo.getCode() == 1000) {
                    Logger.e("CCC", "推流成功,key:" + playKey);
                    mView.showVLCVideo(usable_cams, playKey, camsIndex);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e("CCC", "推流失败");
                doCamsPlay(mac, usable_cams, playKey, camsIndex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logger.e("CCC", "推流取消");
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void changeProfile(String mac, String playKey, String profileToken, String label) {
        Flowable<Object> source = mDS.switchProfile(mac, playKey, profileToken);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.e("CCC", "切换清晰度成功");
                    mView.showVLCVideoLabel(label);
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void doCamsMove(String mac, String playKey, CamsMove camsMove) {
        Flowable<Object> source = mDS.doCamsMove(mac, playKey, camsMove);
        fetch(source).onSuccess(o -> {
            Logger.e("CCC", "移动镜头成功");
        }).start();
    }

    @Override
    public void getWeather(String mac) {
        RequestParams params = new RequestParams(BASE_URL + "api/device/get_weather");
        params.addParameter("device_mac", mac);

        params.addHeader("authorization", AppData.token()); //为当前请求添加一个头
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                WeatherInfo weatherInfo = gson.fromJson(result, WeatherInfo.class);
                if (weatherInfo.getCode() == 1000) {
                    Logger.e("CCC", "获取到天气" + result);
                    mView.onGetWhetherResult(weatherInfo);
                }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
