package com.ypcxpt.fish;

import com.ypcxpt.fish.core.app.AppData;

/**
 * 新域名管理
 */
public class BaseUrlConstant {
    /**
     * 后台接口
     */
    public static final String BASE_URL = "https://fish.ypcxpt.com/";

    /**
     * webSocket
     */
    public static final String WEBSOCKET_URI = "wss://fish.ypcxpt.com?token=" + AppData.token();
}
