package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypcxpt.fish.library.util.Logger;

/**
 * 仅仅是网络的Model。
 */
public class IoStatus implements Parcelable {
    public String code;
    public String start_time;
    public int opened;
    public int duration;

    public IoStatus() {
    }

    public IoStatus(String code, int opened) {
        this.code = code;
        this.opened = opened;
    }


    @Override
    public String toString() {
        Logger.i("NetDevice-->", "{" +
                "code='" + code + '\'' +
                ", opened='" + opened + '\'' +
                '}');

        return "NetDevice{" +
                "code='" + code + '\'' +
                ", opened='" + opened + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.start_time);
        dest.writeInt(this.opened);
        dest.writeInt(this.duration);
    }

    protected IoStatus(Parcel in) {
        this.code = in.readString();
        this.start_time = in.readString();
        this.opened = in.readInt();
        this.duration = in.readInt();
    }

    public static final Creator<IoStatus> CREATOR = new Creator<IoStatus>() {
        @Override
        public IoStatus createFromParcel(Parcel source) {
            return new IoStatus(source);
        }

        @Override
        public IoStatus[] newArray(int size) {
            return new IoStatus[size];
        }
    };
}
