package com.ypcxpt.fish.library.net.request;

import java.util.HashMap;

public class ParamBuilder {
    private HashMap<String, Object> mParamMap;

    public static ParamBuilder newBuilder() {
        return new ParamBuilder();
    }

    private ParamBuilder() {
    }

    public ParamBuilder put(String key, String value) {
        getParamMap().put(key, value);
        return this;
    }

    public HashMap<String, Object> build() {
        return getParamMap();
    }

    private HashMap<String, Object> getParamMap() {
        return mParamMap == null ? mParamMap = new HashMap<>() : mParamMap;
    }

    public ParamBuilder put(String key, Object value) {
        getParamMap().put(key, value);
        return this;
    }
}
