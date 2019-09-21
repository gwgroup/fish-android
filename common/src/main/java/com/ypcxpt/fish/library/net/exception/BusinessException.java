package com.ypcxpt.fish.library.net.exception;

import com.ypcxpt.fish.library.net.response.BizMsg;

public class BusinessException extends RuntimeException {
    public BizMsg bizMsg;

    public BusinessException(int code, String msg) {
        bizMsg = new BizMsg(code, msg);
    }

    public BizMsg getBizMsg() {
        return bizMsg;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "bizMsg=" + bizMsg +
                '}';
    }

}
