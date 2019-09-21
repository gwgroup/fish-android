package com.ypcxpt.fish.login.model;

import com.ypcxpt.fish.library.util.Logger;

/**
 * 登录成功后返回的数据.
 */
public class LoginResult {
    public String token;

    @Override
    public String toString() {
        Logger.i("LoginResult-->", "{" +
                "token='" + token + '\'' +
                '}');
        return "LoginResult{" +
                "token='" + token + '\'' +
                '}';
    }
}
