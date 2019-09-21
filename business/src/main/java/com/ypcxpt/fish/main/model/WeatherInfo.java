package com.ypcxpt.fish.main.model;

import com.blankj.utilcode.util.ObjectUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.ypcxpt.fish.app.util.DisplayUtils.getPureTemp;

public class WeatherInfo {
    public static final String DEGREE_FLAG = "℃";

    public DailyWeatherInfo yesterday;

    public List<DailyWeatherInfo> forecast;

    public String city;

    @SerializedName("wendu")
    public String currTemp;

    @SerializedName("ganmao")
    public String note;

    public String aqi;

    public String getCurrTemp() {
        return getPureTemp(currTemp);
    }

    public DailyWeatherInfo getTodayWeather(){
        if (ObjectUtils.isEmpty(forecast)) {
            return null;
        }
        return forecast.get(0);
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "yesterday=" + yesterday +
                ", forecast=" + forecast +
                ", city='" + city + '\'' +
                ", currTemp='" + currTemp + '\'' +
                ", note='" + note + '\'' +
                ", aqi='" + aqi + '\'' +
                '}';
    }

}
