package com.ypcxpt.fish.main.model;

import java.io.Serializable;
import java.util.List;

/**
 * @作者 Lenny
 * @时间 2018/12/29 13:30
 */
public class BannerInfo implements Serializable {
    /**
     * success : true
     * data : [{"id":"2b67e580-4fa8-44b9-99a4-d4956dd675b0","title":"tets===","banner_image":"{\"url\":\"http://192.168.0.74:9002/rms_files//bb33b5cdaee042caf3cfa7df82566ba6.jpg\",\"name\":\"109951163091896105\"}","small_image":"{\"url\":\"http://192.168.0.74:9002/rms_files//2c14b7502e2616e7158f281ccfddfbb4.jpg\",\"name\":\"20180828161818552\"}","audio":"","video":"","content":"<p class=\"medium-insert-active\"><\/p><div class=\"medium-insert-buttons\" contenteditable=\"false\" style=\"left: 24px; top: 16px;\">\n    <button class=\"medium-insert-buttons-show medium-insert-buttons-rotate\" type=\"button\"><span>+<\/span><\/button>\n    <ul class=\"medium-insert-buttons-addons\" style=\"display: block;\">\n            <li><button data-addon=\"images\" data-action=\"add\" class=\"medium-insert-action\" type=\"button\"><span class=\"fa fa-camera\"><\/span><\/button><\/li>\n            <li><button data-addon=\"embeds\" data-action=\"add\" class=\"medium-insert-action\" type=\"button\"><span class=\"fa fa-youtube-play\"><\/span><\/button><\/li>\n    <\/ul>\n<\/div>","create_time":"2018-12-29T05:25:23.000Z","update_time":"2018-12-29T05:25:56.000Z","creater_id":"1","view_count":0,"type":2,"banner":1,"deleted":0,"comment":1,"creater_name":"钱钱的小河","star_count":0}]
     */

    private boolean success;
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 2b67e580-4fa8-44b9-99a4-d4956dd675b0
         * title : tets===
         * banner_image : {"url":"http://192.168.0.74:9002/rms_files//bb33b5cdaee042caf3cfa7df82566ba6.jpg","name":"109951163091896105"}
         * small_image : {"url":"http://192.168.0.74:9002/rms_files//2c14b7502e2616e7158f281ccfddfbb4.jpg","name":"20180828161818552"}
         * audio :
         * video :
         * content : <p class="medium-insert-active"></p><div class="medium-insert-buttons" contenteditable="false" style="left: 24px; top: 16px;">
         <button class="medium-insert-buttons-show medium-insert-buttons-rotate" type="button"><span>+</span></button>
         <ul class="medium-insert-buttons-addons" style="display: block;">
         <li><button data-addon="images" data-action="add" class="medium-insert-action" type="button"><span class="fa fa-camera"></span></button></li>
         <li><button data-addon="embeds" data-action="add" class="medium-insert-action" type="button"><span class="fa fa-youtube-play"></span></button></li>
         </ul>
         </div>
         * create_time : 2018-12-29T05:25:23.000Z
         * update_time : 2018-12-29T05:25:56.000Z
         * creater_id : 1
         * view_count : 0
         * type : 2
         * banner : 1
         * deleted : 0
         * comment : 1
         * creater_name : 钱钱的小河
         * star_count : 0
         */

        private String id;
        private String title;
        private String banner_image;
        private String small_image;
        private String audio;
        private String video;
        private String content;
        private String create_time;
        private String update_time;
        private String creater_id;
        private int view_count;
        private int type;
        private int banner;
        private int deleted;
        private int comment;
        private String creater_name;
        private int star_count;

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

        public String getBanner_image() {
            return banner_image;
        }

        public void setBanner_image(String banner_image) {
            this.banner_image = banner_image;
        }

        public String getSmall_image() {
            return small_image;
        }

        public void setSmall_image(String small_image) {
            this.small_image = small_image;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getCreater_id() {
            return creater_id;
        }

        public void setCreater_id(String creater_id) {
            this.creater_id = creater_id;
        }

        public int getView_count() {
            return view_count;
        }

        public void setView_count(int view_count) {
            this.view_count = view_count;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getBanner() {
            return banner;
        }

        public void setBanner(int banner) {
            this.banner = banner;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
        }

        public String getCreater_name() {
            return creater_name;
        }

        public void setCreater_name(String creater_name) {
            this.creater_name = creater_name;
        }

        public int getStar_count() {
            return star_count;
        }

        public void setStar_count(int star_count) {
            this.star_count = star_count;
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
