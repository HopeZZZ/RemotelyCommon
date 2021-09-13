package com.zhongke.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by cxf on 2018/7/19.
 */

public class ZKDateFormatUtil {

    private static SimpleDateFormat sFormat;
    private static SimpleDateFormat sFormat2;
    private static SimpleDateFormat sFormat3;
    private static SimpleDateFormat sFormat4;
    private static SimpleDateFormat sFormat5;

    static {
        sFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sFormat2 = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        sFormat3 = new SimpleDateFormat("MM.dd-HH:mm:ss");
        sFormat4 = new SimpleDateFormat("yyyy.MM.dd");
        sFormat5 = new SimpleDateFormat("yyyy-MM-dd  HH-mm-ss");
    }


    public static String getCurTimeString() {
        return sFormat.format(new Date());
    }

    public static String getVideoCurTimeString() {
        return sFormat2.format(new Date());
    }

    public static String getCurTimeString2() {
        return sFormat3.format(new Date());
    }

    public static String getCurTimeString3() {
        return sFormat4.format(new Date());
    }

    public static String getCurTimeString5() {
        return sFormat5.format(new Date(System.currentTimeMillis()));
    }

    public static String getDistanceTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;

        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            return "1日";
        }
        day = diff / (24 * 60 * 60);

        if (day == 0) {
            return "1日";
        } else {
            return day + 1 + "日";
        }
    }

    /**
     * 时间戳转换成时间格式
     *
     * @param duration
     * @return
     */
    public static String formatDurationTime(long duration) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }


}
