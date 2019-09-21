package com.ypcxpt.fish.main.event;

import com.ypcxpt.fish.main.model.RabbitMQInfo;

public class OnRabbitMQUpdatedEvent {
    public RabbitMQInfo rabbitMQInfo;

    public OnRabbitMQUpdatedEvent(RabbitMQInfo rabbitMQInfo) {
        this.rabbitMQInfo = rabbitMQInfo;
    }

    @Override
    public String toString() {
        return "OnRabbitMQUpdatedEvent{" +
                "rabbitMQInfo=" + rabbitMQInfo +
                '}';
    }
}
