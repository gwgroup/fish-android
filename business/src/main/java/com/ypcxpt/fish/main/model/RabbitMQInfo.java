package com.ypcxpt.fish.main.model;

import java.io.Serializable;

public class RabbitMQInfo implements Serializable {

    /**
     * type : 1
     * data : {"title":"您好!Lenny,今天是您的生日!"}
     */

    private int type;
    private DataBean data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * title : 您好!Lenny,今天是您的生日!
         */

        private String id;
        private String title;

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
    }
}
