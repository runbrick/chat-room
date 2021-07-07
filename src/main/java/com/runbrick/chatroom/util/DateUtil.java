package com.runbrick.chatroom.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间日期类
 */
public final class DateUtil {

    /**
     * 获取当前时间
     *
     * @return Date
     */
    public static Date getTheCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 获取当前格式化的时间
     *
     * @return String
     */
    public static String getTheCurrentTimeString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return dateTimeFormatter.format(now);
    }
}
