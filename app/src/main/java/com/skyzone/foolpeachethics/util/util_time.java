package com.skyzone.foolpeachethics.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Skyzone on 2/23/2017.
 */

public class util_time {

    public static String parseTime(String date) {
        try {
            SimpleDateFormat format_cache = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
            Date date_start = format_cache.parse(date);
            SimpleDateFormat to_format = new SimpleDateFormat("yyyy MM dd HH:mm");
            return to_format.format(date_start);
        } catch (ParseException e) {
            return "";
        }
    }

    public static long getDiff2Now(String time) {
        SimpleDateFormat format_cache = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        try {
            Date date = format_cache.parse(time);
            long second = (System.currentTimeMillis() - date.getTime()) / 1000;
            return second;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean getDiff(String time_start, String time_end) {
        SimpleDateFormat format_cache = new SimpleDateFormat("yy-MM-dd HH:mm:SS");
        try {
            Date date_start = format_cache.parse(time_start);
            Date date_end = format_cache.parse(time_end);
            long min = (date_end.getTime() - date_start.getTime()) / 1000 / 60;
            if (min > 5) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("date catch");
            return true;
        }
    }

    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:SS");
        return format.format(new Date(System.currentTimeMillis()));
    }
}
