package com.ypcxpt.fish.device.event;

import com.ypcxpt.fish.core.model.ManualActionModel;

public class OnManualActionItemClickEvent {
    public ManualActionModel data;

    public OnManualActionItemClickEvent(ManualActionModel data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OnManualActionItemClickEvent{" +
                "data=" + data +
                '}';
    }
}
