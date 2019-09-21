package com.ypcxpt.fish.jpush;

import java.io.Serializable;

/**
 * @作者 Lenny
 * @时间 2019/1/5 15:35
 */
public class JPushInfo implements Serializable {
    private String id;
    private String title;
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
