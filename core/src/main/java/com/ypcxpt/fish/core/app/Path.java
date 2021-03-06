package com.ypcxpt.fish.core.app;

/**
 * 所有界面的路由维护在此.
 */
public class Path {

    /**
     * 登录模块.
     */
    public static class Login {
        public static final String LOGIN = "/login/login";
        public static final String BIND = "/login/bind";
        public static final String ADVERTISING = "/login/advertising";
        public static final String ADVERTISING_DETAIL = "/login/advertising_detail";
    }

    /**
     * 主页模块.
     */
    public static class Main {
        public static final String MAIN = "/main/main";
        public static final String IO_CONFIG= "/main/ioConfig";
        public static final String ADD_PLAN= "/main/addPlan";
        public static final String ADD_TRIGGER= "/main/addTrigger";
        public static final String NET_SETTING = "/main/netSetting";

        public static final String EDIT_PROFILE = "/main/edit_user_profile";
        public static final String DEVICE_MANAGER = "/main/device_manager";
        public static final String EDIT_FEEDBACK = "/main/edit_feedback";

        public static final String COLLECTION = "/main/collection";
        public static final String COMMENT = "/main/comment";
    }

}