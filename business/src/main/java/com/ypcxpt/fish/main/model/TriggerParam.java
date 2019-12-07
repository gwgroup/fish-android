package com.ypcxpt.fish.main.model;

public class TriggerParam {
    /**
     * monitor : o2
     * condition : >
     * condition_val : 15.0
     * io_code : aerator2
     * operaction : close
     * duration : 1
     * enabled : false
     * id : 39db9d50-dcd2-4e1e-a32e-a5be7d6f51b0
     */

    private String monitor;
    private String condition;
    private double condition_val;
    private String io_code;
    private String operaction;
    private int duration;
    private boolean enabled;
    private String id;

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getCondition_val() {
        return condition_val;
    }

    public void setCondition_val(double condition_val) {
        this.condition_val = condition_val;
    }

    public String getIo_code() {
        return io_code;
    }

    public void setIo_code(String io_code) {
        this.io_code = io_code;
    }

    public String getOperaction() {
        return operaction;
    }

    public void setOperaction(String operaction) {
        this.operaction = operaction;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
