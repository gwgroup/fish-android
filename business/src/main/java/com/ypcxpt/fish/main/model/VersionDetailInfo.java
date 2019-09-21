package com.ypcxpt.fish.main.model;

import java.io.Serializable;

/**
 * @Description: ${TODO}
 * @author: xulailing on 2017/8/21 13:58.
 */

public class VersionDetailInfo implements Serializable {

    private String version;
    private String download_url;//下载链接
    private String describe;//有新的版本可使用
    private String version_id;//版本号
    private String introdution;//更新的内容列表
    private String must_update = "1";//0：有更新可忽略1：有更新不可忽略2：无更新
    private String has_version;//是否有更新

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHas_version() {
        return has_version;
    }

    public void setHas_version(String has_version) {
        this.has_version = has_version;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getVersion_id() {
        return version_id;
    }

    public void setVersion_id(String version_id) {
        this.version_id = version_id;
    }

    public String getIntrodution() {
        return introdution;
    }

    public void setIntrodution(String introdution) {
        this.introdution = introdution;
    }

    public String getMust_update() {
        return must_update;
    }

    public void setMust_update(String must_update) {
        this.must_update = must_update;
    }
}
