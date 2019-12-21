package com.ypcxpt.fish.main.event;

import com.ypcxpt.fish.main.model.Scenes;

import java.util.List;

public class OnSceneInfoEvent {
    public List<Scenes> scenes;
    public String macAddress;
    public String sceneName;

    public OnSceneInfoEvent(List<Scenes> scenes, String macAddress, String sceneName) {
        this.scenes = scenes;
        this.macAddress = macAddress;
        this.sceneName = sceneName;
    }

    @Override
    public String toString() {
        return "OnSceneInfoEvent{" +
                "scenes=" + scenes +
                "sceneName=" + sceneName +
                '}';
    }
}
