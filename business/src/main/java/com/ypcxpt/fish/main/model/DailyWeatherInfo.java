package com.ypcxpt.fish.main.model;

import com.google.gson.annotations.SerializedName;

import com.ypcxpt.fish.library.util.Logger;

import static com.ypcxpt.fish.app.util.DisplayUtils.getPureTemp;

public class DailyWeatherInfo {
    /**
     * 数据结构：
     * "date":"14日星期日",
     * "type":"阴"
     * "high":"高温 22℃",
     * "low":"低温 18℃",
     * "fx":"西风",
     * "fl":"<![CDATA[<3级]]>",
     */
    public String date;
    @SerializedName("type")
    public String weather;
    @SerializedName("high")
    public String higherTemp;
    @SerializedName("low")
    public String lowerTemp;
    @SerializedName("fengxiang")
    public String windDirection;
    @SerializedName("fengli")
    public String windStrength;

    public String getLowerTemp() {
        return getPureTemp(lowerTemp).replace("低温 ", "");
    }

    public String getHigherTemp() {
        return getPureTemp(higherTemp).replace("高温 ", "");
    }

    @Override
    public String toString() {
        Logger.i("日天气数据-->", "{" +
                "date='" + date + '\'' +
                ", weather='" + weather + '\'' +
                ", higherTemp='" + higherTemp + '\'' +
                ", lowerTemp='" + lowerTemp + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", windStrength='" + windStrength + '\'' +
                '}');
        return "DailyWeatherInfo{" +
                "date='" + date + '\'' +
                ", weather='" + weather + '\'' +
                ", higherTemp='" + higherTemp + '\'' +
                ", lowerTemp='" + lowerTemp + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", windStrength='" + windStrength + '\'' +
                '}';
    }

}
