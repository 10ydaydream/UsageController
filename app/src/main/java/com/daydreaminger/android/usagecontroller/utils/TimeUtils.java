package com.daydreaminger.android.usagecontroller.utils;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间处理工具类
 *
 * @author : daydreaminger
 * @date : 2020/10/1 19:29
 */
public class TimeUtils {
    private static final String TAG = "TimeUtils";

    public static final String FORMAT_PATTERN_YMDHM = "yyyy年MM月dd日 HH:mm ";

    public static final long TIME_SECOND = 1000;
    public static final long TIME_MINUTE = 60000;
    public static final long TIME_HOUR = 3600000;
    public static final long TIME_DAY = 86400000;
    public static final long TIME_WEEK = 604800000;

    /**
     * 时间格式化
     *
     * @param time    时间戳
     * @param pattern
     * @return
     */
    public static String format(long time, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return dateFormat.format(new Date(time));
    }

    public static String format(@NonNull Date time, String pattern) {
        if (time == null) {
            throw new NullPointerException("format time can not be null.");
        }
        return format(time.getTime(), pattern);
    }

    /**
     * 检查时间戳的长度，部分手机获取的时间戳是精确到秒的，也就是只有10位的long类型，小于13位的补全13位，大于等于13位的直接返回。
     *
     * @param time 传入时间
     * @return （处理过后的）时间值
     */
    public static long checkTimeLength(long time) {
        String timeStr = String.valueOf(time);
        if (timeStr.length() < 13) {
            int length = 13 - 13 % timeStr.length();
            while (length > 0) {
                time *= 10;
                length--;
            }
        }
        return time;
    }

    public static long currentTimeZero() {
        return timeZero(System.currentTimeMillis());
    }

    public static long timeZero(long time) {
        long zeroTime = time / TIME_DAY * TIME_DAY;
        return zeroTime;
    }
}
