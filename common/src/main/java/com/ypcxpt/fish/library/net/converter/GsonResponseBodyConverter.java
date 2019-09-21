package com.ypcxpt.fish.library.net.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import com.ypcxpt.fish.library.net.response.ResponseProcessor;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return new ResponseProcessor<T>().process(gson, adapter, value);
        } finally {
            value.close();
        }
    }
}
