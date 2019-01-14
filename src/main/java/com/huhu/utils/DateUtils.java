package com.huhu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: wilimm
 * @Date: 2019/1/14 18:22
 */
public class DateUtils {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String format(Date date) {
        return dateFormat.format(date);
    }
}
