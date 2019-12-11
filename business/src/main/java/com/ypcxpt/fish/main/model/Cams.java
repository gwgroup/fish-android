package com.ypcxpt.fish.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypcxpt.fish.library.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 摄像头配置信息Model。
 */
public class Cams implements Parcelable {
    public List<CamsUseable> usable_cams;
    public List<CamsNotAvailable> not_available_cams;

    public Cams() {
    }

    @Override
    public String toString() {
        Logger.i("Cams-->", "{" +
                "usable_cams='" + usable_cams + '\'' +
                ", not_available_cams='" + not_available_cams + '\'' +
                '}');

        return "Cams{" +
                "usable_cams='" + usable_cams + '\'' +
                ", not_available_cams='" + not_available_cams + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.usable_cams);
        dest.writeList(this.not_available_cams);
    }

    protected Cams(Parcel in) {
        this.usable_cams = new ArrayList<>();//有集合定义的时候一定要初始化
        in.readList(this.usable_cams, CamsUseable.class.getClassLoader());//这里获取类加载器
        this.not_available_cams = new ArrayList<>();//有集合定义的时候一定要初始化
        in.readList(this.not_available_cams, CamsNotAvailable.class.getClassLoader());//这里获取类加载器
    }

    public static final Creator<Cams> CREATOR = new Creator<Cams>() {
        @Override
        public Cams createFromParcel(Parcel source) {
            return new Cams(source);
        }

        @Override
        public Cams[] newArray(int size) {
            return new Cams[size];
        }
    };
}
