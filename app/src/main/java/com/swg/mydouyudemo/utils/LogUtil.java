package com.swg.mydouyudemo.utils;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.swg.mydouyudemo.BuildConfig;

/**
 * Created by Administrator on 2017/11/22.
 */

public class LogUtil {

    private static final String DEFAULT_TAG = "MyDouYu";

    private LogUtil() {
    }

    public static void init() {
        Logger.init(DEFAULT_TAG)
                .methodCount(3)
                .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
    }

    public static void d(String message, Object... args) {
        Logger.d(message, args);
    }

    public static void d(Object object) {
        Logger.d(object);
    }

    public static void e(String message, Object... args) {
        Logger.e(message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        Logger.i(message, args);
    }

    public static void v(String message, Object... args) {
        Logger.v(message, args);
    }

    public static void w(String message, Object... args) {
        Logger.w(message, args);
    }

    public static void wtf(String message, Object... args) {
        Logger.wtf(message, args);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void xml(String xml) {
        Logger.xml(xml);
    }

}
