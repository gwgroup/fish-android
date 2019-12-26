package com.ypcxpt.fish.app.util;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;

import com.ypcxpt.fish.core.R;
import com.ypcxpt.fish.library.util.ResourceUtils;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.main.model.DailyWeatherInfo;
import com.ypcxpt.fish.main.model.WeatherInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ypcxpt.fish.core.app.Constant.NO_DATA;

public class DisplayUtils {

    public static final int DEFAULT_MAX_PROGRESS = 100;

    public static final String MISSING_INFO = "待补充";

    public static final String DEFAULT_PHONE_SEPARATOR = "-";

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEGREE_FLAG = "℃";

    public static final String DEVICE_DETAIL_NO_DATA = "读取中";

    /**
     * @param levelIndex 档位的等级，从0开开始。比如共5档，则从0~4。
     * @param maxLevel   最大几档
     */
    public static void showDeviceProgress(int levelIndex, int maxLevel, ProgressBar pb) {
        if (levelIndex == NO_DATA) {
            pb.setProgress(0);
        } else {
            float rate = 100f * (levelIndex + 1) / maxLevel;
            pb.setProgress((int) rate);
        }
    }

    public static String getFormatPhone(String str) {
        if (StringUtils.isTrimEmpty(str) || str.length() != 11) {
            return null;
        }

        String s1 = str.substring(0, 3);
        String s2 = str.substring(3, 7);
        String s3 = str.substring(7);

        return s1 + DEFAULT_PHONE_SEPARATOR + s2 + DEFAULT_PHONE_SEPARATOR + s3;
    }

    public static String getFormatDate(String strDate) {
        if (StringUtils.isTrimEmpty(strDate)) {
            return null;
        }

        if (strDate.contains("-")) {
            return strDate;
        }

        Date date;
        try {
            date = DateFormat.getDateInstance().parse(strDate);
        } catch (ParseException e) {
            return null;
        }

        String formatDate = getDefaultDateFormat().format(date);
        return formatDate;
    }

    private static SimpleDateFormat getDefaultDateFormat() {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    }

    public static void displayProfileInfo(TextView tv, String info) {
        int textColor;
        String text;
        if (StringUtils.isTrimEmpty(info)) {
            textColor = R.color.user_profile_item_hint;
            text = MISSING_INFO;
        } else {
            textColor = R.color.common_2f2f2f;
            text = info;
        }
        tv.setTextColor(ResourceUtils.getColor(textColor));
        ViewHelper.setText(tv, text);
    }

    public static int getWeatherIcon(WeatherInfo.DataBeanX.DataBean weatherInfo) {
        String weather = null;
        try {
            weather = weatherInfo.getWea();
        } catch (Exception e) {
        }

        if (weatherInfo == null || weatherInfo.getWea() == null || StringUtils.isTrimEmpty(weather)) {
            return R.mipmap.forecast_icon_sunnny;
        }

        if (weather.contains("晴")) {
            return R.mipmap.forecast_icon_sunnny;
        } else if (weather.contains("雨夹雪")) {
            return R.mipmap.forecast_icon_sleet;
        } else if (weather.contains("雨")) {
            if (weather.equals("小雨")) {
                return R.mipmap.forecast_icon_rain;
            } else if (weather.equals("中雨")) {
                return R.mipmap.forecast_icon_moderaterain;
            } else if (weather.equals("大雨")) {
                return R.mipmap.forecast_icon_heavyrain;
            } else if (weather.equals("暴雨")) {
                return R.mipmap.forecast_icon_rainstorm;
            } else if (weather.equals("雷阵雨")) {
                return R.mipmap.forecast_icon_thundershower;
            } else if (weather.equals("阵雨")) {
                return R.mipmap.forecast_icon_shower;
            } else {
                return R.mipmap.forecast_icon_rain;
            }
        } else if (weather.contains("多云")) {
            return R.mipmap.forecast_icon_clound;
        } else if (weather.contains("阴")) {
            return R.mipmap.forecast_icon_overcast;
        } else if (weather.contains("雪")) {
            if (weather.equals("小雪")) {
                return R.mipmap.forecast_icon_snow;
            } else if (weather.equals("中雪")) {
                return R.mipmap.forecast_icon_morderatesnow;
            } else if (weather.equals("大雪")) {
                return R.mipmap.forecast_icon_heavysnow;
            } else if (weather.equals("暴雪")) {
                return R.mipmap.forecast_icon_snowstorm;
            } else {
                return R.mipmap.forecast_icon_snow;
            }
        } else if (weather.contains("霾")) {
            return R.mipmap.forecast_icon_haze;
        } else if (weather.contains("尘")) {
            return R.mipmap.forecast_icon_dust;
        } else if (weather.contains("沙")) {
            return R.mipmap.forecast_icon_sandstorm;
        } else if (weather.contains("雾")) {
            return R.mipmap.forecast_icon_fog;
        } else {
            return R.mipmap.forecast_icon_sunnny;
        }
    }

    public static String getWeatherCollections(WeatherInfo.DataBeanX.DataBean weatherInfo) {
        String lowerTemp = null, higherTemp = null, windDirection = null, windStrength = null;
        if (weatherInfo != null) {
            lowerTemp = weatherInfo.getTem2();
            higherTemp = weatherInfo.getTem1();
            windDirection = weatherInfo.getWin().get(0);
            windStrength = weatherInfo.getWin_speed();
        }

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(lowerTemp)
                .append(" ~ ")
                .append(higherTemp)
                .append("  ")
                .append(windDirection)
                .append(windStrength);

        return sBuilder.toString();
    }

    public static String getPureTemp(String temp) {
        return StringUtils.isEmpty(temp) ? "" : temp.replace(DEGREE_FLAG, "");
    }

}
