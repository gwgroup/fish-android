package com.ypcxpt.fish.main.model;

import java.io.Serializable;

public class RegionInfo implements Serializable {

    /**
     * code : 1101
     * parent : 11
     * name : 市辖区
     * level : 2
     * root : 0
     */

    private String code;
    private String parent;
    private String name;
    private int level;
    private int root;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRoot() {
        return root;
    }

    public void setRoot(int root) {
        this.root = root;
    }
}
