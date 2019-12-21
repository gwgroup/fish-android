package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.ypcxpt.fish.library.util.Logger;

/**
 * 仅仅是网络的Model。
 */
public class Scenes implements Parcelable {
    @SerializedName("device_mac")
    public String macAddress;
    public String scene_name;//备注名
    public String create_time;
    public String update_time;

    public Scenes() {
    }

    public Scenes(String macAddress, String name, String cteateTime, String updateTime) {
        this.macAddress = macAddress;
        this.scene_name = name;
        this.create_time = cteateTime;
        this.update_time = updateTime;
    }


    @Override
    public String toString() {
        Logger.i("Scenes-->", "{" +
                "macAddress='" + macAddress + '\'' +
                ", scene_name='" + scene_name + '\'' +
                '}');

        return "Scenes{" +
                "macAddress='" + macAddress + '\'' +
                ", scene_name='" + scene_name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.macAddress);
        dest.writeString(this.scene_name);
        dest.writeString(this.create_time);
        dest.writeString(this.update_time);
    }

    protected Scenes(Parcel in) {
        this.macAddress = in.readString();
        this.scene_name = in.readString();
        this.create_time = in.readString();
        this.update_time = in.readString();
    }

    public static final Creator<Scenes> CREATOR = new Creator<Scenes>() {
        @Override
        public Scenes createFromParcel(Parcel source) {
            return new Scenes(source);
        }

        @Override
        public Scenes[] newArray(int size) {
            return new Scenes[size];
        }
    };
}
