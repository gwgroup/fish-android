package com.ypcxpt.fish.library.app;

public class GlobalEvent {
    private String msg;

    public GlobalEvent(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "GlobalEvent{" + "msg='" + msg + '\'' + '}';
    }
}
