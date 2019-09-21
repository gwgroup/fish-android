package com.ypcxpt.fish.library.net.response;

public class BizMsg {

    public int code;

    public String msg;

    public BizMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BizMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

}
