package com.ypcxpt.fish.login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.ypcxpt.fish.library.util.Logger;

/**
 * 个人信息.
 */
public class User implements Parcelable {
    //    @SerializedName("_id")
    public String id;

    @SerializedName("icon")
    public String avatar;

    public String display_name;

    @SerializedName("mobile")
    public String phoneNo;

    @SerializedName("create_time")
    public String createTime;

    @SerializedName("update_time")
    public String updateTime;

    //只针对一些特殊的需要描述信息的对象,需要返回1,其他情况返回0就可以
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 该方法负责序列化
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.avatar);
        dest.writeString(this.display_name);
        dest.writeString(this.phoneNo);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.avatar = in.readString();
        this.display_name = in.readString();
        this.phoneNo = in.readString();
        this.createTime = in.readString();
        this.updateTime = in.readString();
    }

    /**
     * 负责反序列化
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        /**
         * 从序列化后的对象中创建原始对象
         */
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        /**
         * 创建指定长度的原始对象数组
         */
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return false;
        }
        User targetObj = (User) obj;
        return toString().equals(targetObj.toString());
    }

    @Override
    public String toString() {
        Logger.i("UserProfile-->", "{" +
                "id='" + id + '\'' +
                //                ", avatar='" + avatar + '\'' +
                ", display_name='" + display_name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime=" + updateTime +
                '}');
        return "UserProfile{" +
                "id='" + id + '\'' +
                //                ", avatar='" + avatar + '\'' +
                ", display_name='" + display_name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }

}
