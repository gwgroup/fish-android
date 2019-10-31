package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.ypcxpt.fish.library.util.Logger;

/**
 * 仅仅是网络的Model。
 */
public class IoInfo implements Parcelable {
    public String code;
    public String type;
    public String name;
    public int pin;
    public double weight_per_second;//投喂机的校准数值，标识每秒投放多少克
    public boolean enabled;//启用禁用
    public int power_w;//瓦特

    public IoInfo() {
    }

    public IoInfo(String code, String type, String name, int pin) {
        this.code = code;
        this.type = type;
        this.name = name;
        this.pin = pin;
    }


    @Override
    public String toString() {
        Logger.i("NetDevice-->", "{" +
                "code='" + code + '\'' +
                ", type='" + type + '\'' +
                '}');

        return "NetDevice{" +
                "code='" + code + '\'' +
                ", type='" + type + '\'' +
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
    }

    protected IoInfo(Parcel in) {
        this.code = in.readString();
        this.type = in.readString();
        this.name = in.readString();
        this.pin = in.readInt();
        this.weight_per_second = in.readDouble();
        this.enabled = in.readInt() == 1;
        this.power_w = in.readInt();
    }

    public static final Creator<IoInfo> CREATOR = new Creator<IoInfo>() {
        @Override
        public IoInfo createFromParcel(Parcel source) {
            return new IoInfo(source);
        }

        @Override
        public IoInfo[] newArray(int size) {
            return new IoInfo[size];
        }
    };
}
