package com.ypcxpt.fish.login.model;

import java.io.Serializable;

/**
 * @作者 Lenny
 * @时间 2019/10/14 20:33
 */
public class LoginInfo implements Serializable {
    /**
     * code : 1000
     * data : 7ff9a6439cee42d884c398618c2ba162
     */

    private int code;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
