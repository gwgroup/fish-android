package com.ypcxpt.fish.app.util;

import android.app.Activity;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;

import com.ypcxpt.fish.library.R;
import com.ypcxpt.fish.library.definition.JConsumer;
import com.ypcxpt.fish.library.util.FormatUtils;
import com.ypcxpt.fish.library.util.ResourceUtils;

import java.util.Calendar;
import java.util.Date;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.WheelPicker;

import static com.ypcxpt.fish.library.util.ResourceUtils.getDimension;

public class CommonWheelHelper {

    public static void doCommonSettings(WheelPicker picker) {
        int commonColor = ResourceUtils.getColor(R.color.main_color);

        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);

        picker.setOffset(3);
        picker.setCancelVisible(false);

        picker.setTopHeight(getDimension(R.dimen.dp21));
        picker.setTopLineHeight(getDimension(R.dimen.dp1));

        picker.setContentPadding(getDimension(R.dimen.dp5), getDimension(R.dimen.dp6));
        picker.setTopPadding(getDimension(R.dimen.dp10));

        picker.setTextSize(18);
        picker.setTitleTextSize(17);
        picker.setSubmitTextSize(16);

        picker.setTextColor(commonColor);
        picker.setTitleTextColor(commonColor);
        picker.setSubmitTextColor(commonColor);
        picker.setLabelTextColor(commonColor);
        picker.setTopLineColor(commonColor);
        picker.setDividerColor(commonColor);
    }

    public static DatePicker createCommonDatePicker(Activity activity, JConsumer<String> selectedDate) {
        DatePicker picker = new DatePicker(activity);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        picker.setResetWhileWheel(false);

        picker.setRangeStart(year - 99, 1, 1);
        picker.setRangeEnd(year, month, day);
        picker.setSelectedItem(year - 35, month, day);

        picker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year1, month1, day1) -> selectedDate.accept(getFormatDate(year1, month1, day1)));
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(getFormatDate(year, picker.getSelectedMonth(), picker.getSelectedDay()));
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(getFormatDate(picker.getSelectedYear(), month, picker.getSelectedDay()));

            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(getFormatDate(picker.getSelectedYear(), picker.getSelectedMonth(), day));
            }
        });

        picker.setTitleText(getFormatDate(picker.getSelectedYear(), picker.getSelectedMonth(), picker.getSelectedDay()));
        doCommonSettings(picker);

        return picker;
    }

    public static void showDatePickerWithData(DatePicker datePicker, String date) {
        if (StringUtils.isTrimEmpty(date)) {
            datePicker.show();
        } else {
            String formatDate = DisplayUtils.getFormatDate(date);
            String[] split = formatDate.split("-");
            if (ObjectUtils.isEmpty(split) || split.length != 3) {
                return;
            }
            int year = FormatUtils.getInt(split[0]);
            int month = FormatUtils.getInt(split[1]);
            int day = FormatUtils.getInt(split[2]);
            datePicker.setTitleText(formatDate);
            datePicker.setSelectedItem(year, month, day);

            datePicker.show();
        }
    }

    private static String getFormatDate(String year, String month, String day) {
        return year + "-" + month + "-" + day;
    }

}
