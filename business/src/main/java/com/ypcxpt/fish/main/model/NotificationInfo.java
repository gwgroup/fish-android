package com.ypcxpt.fish.main.model;

import java.io.Serializable;
import java.util.List;

public class NotificationInfo implements Serializable {

    /**
     * totalCount : 11
     * rows : [{"id":"79ae8fb7-b05c-4f14-aa3a-80f2affb8aaf","user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","title":"生日快乐","type":1,"create_time":"2019-01-08T03:54:22.000Z","template_id":"404d7f4f-7d95-40e8-a18f-a6044b05892d","read":0,"template_name":"生日模板","user_name":null},{"id":"e2444bb1-390c-49aa-a26c-7741d08e866f","user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","title":"生日快乐","type":1,"create_time":"2019-01-08T03:51:48.000Z","template_id":"404d7f4f-7d95-40e8-a18f-a6044b05892d","read":0,"template_name":"生日模板","user_name":null},{"id":"466ab9f9-610f-4389-bfec-de16e2d4add6","user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","title":"生日快乐","type":1,"create_time":"2019-01-08T03:39:17.000Z","template_id":"404d7f4f-7d95-40e8-a18f-a6044b05892d","read":0,"template_name":"生日模板","user_name":null},{"id":"893a2914-7085-4cf9-ae17-603b60c07a2b","user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","title":"生日快乐","type":1,"create_time":"2019-01-08T03:37:00.000Z","template_id":"404d7f4f-7d95-40e8-a18f-a6044b05892d","read":0,"template_name":"生日模板","user_name":null},{"id":"3407a627-b200-4484-9c03-c6484f1afeb6","user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","title":"生日快乐","type":1,"create_time":"2019-01-08T02:51:17.000Z","template_id":"404d7f4f-7d95-40e8-a18f-a6044b05892d","read":0,"template_name":"生日模板","user_name":null}]
     */

    private int totalCount;
    private List<RowsBean> rows;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Serializable {
        /**
         * id : 79ae8fb7-b05c-4f14-aa3a-80f2affb8aaf
         * user_id : b2aa6c2a-6f93-41e4-af85-a5259b639d38
         * title : 生日快乐
         * type : 1
         * create_time : 2019-01-08T03:54:22.000Z
         * template_id : 404d7f4f-7d95-40e8-a18f-a6044b05892d
         * read : 0
         * template_name : 生日模板
         * user_name : null
         */

        private String id;
        private String user_id;
        private String title;
        private String content;
        private int type;
        private String create_time;
        private String template_id;
        private int read;
        private String template_name;
        private String user_name;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getTemplate_id() {
            return template_id;
        }

        public void setTemplate_id(String template_id) {
            this.template_id = template_id;
        }

        public int getRead() {
            return read;
        }

        public void setRead(int read) {
            this.read = read;
        }

        public String getTemplate_name() {
            return template_name;
        }

        public void setTemplate_name(String template_name) {
            this.template_name = template_name;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }
    }
}
