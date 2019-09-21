package com.ypcxpt.fish.login.model;

import java.io.Serializable;

/**
 * 微信登录成功后返回的数据.
 */
public class WechatLoginResult implements Serializable {
    private boolean success;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean implements Serializable {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
