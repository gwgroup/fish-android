package com.ypcxpt.fish.core.ble;

import android.app.Application;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.internal.RxBleLog;

public class BLEClient {
    private static RxBleClient sClient;

    public static void init(Application app) {
        sClient = RxBleClient.create(app);
        RxBleClient.setLogLevel(RxBleLog.DEBUG);
    }

    public static RxBleClient get() {
        return sClient;
    }
}
