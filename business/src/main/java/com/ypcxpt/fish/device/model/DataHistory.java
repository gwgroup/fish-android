package com.ypcxpt.fish.device.model;

import java.io.Serializable;

public class DataHistory implements Serializable {
    /**
     * identifier : 57:4C:54:49:C1:35
     * action_time : 2018-09-23 12:31:47
     * data :
     * action_code : 1
     */

    private String identifier;
    private String action_time;
    private String data;
    private int action_code;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getAction_time() {
        return action_time;
    }

    public void setAction_time(String action_time) {
        this.action_time = action_time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getAction_code() {
        return action_code;
    }

    public void setAction_code(int action_code) {
        this.action_code = action_code;
    }
}
