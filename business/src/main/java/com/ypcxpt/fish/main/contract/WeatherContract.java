package com.ypcxpt.fish.main.contract;

import android.location.Location;

import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.WeatherInfo;

public interface WeatherContract {
    interface View extends IView {
        void onGetWhetherResult(WeatherInfo weatherInfo);
        void onGetLocation();
    }

    interface Presenter extends IPresenter {
        void getWeatherInfo(Location loc);
    }
}
