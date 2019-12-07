package com.ypcxpt.fish.main.model;

public class PlanParam {

    /**
     * id : 431929e1-42f9-fd7b-a412-fb50f004ae52
     * day_of_month : 1
     * day_of_week : 1
     * duration : 6000
     * enabled : true
     * hour : 12
     * io_code : feeder1
     * minute : 0
     * per : day
     * second : 0
     * weight : 150.0
     */

    private String id;
    private int day_of_month;
    private int day_of_week;
    private int duration;
    private boolean enabled;
    private int hour;
    private String io_code;
    private int minute;
    private String per;
    private int second;
    private double weight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDay_of_month() {
        return day_of_month;
    }

    public void setDay_of_month(int day_of_month) {
        this.day_of_month = day_of_month;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getIo_code() {
        return io_code;
    }

    public void setIo_code(String io_code) {
        this.io_code = io_code;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
