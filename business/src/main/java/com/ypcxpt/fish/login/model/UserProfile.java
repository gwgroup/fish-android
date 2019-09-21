package com.ypcxpt.fish.login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人信息.
 */
public class UserProfile implements Parcelable {
//    @SerializedName("_id")
    public String id;

    @SerializedName("create_time")
    public String createTime;

    @SerializedName("mobile")
    public String phoneNo;

//    @SerializedName("sex")
//    public boolean gender = true;
    @SerializedName("sex")
    public int gender = 1;

    @SerializedName("card_id")
    public String idNumber;

    @SerializedName("icon")
    public String avatar;

    public String name;

    public int height;

    public int weight;

    public String birthday;

    public String region_code;
    public String region_name;
    public String address;

    public String openid;
    public String integral;
    public int xp;
    public int star_count;
    public String unread_notice_count;

    public List<NetDevice> devices;

    public String getGender() {
        return gender == 1 ? "男" : "女";
    }

    //只针对一些特殊的需要描述信息的对象,需要返回1,其他情况返回0就可以
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 该方法负责序列化
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.createTime);
        dest.writeString(this.phoneNo);
//        dest.writeByte(this.gender ? (byte) 1 : (byte) 0);
        dest.writeInt(this.gender);
        dest.writeString(this.idNumber);
        dest.writeString(this.avatar);
        dest.writeString(this.name);
        dest.writeInt(this.height);
        dest.writeInt(this.weight);
        dest.writeString(this.birthday);
        dest.writeString(this.region_code);
        dest.writeString(this.region_name);
        dest.writeString(this.address);
        dest.writeString(this.openid);
        dest.writeString(this.integral);
        dest.writeInt(this.star_count);
        dest.writeString(this.unread_notice_count);
        dest.writeInt(this.xp);
        dest.writeList(this.devices);
    }

    public UserProfile() {
    }

    protected UserProfile(Parcel in) {
        this.id = in.readString();
        this.createTime = in.readString();
        this.phoneNo = in.readString();
//        this.gender = in.readByte() != 0;
        this.gender = in.readInt();
        this.idNumber = in.readString();
        this.avatar = in.readString();
        this.name = in.readString();
        this.height = in.readInt();
        this.weight = in.readInt();
        this.birthday = in.readString();
        this.region_code = in.readString();
        this.region_name = in.readString();
        this.address = in.readString();
        this.openid = in.readString();
        this.integral = in.readString();
        this.star_count = in.readInt();
        this.unread_notice_count = in.readString();
        this.xp = in.readInt();
        this.devices = new ArrayList<NetDevice>();//有集合定义的时候一定要初始化
        in.readList(this.devices, NetDevice.class.getClassLoader());//这里获取类加载器
    }

    /**
     * 负责反序列化
     */
    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        /**
         * 从序列化后的对象中创建原始对象
         */
        @Override
        public UserProfile createFromParcel(Parcel source) {
            return new UserProfile(source);
        }

        /**
         * 创建指定长度的原始对象数组
         */
        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserProfile) {
            return false;
        }
        UserProfile targetObj = (UserProfile) obj;
        return toString().equals(targetObj.toString());
    }

    @Override
    public String toString() {
        Logger.i("UserProfile-->", "{" +
                "id='" + id + '\'' +
                ", createTime='" + createTime + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", gender=" + gender +
                ", idNumber='" + idNumber + '\'' +
//                ", avatar='" + avatar + '\'' +
                ", openid='" + openid + '\'' +
                ", integral='" + integral + '\'' +
                ", star_count='" + star_count + '\'' +
                ", unread_notice_count='" + unread_notice_count + '\'' +
                ", xp='" + xp + '\'' +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", birthday='" + birthday + '\'' +
                ", region_code='" + region_code + '\'' +
                ", region_name='" + region_name + '\'' +
                ", address='" + address + '\'' +
                ", devices=" + devices +
                '}');
        return "UserProfile{" +
                "id='" + id + '\'' +
                ", createTime='" + createTime + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", gender=" + gender +
                ", idNumber='" + idNumber + '\'' +
                ", avatar='" + avatar + '\'' +
                ", openid='" + openid + '\'' +
                ", integral='" + integral + '\'' +
                ", star_count='" + star_count + '\'' +
                ", unread_notice_count='" + unread_notice_count + '\'' +
                ", xp='" + xp + '\'' +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", birthday='" + birthday + '\'' +
                ", region_code='" + region_code + '\'' +
                ", region_name='" + region_name + '\'' +
                ", address='" + address + '\'' +
                ", devices=" + devices +
                '}';
    }

}
