package com.ypcxpt.fish.main.event;

import com.ypcxpt.fish.login.model.UserProfile;

public class OnProfileUpdatedEvent {
    public UserProfile userProfile;

    public OnProfileUpdatedEvent(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public String toString() {
        return "OnProfileUpdatedEvent{" +
                "userProfile=" + userProfile +
                '}';
    }
}
