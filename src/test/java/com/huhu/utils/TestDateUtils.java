package com.huhu.utils;

import java.util.Date;

/**
 * @Author: wilimm
 * @Date: 2019/1/14 18:23
 */
public class TestDateUtils {

    public static void main(String[] args) {
        String format = DateUtils.format(new Date());
        System.out.println(format);
    }
}
