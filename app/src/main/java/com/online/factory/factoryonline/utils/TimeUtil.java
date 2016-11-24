package com.online.factory.factoryonline.utils;

import java.text.SimpleDateFormat;

/**
 * 作者: GIndoc
 * 日期: 2016/11/23 17:54
 * 作用:
 */

public class TimeUtil {

    public static String formatTimeStamp(String stringFormat, long timestamp) {
        SimpleDateFormat format =  new SimpleDateFormat(stringFormat);
        return format.format(timestamp);
    }

    public static String vagueTime(long timestamp) {
        long currentTimestamp = System.currentTimeMillis()/1000;
        long interval = currentTimestamp - timestamp;
        if (interval < 60) {
            return "刚刚";
        } else if (interval >= 60 && interval < 60 * 60) {
            return interval / 60 + "分钟前";
        } else if (interval >= 60 * 60 && interval < 60 * 60 * 24) {
            return interval / (60 * 60) + "小时前";
        } else if (interval >= (60 * 60 * 24) && interval < 60 * 60 * 24 * 30) {
            return interval / (60 * 60 * 24)  + "天前";
        } else{
            return formatTimeStamp("yyyy-MM-dd", timestamp);
        }
    }
}
