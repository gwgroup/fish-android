package com.ypcxpt.fish.login.event;

import com.ypcxpt.fish.login.model.UserProfile;

public class SplashEvent {

    public UserProfile userProfile;

    public boolean isRequestSuccess;

    public SplashEvent(boolean isRequestSuccess, UserProfile userProfile) {
        this.isRequestSuccess = isRequestSuccess;
        this.userProfile = userProfile;
    }

    @Override
    public String toString() {
        return "SplashEvent{" +
                "userProfile=" + userProfile +
                ", isRequestSuccess=" + isRequestSuccess +
                '}';
    }

}
