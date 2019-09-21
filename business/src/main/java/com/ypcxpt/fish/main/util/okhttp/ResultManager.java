package com.ypcxpt.fish.main.util.okhttp;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.ypcxpt.fish.library.util.Logger;

/**
 * 标准返回结果管理
 */
public class ResultManager<T> {

    private T entity;

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public T resolveEntity(Context context, Class<T> classOfT, String jsonStr, String describeStr) {
//		Logs.i("TAB", "请求类型：" + describeStr);
//		Logs.i("TAB", "第一层返回信息：" + jsonStr);
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
            T result = gson.fromJson(jsonObject, classOfT);
            return result;
        } catch (Exception e) {
            Toast.makeText(context, "请稍后重试", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public T resolveEntity(Class<T> classOfT, String jsonStr, String describeStr) {
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
            T result = gson.fromJson(jsonObject, classOfT);
            return result;
        } catch (Exception e) {
            Logger.e("entity is badconversion ", "--");
            e.printStackTrace();
            return null;
        }
    }

    public T resolveEntity(Class<T> classofT, JsonElement element) {
        Gson gson = new Gson();
        try {
            T result = gson.fromJson(element, classofT);
            return result;
        } catch (Exception e) {
            Logger.e("entity is badconversion ", "--");
            e.printStackTrace();
//			T result = resolveEntity(classofT,element.getAsString(),"");
//			return result;
            return null;
        }
    }
}