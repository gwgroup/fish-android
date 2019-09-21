package com.ypcxpt.fish.library.net.exception;

public class ApiException extends RuntimeException {

    public static final String RESPONSE_NULL = "整个Response为空";

    public static final String DATA_NULL = "空数据";

    public ApiException(String message) {
        super(message);
    }

}
