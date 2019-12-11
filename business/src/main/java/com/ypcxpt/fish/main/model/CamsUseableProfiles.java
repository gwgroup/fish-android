package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypcxpt.fish.library.util.Logger;

/**
 * 仅仅是网络的Model。
 */
public class CamsUseableProfiles implements Parcelable {
    public String token;
    public String label;
    public String rtsp_url;
    public int width;
    public int height;
    public boolean selected;


    public CamsUseableProfiles() {
    }

    public CamsUseableProfiles(String token, String label, String rtsp_url) {
        this.token = token;
        this.label = label;
        this.rtsp_url = rtsp_url;
    }


    @Override
    public String toString() {
        Logger.i("CamsUseableProfiles-->", "{" +
                "token='" + token + '\'' +
                ", label='" + label + '\'' +
                ", rtsp_url='" + rtsp_url + '\'' +
                '}');

        return "CamsUseableProfiles{" +
                "token='" + token + '\'' +
                ", label='" + label + '\'' +
                ", rtsp_url='" + rtsp_url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.label);
        dest.writeString(this.rtsp_url);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.selected ? 1 : 0);
    }

    protected CamsUseableProfiles(Parcel in) {
        this.token = in.readString();
        this.label = in.readString();
        this.rtsp_url = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.selected = in.readInt() == 1;
    }

    public static final Creator<CamsUseableProfiles> CREATOR = new Creator<CamsUseableProfiles>() {
        @Override
        public CamsUseableProfiles createFromParcel(Parcel source) {
            return new CamsUseableProfiles(source);
        }

        @Override
        public CamsUseableProfiles[] newArray(int size) {
            return new CamsUseableProfiles[size];
        }
    };
}
