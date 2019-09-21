package com.ypcxpt.fish.main.presenter;

import android.location.Location;

import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.library.util.GpsHelper;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.event.OnMainPagePermissionResultEvent;
import com.ypcxpt.fish.main.model.WeatherInfo;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.Flowable;

public class WeatherPresenter extends BasePresenter<MyDeviceContract.View> {
    private DataSource mDS;

    public WeatherPresenter() {
        mDS = new DataRepository();
    }

    public void getWhetherInfo() {
        Location loc = GpsHelper.getGpsLocation(getActivity());
        if (loc == null) {
            mView.onGetWhetherResult(null);
            return;
        }

        Flowable<WeatherInfo> source = mDS.getWeatherInfo(loc.getLatitude(), loc.getLongitude());

        silenceFetch(source)
                .onSuccess(whetherInfo -> {
                    Logger.d("CCC", whetherInfo.toString());
                    mView.onGetWhetherResult(whetherInfo);
                }).onBizError(bizMsg -> mView.onGetWhetherResult(null))
                .onError(throwable -> mView.onGetWhetherResult(null))
                .start();
    }

    @Subscribe
    public void onEventReceived(OnMainPagePermissionResultEvent event) {
        /* 首页会申请权限，只有在得到权限结果后才请求天气 */
        getWhetherInfo();
    }

//    @Override
//    public void getWeatherInfo(Location loc) {
//        if (loc == null) {
//            Logger.e("CCC", "loc == null");
//            mView.onGetWhetherResult(null);
//            return;
//        }
//        Logger.e("CCC", "Lat" + loc.getLatitude());
//
//        Flowable<WeatherInfo> source = mDS.getWeatherInfo(loc.getLatitude(), loc.getLongitude());
//
//        silenceFetch(source)
//                .onSuccess(whetherInfo -> {
//                    Logger.d("CCC", whetherInfo.toString());
//                    mView.onGetWhetherResult(whetherInfo);
//                }).onBizError(bizMsg -> mView.onGetWhetherResult(null))
//                .onError(throwable -> mView.onGetWhetherResult(null))
//                .start();
//    }
}
