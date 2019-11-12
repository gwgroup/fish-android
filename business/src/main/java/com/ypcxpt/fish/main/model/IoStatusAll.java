package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypcxpt.fish.library.util.Logger;

/**
 * 设备状态信息Model。
 */
public class IoStatusAll implements Parcelable {
    public int online;
    public double water_temperature;
    public double ph;
    public double o2;

    public IoStatusAll() {
    }

    public IoStatusAll(int online) {
        this.online = online;
    }


    @Override
    public String toString() {
        Logger.i("NetDevice-->", "{" +
                ", online='" + online + '\'' +
                '}');

        return "NetDevice{" +
                ", online='" + online + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.online);
        dest.writeDouble(this.water_temperature);
        dest.writeDouble(this.ph);
        dest.writeDouble(this.o2);
    }

    protected IoStatusAll(Parcel in) {
        this.online = in.readInt();
        this.water_temperature = in.readDouble();
        this.ph = in.readDouble();
        this.o2 = in.readDouble();
    }

    public static final Creator<IoStatusAll> CREATOR = new Creator<IoStatusAll>() {
        @Override
        public IoStatusAll createFromParcel(Parcel source) {
            return new IoStatusAll(source);
        }

        @Override
        public IoStatusAll[] newArray(int size) {
            return new IoStatusAll[size];
        }
    };
}
