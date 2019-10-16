package com.ypcxpt.fish.login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人信息.
 */
public class UserProfile implements Parcelable {
    public User user;
    public List<Scenes> scenes;

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
        dest.writeParcelable(user, 0);
        dest.writeList(this.scenes);
    }

    public UserProfile() {
    }

    protected UserProfile(Parcel in) {
        // 读取对象需要提供一个类加载器去读取,因为写入的时候写入了类的相关信息
        this.user = in.readParcelable(User.class.getClassLoader());
        this.scenes = new ArrayList<>();//有集合定义的时候一定要初始化
        in.readList(this.scenes, NetDevice.class.getClassLoader());//这里获取类加载器
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
                "user='" + user + '\'' +
                ", scenes=" + scenes +
                '}');
        return "UserProfile{" +
                "user='" + user + '\'' +
                ", scenes=" + scenes +
                '}';
    }

}
