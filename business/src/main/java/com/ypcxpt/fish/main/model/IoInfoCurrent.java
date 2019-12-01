package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypcxpt.fish.library.util.Logger;

/**
 * 首页经过组装的model。
 */
public class IoInfoCurrent implements Parcelable {
    public String code;
    public String type;
    public String name;
    public int pin;
    public double weight_per_second;//投喂机的校准数值，标识每秒投放多少克
    public boolean enabled;//启用禁用
    public int power_w;//瓦特

    public String start_time;
    public int opened;//打开关闭
    public int duration;//时长

    public IoInfoCurrent() {
    }

    public IoInfoCurrent(boolean enabled, String code, String type, String name,
                         double weight_per_second, int pin, int power_w, int opened,
                         int duration, String start_time) {
        this.enabled = enabled;
        this.code = code;
        this.type = type;
        this.name = name;
        this.weight_per_second = weight_per_second;
        this.pin = pin;
        this.power_w = power_w;
        this.opened = opened;
        this.duration = duration;
        this.start_time = start_time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public double getWeight_per_second() {
        return weight_per_second;
    }

    public void setWeight_per_second(double weight_per_second) {
        this.weight_per_second = weight_per_second;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPower_w() {
        return power_w;
    }

    public void setPower_w(int power_w) {
        this.power_w = power_w;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getOpened() {
        return opened;
    }

    public void setOpened(int opened) {
        this.opened = opened;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static Creator<IoInfoCurrent> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        Logger.i("IoInfoCurrent-->", "{" +
                "enabled='" + enabled + '\'' +
                "code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", weight_per_second='" + weight_per_second + '\'' +
                ", pin='" + pin + '\'' +
                ", power_w='" + power_w + '\'' +
                ", opened='" + opened + '\'' +
                ", duration='" + duration + '\'' +
                ", start_time='" + start_time + '\'' +
                '}');

        return "IoInfoCurrent{" +
                "enabled='" + enabled + '\'' +
                "code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", weight_per_second='" + weight_per_second + '\'' +
                ", pin='" + pin + '\'' +
                ", power_w='" + power_w + '\'' +
                ", opened='" + opened + '\'' +
                ", duration='" + duration + '\'' +
                ", start_time='" + start_time + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeInt(this.pin);
        dest.writeDouble(this.weight_per_second);
        dest.writeInt(this.enabled ? 1 : 0);
        dest.writeInt(this.power_w);

        dest.writeString(this.start_time);
        dest.writeInt(this.opened);
        dest.writeInt(this.duration);
    }

    protected IoInfoCurrent(Parcel in) {
        this.code = in.readString();
        this.type = in.readString();
        this.name = in.readString();
        this.pin = in.readInt();
        this.weight_per_second = in.readDouble();
        this.enabled = in.readInt() == 1;
        this.power_w = in.readInt();

        this.start_time = in.readString();
        this.opened = in.readInt();
        this.duration = in.readInt();
    }

    public static final Creator<IoInfoCurrent> CREATOR = new Creator<IoInfoCurrent>() {
        @Override
        public IoInfoCurrent createFromParcel(Parcel source) {
            return new IoInfoCurrent(source);
        }

        @Override
        public IoInfoCurrent[] newArray(int size) {
            return new IoInfoCurrent[size];
        }
    };
}
