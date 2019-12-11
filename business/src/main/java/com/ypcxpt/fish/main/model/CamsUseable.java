package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypcxpt.fish.library.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 摄像头配置信息Model。
 */
public class CamsUseable implements Parcelable {
    public String key;
    public String hostname;
    public List<CamsUseableProfiles> profiles;
    public String preview_image;

    public CamsUseable() {
    }

    @Override
    public String toString() {
        Logger.i("CamsUseable-->", "{" +
                "key='" + key + '\'' +
                ", hostname='" + hostname + '\'' +
                ", preview_image='" + preview_image + '\'' +
                ", profiles='" + profiles + '\'' +
                '}');

        return "CamsUseable{" +
                "key='" + key + '\'' +
                ", hostname='" + hostname + '\'' +
                ", preview_image='" + preview_image + '\'' +
                ", profiles='" + profiles + '\'' +
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
        dest.writeString(this.preview_image);
        dest.writeList(this.profiles);
    }

    protected CamsUseable(Parcel in) {
        this.key = in.readString();
        this.hostname = in.readString();
        this.preview_image = in.readString();
        this.profiles = new ArrayList<>();//有集合定义的时候一定要初始化
        in.readList(this.profiles, CamsUseableProfiles.class.getClassLoader());//这里获取类加载器
    }

    public static final Creator<CamsUseable> CREATOR = new Creator<CamsUseable>() {
        @Override
        public CamsUseable createFromParcel(Parcel source) {
            return new CamsUseable(source);
        }

        @Override
        public CamsUseable[] newArray(int size) {
            return new CamsUseable[size];
        }
    };
}
