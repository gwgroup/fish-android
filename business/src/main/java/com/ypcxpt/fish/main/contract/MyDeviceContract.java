package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.main.model.Scenes;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.CamsUseable;
import com.ypcxpt.fish.main.model.IoInfo;
import com.ypcxpt.fish.main.model.WeatherInfo;

import java.util.List;

public interface MyDeviceContract {
    interface View extends IView {
        void onGetWhetherResult(WeatherInfo weatherInfo);

        void showScenes(List<Scenes> scenes, int selected);

        void showIoStatus(List<IoInfo> ioInfos);

        void displayCamsCount(List<CamsUseable> usable_cams, String passKey, String pass);

        void showVLCVideo(List<CamsUseable> usable_cams, String playKey, int camsIndex);

        void showVLCVideoLabel(String label);
    }

    interface Presenter extends IPresenter {
        //添加场景
        void addScenes(String mac, String name);
        //移除场景
        void removeScenes(String mac);
        //重命名场景
        void renameScenes(String mac, String name);
        //获取场景
        void getScenes(int selected);
        //获取设备IO配置信息
        void getIoStatus(String mac);
        //打开IO
        void openIO(String mac, String code, int duration);
        //关闭IO
        void closeIO(String mac, String code);
        void calibrationFeeder(String mac, String code, double feeder);

        //获取加密摄像头配置
        void getNotAvailableCams(String mac, String key, String pass);
        //获取摄像头配置
        void getCamsConfig(String mac);
        //请求推流
        void doCamsPlay(String mac, List<CamsUseable> usable_cams, String playKey, int camsIndex);
        //切换清晰度
        void changeProfile(String mac, String playKey, String profileToken, String label);

        //获取天气
        void getWeather(String mac);
    }
}
