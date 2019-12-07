package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypcxpt.fish.library.util.Logger;

/**
 * 触发任务Model。
 */
public class IoTrigger implements Parcelable {
    public String id;
    public String io_code;
    public String io_type;
    public String io_name;
    public String monitor;
    public String condition;
    public double condition_val;
    public String operaction;
    public boolean enabled;
    public boolean io_enabled;//设备是否启用
    public int duration;

    public IoTrigger() {
    }

    public IoTrigger(String io_code, String io_type, String io_name) {
        this.io_code = io_code;
        this.io_type = io_type;
        this.io_name = io_name;
    }


    @Override
    public String toString() {
        Logger.i("IoTrigger-->", "{" +
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
        dest.writeString(this.monitor);
        dest.writeString(this.condition);
        dest.writeString(this.operaction);
        dest.writeDouble(this.condition_val);
        dest.writeInt(this.enabled ? 1 : 0);
        dest.writeInt(this.io_enabled ? 1 : 0);
        dest.writeInt(this.duration);
    }

    protected IoTrigger(Parcel in) {
        this.id = in.readString();
        this.io_code = in.readString();
        this.io_type = in.readString();
        this.io_name = in.readString();
        this.monitor = in.readString();
        this.condition = in.readString();
        this.operaction = in.readString();
        this.condition_val = in.readDouble();
        this.enabled = in.readInt() == 1;
        this.io_enabled = in.readInt() == 1;
        this.duration = in.readInt();
    }

    public static final Creator<IoTrigger> CREATOR = new Creator<IoTrigger>() {
        @Override
        public IoTrigger createFromParcel(Parcel source) {
            return new IoTrigger(source);
        }

        @Override
        public IoTrigger[] newArray(int size) {
            return new IoTrigger[size];
        }
    };
}
