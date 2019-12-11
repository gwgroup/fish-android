package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypcxpt.fish.library.util.Logger;

/**
 * 仅仅是网络的Model。
 */
public class CamsNotAvailable implements Parcelable {
    public String key;
    public String hostname;

    public CamsNotAvailable() {
    }

    public CamsNotAvailable(String key, String hostname) {
        this.key = key;
        this.hostname = hostname;
    }


    @Override
    public String toString() {
        Logger.i("CamsNotAvailable-->", "{" +
                "key='" + key + '\'' +
                ", hostname='" + hostname + '\'' +
                '}');

        return "CamsNotAvailable{" +
                "key='" + key + '\'' +
                ", hostname='" + hostname + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.hostname);
    }

    protected CamsNotAvailable(Parcel in) {
        this.key = in.readString();
        this.hostname = in.readString();
    }

    public static final Creator<CamsNotAvailable> CREATOR = new Creator<CamsNotAvailable>() {
        @Override
        public CamsNotAvailable createFromParcel(Parcel source) {
            return new CamsNotAvailable(source);
        }

        @Override
        public CamsNotAvailable[] newArray(int size) {
            return new CamsNotAvailable[size];
        }
    };
}
