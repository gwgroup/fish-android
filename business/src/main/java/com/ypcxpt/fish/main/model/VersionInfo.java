package com.ypcxpt.fish.main.model;

import java.io.Serializable;

/**
 * @ProjectName: fish-android
 * @Package: com.ypcxpt.fish.main.model
 * @ClassName: VersionInfo
 * @Description: java类作用描述
 * @Author: xulailing
 * @CreateDate: 2020/3/30 22:41
 * @UpdateUser: xulailing
 * @UpdateDate: 2020/3/30 22:41
 * @UpdateRemark: 更新说明
 */
public class VersionInfo implements Serializable {
    /**
     * version : 1.0.6
     * describe : 云台控制
     */

    private String version;
    private String describe;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
