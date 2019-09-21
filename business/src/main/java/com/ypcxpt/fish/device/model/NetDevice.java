package com.ypcxpt.fish.device.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.polidea.rxandroidble2.scan.ScanResult;

import com.ypcxpt.fish.library.util.Logger;

/**
 * 仅仅是网络的Model。
 */
public class NetDevice implements Parcelable {
    @SerializedName("mac")
    public String macAddress;
    public String type;//设备名
    public String name;//备注名

    public ScanResult scanResult;

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public NetDevice() {
    }

    public NetDevice(String macAddress, String name, String type) {
        this.macAddress = macAddress;
        this.name = name;
        this.type = type;
    }


    @Override
    public String toString() {
        Logger.i("NetDevice-->", "{" +
                "macAddress='" + macAddress + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}');

        return "NetDevice{" +
                "macAddress='" + macAddress + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.macAddress);
        dest.writeString(this.name);
        dest.writeString(this.type);
    }

    protected NetDevice(Parcel in) {
        this.macAddress = in.readString();
        this.name = in.readString();
        this.type = in.readString();
    }

    public static final Creator<NetDevice> CREATOR = new Creator<NetDevice>() {
        @Override
        public NetDevice createFromParcel(Parcel source) {
            return new NetDevice(source);
        }

        @Override
        public NetDevice[] newArray(int size) {
            return new NetDevice[size];
        }
    };
}
