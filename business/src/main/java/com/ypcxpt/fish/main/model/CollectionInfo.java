package com.ypcxpt.fish.main.model;

import java.io.Serializable;
import java.util.List;

public class CollectionInfo implements Serializable {
    /**
     * totalCount : 2
     * rows : [{"id":"a82e4f0b-04a5-4ab0-9df5-0eeff900f8eb","title":"test99999999","banner_image":"{\"url\":\"http://192.168.0.74:9002/rms_files//d6e1a91d26a30480b5f79edc786fa2b0.jpg\",\"name\":\"1099511630918961051\"}","small_image":"{\"url\":\"http://192.168.0.74:9002/rms_files//62b5eb813da4299bf51886e511e5007f.jpg\",\"name\":\"1099511630918961051\"}","audio":"{\"url\":\"http://192.168.0.74:9002/rms_files//220edcaaadc8b46bed06412fb8b8f21c.mp3\",\"name\":\"茶理理 - 魑魅魍魉\"}","video":"","create_time":"2018-12-29T05:44:51.000Z","update_time":"2018-12-29T05:45:00.000Z","creater_id":"1","view_count":0,"type":2,"banner":1,"deleted":0,"comment":1,"creater":"钱钱的小河"},{"id":"2529ccc9-1d57-41e5-97ab-deeaaf485353","title":"test999","banner_image":"{\"url\":\"http://192.168.0.74:9002/rms_files//105e2ba3e8275a27cd20912bb210b06b.jpg\",\"name\":\"1099511630918961051\"}","small_image":"{\"url\":\"http://192.168.0.74:9002/rms_files//62b5eb813da4299bf51886e511e5007f.jpg\",\"name\":\"1099511630918961051\"}","audio":"{\"url\":\"http://192.168.0.74:9002/rms_files//220edcaaadc8b46bed06412fb8b8f21c.mp3\",\"name\":\"茶理理 - 魑魅魍魉\"}","video":null,"create_time":"2018-12-28T02:15:36.000Z","update_time":"2018-12-30T03:33:39.000Z","creater_id":"1","view_count":0,"type":1,"banner":0,"deleted":0,"comment":1,"creater":"钱钱的小河"}]
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
         * id : a82e4f0b-04a5-4ab0-9df5-0eeff900f8eb
         * title : test99999999
         * banner_image : {"url":"http://192.168.0.74:9002/rms_files//d6e1a91d26a30480b5f79edc786fa2b0.jpg","name":"1099511630918961051"}
         * small_image : {"url":"http://192.168.0.74:9002/rms_files//62b5eb813da4299bf51886e511e5007f.jpg","name":"1099511630918961051"}
         * audio : {"url":"http://192.168.0.74:9002/rms_files//220edcaaadc8b46bed06412fb8b8f21c.mp3","name":"茶理理 - 魑魅魍魉"}
         * video :
         * create_time : 2018-12-29T05:44:51.000Z
         * update_time : 2018-12-29T05:45:00.000Z
         * creater_id : 1
         * view_count : 0
         * type : 2
         * banner : 1
         * deleted : 0
         * comment : 1
         * creater : 钱钱的小河
         */

        private String id;
        private String title;
        private String banner_image;
        private String small_image;
        private String audio;
        private String video;
        private String create_time;
        private String update_time;
        private String creater_id;
        private int view_count;
        private int type;
        private int banner;
        private int deleted;
        private int comment;
        private String creater;

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

        public String getCreater() {
            return creater;
        }

        public void setCreater(String creater) {
            this.creater = creater;
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
