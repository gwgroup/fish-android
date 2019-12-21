package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypcxpt.fish.library.util.Logger;

/**
 * WebSocket Model。
 */
public class WebSocketInfo implements Parcelable {
    public int type;
    public String device_mac;
    public IoStatusAll data;

    public WebSocketInfo() {
    }

    public WebSocketInfo(int type, String device_mac, IoStatusAll data) {
        this.type = type;
        this.device_mac = device_mac;
        this.data = data;
    }


    @Override
    public String toString() {
        Logger.i("WebSocketInfo-->", "{" +
                ", type='" + type + '\'' +
                ", device_mac='" + device_mac + '\'' +
                '}');

        return "WebSocketInfo{" +
                ", type='" + type + '\'' +
                ", device_mac='" + device_mac + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.device_mac);
        dest.writeParcelable(data, 0);
    }

    protected WebSocketInfo(Parcel in) {
        this.type = in.readInt();
        this.device_mac = in.readString();
        this.data = in.readParcelable(IoStatusAll.class.getClassLoader());
    }

    public static final Creator<WebSocketInfo> CREATOR = new Creator<WebSocketInfo>() {
        @Override
        public WebSocketInfo createFromParcel(Parcel source) {
            return new WebSocketInfo(source);
        }

        @Override
        public WebSocketInfo[] newArray(int size) {
            return new WebSocketInfo[size];
        }
    };
}
