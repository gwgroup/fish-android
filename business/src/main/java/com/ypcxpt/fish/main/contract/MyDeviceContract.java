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

        void showScenes(List<Scenes> scenes);

        void showIoStatus(List<IoInfo> ioInfos);

        void displayCamsCount(List<CamsUseable> usable_cams);

        void showVLCVideo(List<CamsUseable> usable_cams, String playKey, int camsIndex);
    }

    interface Presenter extends IPresenter {
        //添加场景
        void addScenes(String mac, String name);
        //移除场景
        void removeScenes(String mac);
        //重命名场景
        void renameScenes(String mac, String name);
        //获取场景
        void getScenes();
        //获取设备IO配置信息
        void getIoStatus(String mac);
        //打开IO
        void openIO(String mac, String code, int duration);
        //关闭IO
        void closeIO(String mac, String code);
        void calibrationFeeder(String mac, String code, double feeder);

        //获取摄像头配置
        void getCamsConfig(String mac);
        //请求推流
        void doCamsPlay(String mac, List<CamsUseable> usable_cams, String playKey, int camsIndex);
    }
}
