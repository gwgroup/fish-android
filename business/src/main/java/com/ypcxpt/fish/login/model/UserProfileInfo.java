package com.ypcxpt.fish.login.model;

import java.io.Serializable;
import java.util.List;

/**
 * @作者 Lenny
 * @时间 2019/10/14 21:14
 */
public class UserProfileInfo implements Serializable {
    /**
     * code : 1000
     * data : {"user":{"id":"f8316388-1562-11e9-ab14-d663bd873d93","icon":null,"display_name":"NewbBeach","mobile":"18616514687","create_time":"2019-01-11T05:49:17.000Z","update_time":"2019-03-23T07:36:16.000Z"},"scenes":[{"device_mac":"b827eb170977","scene_name":"小小渔场1","create_time":"2019-09-11T03:37:33.000Z","update_time":"2019-09-11T03:39:42.000Z"},{"device_mac":"b827eb540371","scene_name":"小小渔场2","create_time":"2019-09-11T03:37:42.000Z","update_time":"2019-09-11T03:39:41.000Z"},{"device_mac":"bbbbbbbbbbbb","scene_name":"重命名场景","create_time":"2019-09-21T13:49:37.000Z","update_time":"2019-09-21T13:49:42.000Z"}]}
     */

    private int code;
    private DataBean data;

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

    public static class DataBean implements Serializable {
        /**
         * user : {"id":"f8316388-1562-11e9-ab14-d663bd873d93","icon":null,"display_name":"NewbBeach","mobile":"18616514687","create_time":"2019-01-11T05:49:17.000Z","update_time":"2019-03-23T07:36:16.000Z"}
         * scenes : [{"device_mac":"b827eb170977","scene_name":"小小渔场1","create_time":"2019-09-11T03:37:33.000Z","update_time":"2019-09-11T03:39:42.000Z"},{"device_mac":"b827eb540371","scene_name":"小小渔场2","create_time":"2019-09-11T03:37:42.000Z","update_time":"2019-09-11T03:39:41.000Z"},{"device_mac":"bbbbbbbbbbbb","scene_name":"重命名场景","create_time":"2019-09-21T13:49:37.000Z","update_time":"2019-09-21T13:49:42.000Z"}]
         */

        private UserBean user;
        private List<ScenesBean> scenes;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<ScenesBean> getScenes() {
            return scenes;
        }

        public void setScenes(List<ScenesBean> scenes) {
            this.scenes = scenes;
        }

        public static class UserBean implements Serializable {
            /**
             * id : f8316388-1562-11e9-ab14-d663bd873d93
             * icon : null
             * display_name : NewbBeach
             * mobile : 18616514687
             * create_time : 2019-01-11T05:49:17.000Z
             * update_time : 2019-03-23T07:36:16.000Z
             */

            private String id;
            private Object icon;
            private String display_name;
            private String mobile;
            private String create_time;
            private String update_time;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Object getIcon() {
                return icon;
            }

            public void setIcon(Object icon) {
                this.icon = icon;
            }

            public String getDisplay_name() {
                return display_name;
            }

            public void setDisplay_name(String display_name) {
                this.display_name = display_name;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
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
        }

        public static class ScenesBean implements Serializable {
            /**
             * device_mac : b827eb170977
             * scene_name : 小小渔场1
             * create_time : 2019-09-11T03:37:33.000Z
             * update_time : 2019-09-11T03:39:42.000Z
             */

            private String device_mac;
            private String scene_name;
            private String create_time;
            private String update_time;

            public String getDevice_mac() {
                return device_mac;
            }

            public void setDevice_mac(String device_mac) {
                this.device_mac = device_mac;
            }

            public String getScene_name() {
                return scene_name;
            }

            public void setScene_name(String scene_name) {
                this.scene_name = scene_name;
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
        }
    }
}
