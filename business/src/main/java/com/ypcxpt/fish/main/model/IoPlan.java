package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypcxpt.fish.library.util.Logger;

/**
 * 定时计划Model。
 */
public class IoPlan implements Parcelable {
    public String id;
    public String io_code;
    public String io_type;
    public String io_name;
    public String per;
    public int day_of_month;
    public int day_of_week;
    public int hour;
    public int minute;
    public int second;
    public double weight;//投放多少克
    public boolean enabled;//启用禁用计划
    public boolean io_enabled;//设备是否启用
    public int duration;

    public IoPlan() {
    }

    public IoPlan(String io_code, String io_type, String io_name) {
        this.io_code = io_code;
        this.io_type = io_type;
        this.io_name = io_name;
    }


    @Override
    public String toString() {
        Logger.i("IoPlan-->", "{" +
                "id='" + id + '\'' +
                "io_code='" + io_code + '\'' +
                ", io_type='" + io_type + '\'' +
                '}');

        return "IoPlan{" +
                "id='" + id + '\'' +
                "io_code='" + io_code + '\'' +
                ", io_type='" + io_type + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.io_code);
        dest.writeString(this.io_type);
        dest.writeString(this.io_name);
        dest.writeString(this.per);
        dest.writeInt(this.day_of_month);
        dest.writeInt(this.day_of_week);
        dest.writeInt(this.hour);
        dest.writeInt(this.minute);
        dest.writeInt(this.second);
        dest.writeDouble(this.weight);
        dest.writeInt(this.enabled ? 1 : 0);
        dest.writeInt(this.io_enabled ? 1 : 0);
        dest.writeInt(this.duration);
    }

    protected IoPlan(Parcel in) {
        this.id = in.readString();
        this.io_code = in.readString();
        this.io_type = in.readString();
        this.io_name = in.readString();
        this.per = in.readString();
        this.day_of_month = in.readInt();
        this.day_of_week = in.readInt();
        this.hour = in.readInt();
        this.minute = in.readInt();
        this.second = in.readInt();
        this.weight = in.readDouble();
        this.enabled = in.readInt() == 1;
        this.io_enabled = in.readInt() == 1;
        this.duration = in.readInt();
    }

    public static final Creator<IoPlan> CREATOR = new Creator<IoPlan>() {
        @Override
        public IoPlan createFromParcel(Parcel source) {
            return new IoPlan(source);
        }

        @Override
        public IoPlan[] newArray(int size) {
            return new IoPlan[size];
        }
    };
}
