package com.ypcxpt.fish.main.model;

import java.io.Serializable;
import java.util.List;

public class CommentInfo implements Serializable {
    /**
     * totalCount : 5
     * rows : [{"id":103,"article_id":"b151ee25-ace5-4bcc-91f5-5684fad0ba53","content":"测试","user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","root_id":null,"parent_id":null,"to_user_id":null,"star":1,"reply_count":1,"deleted":0,"create_time":"2019-01-08T02:52:12.000Z","update_time":"2019-01-08T02:59:19.000Z","p_id":null,"p_article_id":null,"p_content":null,"p_user_id":null,"p_root_id":null,"p_parent_id":null,"p_to_user_id":null,"p_star":null,"p_reply_count":null,"p_deleted":null,"p_create_time":null,"p_update_time":null,"user_name":null,"to_user_name":null,"parent_to_user_name":null},{"id":102,"article_id":"b151ee25-ace5-4bcc-91f5-5684fad0ba53","content":"测试回复弹框","user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","root_id":"101-","parent_id":101,"to_user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","star":0,"reply_count":0,"deleted":0,"create_time":"2019-01-08T02:51:01.000Z","update_time":"2019-01-08T02:51:01.000Z","p_id":101,"p_article_id":"b151ee25-ace5-4bcc-91f5-5684fad0ba53","p_content":"测试弹框","p_user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","p_root_id":null,"p_parent_id":null,"p_to_user_id":null,"p_star":0,"p_reply_count":1,"p_deleted":0,"p_create_time":"2019-01-08T02:48:58.000Z","p_update_time":"2019-01-08T02:51:01.000Z","user_name":null,"to_user_name":null,"parent_to_user_name":null},{"id":101,"article_id":"b151ee25-ace5-4bcc-91f5-5684fad0ba53","content":"测试弹框","user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","root_id":null,"parent_id":null,"to_user_id":null,"star":0,"reply_count":1,"deleted":0,"create_time":"2019-01-08T02:48:58.000Z","update_time":"2019-01-08T02:51:01.000Z","p_id":null,"p_article_id":null,"p_content":null,"p_user_id":null,"p_root_id":null,"p_parent_id":null,"p_to_user_id":null,"p_star":null,"p_reply_count":null,"p_deleted":null,"p_create_time":null,"p_update_time":null,"user_name":null,"to_user_name":null,"parent_to_user_name":null},{"id":100,"article_id":"b151ee25-ace5-4bcc-91f5-5684fad0ba53","content":"测试弹框","user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","root_id":null,"parent_id":null,"to_user_id":null,"star":0,"reply_count":0,"deleted":0,"create_time":"2019-01-08T02:47:56.000Z","update_time":"2019-01-08T02:47:56.000Z","p_id":null,"p_article_id":null,"p_content":null,"p_user_id":null,"p_root_id":null,"p_parent_id":null,"p_to_user_id":null,"p_star":null,"p_reply_count":null,"p_deleted":null,"p_create_time":null,"p_update_time":null,"user_name":null,"to_user_name":null,"parent_to_user_name":null},{"id":99,"article_id":"b151ee25-ace5-4bcc-91f5-5684fad0ba53","content":"测试弹框","user_id":"b2aa6c2a-6f93-41e4-af85-a5259b639d38","root_id":null,"parent_id":null,"to_user_id":null,"star":0,"reply_count":0,"deleted":0,"create_time":"2019-01-08T02:46:30.000Z","update_time":"2019-01-08T02:46:30.000Z","p_id":null,"p_article_id":null,"p_content":null,"p_user_id":null,"p_root_id":null,"p_parent_id":null,"p_to_user_id":null,"p_star":null,"p_reply_count":null,"p_deleted":null,"p_create_time":null,"p_update_time":null,"user_name":null,"to_user_name":null,"parent_to_user_name":null}]
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
         * id : 103
         * article_id : b151ee25-ace5-4bcc-91f5-5684fad0ba53
         * content : 测试
         * user_id : b2aa6c2a-6f93-41e4-af85-a5259b639d38
         * root_id : null
         * parent_id : null
         * to_user_id : null
         * star : 1
         * reply_count : 1
         * deleted : 0
         * create_time : 2019-01-08T02:52:12.000Z
         * update_time : 2019-01-08T02:59:19.000Z
         * p_id : null
         * p_article_id : null
         * p_content : null
         * p_user_id : null
         * p_root_id : null
         * p_parent_id : null
         * p_to_user_id : null
         * p_star : null
         * p_reply_count : null
         * p_deleted : null
         * p_create_time : null
         * p_update_time : null
         * user_name : Lenny
         * to_user_name : null
         * parent_to_user_name : null
         * article_title : 按摩椅须知
         * article_banner_image : {"url":"https://files.reead.net/rms_files/a088f9db38155a32d35aa755108a35fd.png","name":"another0095"}
         * article_small_image : {"url":"https://files.reead.net/rms_files/88632b1331d935464d9310ec1a0e09d3.jpg","name":"1099511630918961051"}
         * article_create_time : 2019-01-07T09:09:11.000Z
         */

