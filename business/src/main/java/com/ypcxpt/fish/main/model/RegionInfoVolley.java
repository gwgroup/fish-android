package com.ypcxpt.fish.main.model;

import java.io.Serializable;
import java.util.List;

public class RegionInfoVolley implements Serializable {

    private boolean success;
    private int code;
    private List<RegionInfo> data;

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

    public List<RegionInfo> getData() {
        return data;
    }

    public void setData(List<RegionInfo> data) {
        this.data = data;
    }
}
