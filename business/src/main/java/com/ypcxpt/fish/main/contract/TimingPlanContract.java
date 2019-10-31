package com.ypcxpt.fish.main.contract;

import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.presenter.IPresenter;
import com.ypcxpt.fish.library.view.IView;
import com.ypcxpt.fish.main.model.WeatherInfo;

import java.util.List;

public interface TimingPlanContract {
    interface View extends IView {
        void onGetWhetherResult(WeatherInfo weatherInfo);

        void showScenes(List<Scenes> scenes);
    }

    interface Presenter extends IPresenter {
        void connectServices(NetDevice scanResult);

        void addDevice(NetDevice device);

        //解绑移除设备
        void removeDevice(NetDevice device);
        //重命名设备
        void renameDevice(NetDevice device);

        void getScenes();

        //跳转设备详情进行操作
        void skipDetail(NetDevice device);
    }
}