        private int id;
        private String icon;
        private String article_id;
        private String content;
        private String user_id;
        private String root_id;
        private String parent_id;
        private String to_user_id;
        private int star;
        private int reply_count;
        private int deleted;
        private String create_time;
        private String update_time;
        private String p_id;
        private String p_article_id;
        private String p_content;
        private String p_user_id;
        private String p_root_id;
        private String p_parent_id;
        private String p_to_user_id;
        private String p_star;
        private String p_reply_count;
        private String p_deleted;
        private String p_create_time;
        private String p_update_time;
        private String user_name;
        private String to_user_name;
        private String parent_to_user_name;
        private String article_title;
        private String article_banner_image;
        private String article_small_image;
        private String article_create_time;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getArticle_id() {
            return article_id;
        }

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getRoot_id() {
            return root_id;
        }

        public void setRoot_id(String root_id) {
            this.root_id = root_id;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getTo_user_id() {
            return to_user_id;
        }

        public void setTo_user_id(String to_user_id) {
            this.to_user_id = to_user_id;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public int getReply_count() {
            return reply_count;
        }

        public void setReply_count(int reply_count) {
            this.reply_count = reply_count;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getP_id() {
            return p_id;
        }

        public void setP_id(String p_id) {
            this.p_id = p_id;
        }

        public String getP_article_id() {
            return p_article_id;
        }

        public void setP_article_id(String p_article_id) {
            this.p_article_id = p_article_id;
        }

        public String getP_content() {
            return p_content;
        }

        public void setP_content(String p_content) {
            this.p_content = p_content;
        }

        public String getP_user_id() {
            return p_user_id;
        }

        public void setP_user_id(String p_user_id) {
            this.p_user_id = p_user_id;
        }

        public String getP_root_id() {
            return p_root_id;
        }

        public void setP_root_id(String p_root_id) {
            this.p_root_id = p_root_id;
        }

        public String getP_parent_id() {
            return p_parent_id;
        }

        public void setP_parent_id(String p_parent_id) {
            this.p_parent_id = p_parent_id;
        }

        public String getP_to_user_id() {
            return p_to_user_id;
        }

        public void setP_to_user_id(String p_to_user_id) {
            this.p_to_user_id = p_to_user_id;
        }

        public String getP_star() {
            return p_star;
        }

        public void setP_star(String p_star) {
            this.p_star = p_star;
        }

        public String getP_reply_count() {
            return p_reply_count;
        }

        public void setP_reply_count(String p_reply_count) {
            this.p_reply_count = p_reply_count;
        }

        public String getP_deleted() {
            return p_deleted;
        }

        public void setP_deleted(String p_deleted) {
            this.p_deleted = p_deleted;
        }

        public String getP_create_time() {
            return p_create_time;
        }

        public void setP_create_time(String p_create_time) {
            this.p_create_time = p_create_time;
        }

        public String getP_update_time() {
            return p_update_time;
        }

        public void setP_update_time(String p_update_time) {
            this.p_update_time = p_update_time;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getTo_user_name() {
            return to_user_name;
        }

        public void setTo_user_name(String to_user_name) {
            this.to_user_name = to_user_name;
        }

        public String getParent_to_user_name() {
            return parent_to_user_name;
        }

        public void setParent_to_user_name(String parent_to_user_name) {
            this.parent_to_user_name = parent_to_user_name;
        }

        public String getArticle_title() {
            return article_title;
        }

        public void setArticle_title(String article_title) {
            this.article_title = article_title;
        }

        public String getArticle_banner_image() {
            return article_banner_image;
        }

        public void setArticle_banner_image(String article_banner_image) {
            this.article_banner_image = article_banner_image;
        }

        public String getArticle_small_image() {
            return article_small_image;
        }

        public void setArticle_small_image(String article_small_image) {
            this.article_small_image = article_small_image;
        }

        public String getArticle_create_time() {
            return article_create_time;
        }

        public void setArticle_create_time(String article_create_time) {
            this.article_create_time = article_create_time;
        }
    }

    public static class ImageBean implements Serializable {

        /**
         * url : http://192.168.0.74:9002/rms_files//bb33b5cdaee042caf3cfa7df82566ba6.jpg
         * name : 109951163091896105
         */

        private String url;
        private String name;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
