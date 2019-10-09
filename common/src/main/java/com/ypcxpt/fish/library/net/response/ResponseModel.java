package com.ypcxpt.fish.library.net.response;

import com.google.gson.annotations.SerializedName;

public class ResponseModel<T> {
    /* 必返回字段 */
    public boolean success;

    /* code=1000时为正确数据 */
    @SerializedName("code")
    public int code = DEFAULT_CODE;

    /* 只有错误时才返回 */
    @SerializedName("message")
    public String msg;

    /* 正确时返回的数据 */
    public T data;

    private static final int DEFAULT_CODE = -1;

    @Override
    public String toString() {
        return "ResponseModel{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}
