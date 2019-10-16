package com.ypcxpt.fish.main.model;

import java.io.Serializable;

/**
 * @Description: ${TODO}
 * @author: xulailing on 2017/8/21 13:58.
 */

public class VersionDetailInfo implements Serializable {

    /**
     * code : 0
     * data : {"content":"1、sdfsf","file_hash":"b05178bc3b3702c6beddf48f01c3bc16dddb3ef4ef8cdb13939e4917bf061eb7","file_size":9361909,"file_url":"http://192.168.0.207:8888/files/admin/upload/2019/06/18/e259621486789899125ffb1a40621f06.apk","type":1,"update":false,"version":"2.2.7"}
     * message : 成功
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean implements Serializable {
        /**
         * content : 1、sdfsf
         * file_hash : b05178bc3b3702c6beddf48f01c3bc16dddb3ef4ef8cdb13939e4917bf061eb7
         * file_size : 9361909
         * file_url : http://192.168.0.207:8888/files/admin/upload/2019/06/18/e259621486789899125ffb1a40621f06.apk
         * type : 1
         * update : false
         * version : 2.2.7
         */

        private String content;//更新内容
        private String file_hash;
        private int file_size;
        private String file_url;//下载链接
        private int type;//1：有更新可忽略2：有更新不可忽略
        private boolean update;//是否有更新
        private String version;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFile_hash() {
            return file_hash;
        }

        public void setFile_hash(String file_hash) {
            this.file_hash = file_hash;
        }

        public int getFile_size() {
            return file_size;
        }

        public void setFile_size(int file_size) {
            this.file_size = file_size;
        }

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isUpdate() {
            return update;
        }

        public void setUpdate(boolean update) {
            this.update = update;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
