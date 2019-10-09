package com.ypcxpt.fish.library.net.response;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import com.ypcxpt.fish.library.net.exception.ApiException;
import com.ypcxpt.fish.library.net.exception.BusinessException;

import java.io.IOException;

import okhttp3.ResponseBody;

public class ResponseProcessor<T> {

    public T process(Gson gson, TypeAdapter<T> adapter, ResponseBody value) throws IOException {
        ResponseModel<T> response = gson.fromJson(value.charStream(), ResponseModel.class);

        if (response == null) {
            throw new ApiException(ApiException.RESPONSE_NULL);
        }

        if (response.code == 1000) {
            T data = response.data;
            if (data == null) {
                throw new ApiException(ApiException.DATA_NULL);
            } else {
                return adapter.fromJson(gson.toJson(data));
            }
        } else {
            throw new BusinessException(response.code, response.msg);
        }
    }
}
