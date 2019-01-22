package com.ycbjie.library.utils.time;

import android.support.annotation.IntRange;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateUtils {


    private DateUtils() {}

    public static long getFirstSundayTimeMillisOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int daysFromSunday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        long secondOfToday = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 +
                calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);
        long millisFromSunday = (daysFromSunday * 24 * 60 * 60 + secondOfToday) * 1000;
        return System.currentTimeMillis() - millisFromSunday;
    }

    public static int calNextDayDayOfWeek(int currDayOfWeek) {
        int i = (currDayOfWeek + 1) % 8;
        if (i == 0) i = 1;
        return i;
    }


    private static class FormatDateTimeHolder {
        static SimpleDateFormat mSimpleDateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        static SimpleDateFormat mSimpleDateWeekFormat = new SimpleDateFormat("yyyy年M月d日 EEEE", Locale.CHINA);
        static Date mDate = new Date();
    }

    public static synchronized String formatDateTime(long time) {
        if (time <= 0) throw new IllegalArgumentException("time shouldn't <= 0");
        FormatDateTimeHolder.mDate.setTime(time);
        return FormatDateTimeHolder.mSimpleDateTimeFormat.format(FormatDateTimeHolder.mDate);
    }

    public static synchronized String formatDate(long time) {
        if (time <= 0) throw new IllegalArgumentException("time shouldn't <= 0");
        FormatDateTimeHolder.mDate.setTime(time);
        return FormatDateTimeHolder.mSimpleDateFormat.format(FormatDateTimeHolder.mDate);
    }

    public static synchronized String formatDateWeek(long time) {
        if (time <= 0) throw new IllegalArgumentException("time shouldn't <= 0");
        FormatDateTimeHolder.mDate.setTime(time);
        return FormatDateTimeHolder.mSimpleDateWeekFormat.format(FormatDateTimeHolder.mDate);
    }


    public static String weekNumberToChinese(@IntRange(from = 1, to = 7) int i) {
        switch (i) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
            default:
                return "";
        }
    }


}
