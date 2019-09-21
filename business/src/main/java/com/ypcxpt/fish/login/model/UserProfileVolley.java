package com.ypcxpt.fish.login.model;

import java.io.Serializable;

public class UserProfileVolley implements Serializable {

    private boolean success;
    private int code;
    private UserProfile data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserProfile getData() {
        return data;
    }

    public void setData(UserProfile data) {
        this.data = data;
    }
}
