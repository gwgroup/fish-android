package com.ypcxpt.fish.device.model;

import java.io.Serializable;
import java.util.List;

public class RabbitMqData implements Serializable {
    /**
     * user_id :
     * logs : [{"identifier":"","action_time":"","data":null,"action_code":1}]
     */

    private String user_id;
    private List<LogsBean> logs;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<LogsBean> getLogs() {
        return logs;
    }

    public void setLogs(List<LogsBean> logs) {
        this.logs = logs;
    }

    public static class LogsBean implements Serializable {
        /**
         * identifier :
         * action_time :
         * data : null
         * action_code : 1
         */

        private String identifier;
        private String action_time;
        private String data;
        private int action_code;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getAction_time() {
            return action_time;
        }

        public void setAction_time(String action_time) {
            this.action_time = action_time;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public int getAction_code() {
            return action_code;
        }

        public void setAction_code(int action_code) {
            this.action_code = action_code;
        }
    }
}
