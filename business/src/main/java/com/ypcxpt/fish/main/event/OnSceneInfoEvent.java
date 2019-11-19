package com.ypcxpt.fish.main.event;

import com.ypcxpt.fish.device.model.Scenes;

import java.util.List;

public class OnSceneInfoEvent {
    public List<Scenes> scenes;
    public String sceneName;

    public OnSceneInfoEvent(List<Scenes> scenes, String sceneName) {
        this.scenes = scenes;
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
