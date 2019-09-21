package com.ypcxpt.fish.core.app;

import com.blankj.utilcode.util.StringUtils;

import com.ypcxpt.fish.library.util.SPHelper;

import static com.ypcxpt.fish.core.app.Constant.SPKey.TOKEN;

public class AppData {
    private static String token;

    public static String token() {
        if (StringUtils.isTrimEmpty(token)) {
            token = SPHelper.getString(TOKEN);
        }
        return token;
    }

    public static void setToken(String token) {
        AppData.token = token;
        SPHelper.putString(TOKEN, token);
    }

}
