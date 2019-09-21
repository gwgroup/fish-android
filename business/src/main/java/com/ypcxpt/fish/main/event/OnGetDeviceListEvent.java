package com.ypcxpt.fish.main.event;

import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.util.Logger;

import java.util.List;

public class OnGetDeviceListEvent {
    public List<NetDevice> netDeviceList;

    public OnGetDeviceListEvent(List<NetDevice> netDeviceList) {
        this.netDeviceList = netDeviceList;
    }

    @Override
    public String toString() {
        Logger.i("OnGetDeviceListEvent-->", "{" +
                "netDeviceList=" + netDeviceList +
                '}');
        return "OnGetDeviceListEvent{" +
                "netDeviceList=" + netDeviceList +
                '}';
    }

}
