package com.dreampany.framework.data.util;

import android.util.Log;

import com.dreampany.framework.BuildConfig;


import java.util.Locale;

/**
 * Created by nuc on 12/16/2016.
 */

public final class LogKit {
    private static String tag = LogKit.class.getSimpleName();
    // public static boolean debug = true;

    // private final static Logger logger = LoggerFactory.getLogger(LogKit.class);

    public static void setTag(String tag) {
        LogKit.tag = tag;
    }

    public static void verbose(String msg) {
        if (!AndroidUtil.isDebug()) return;
        Log.v(tag, msg);
    }

    public static void verbose(String tag, String msg) {
        if (!AndroidUtil.isDebug()) return;
        Log.v(LogKit.tag + " " + tag, msg);
    }

/*    public static void verbose(String tag, String msg) {
        if (!AndroidUtil.isDebug()) return;
        Log.v(tag, msg);
    }*/

    public static void verbose(String msg, Object... format) {
        if (!AndroidUtil.isDebug()) return;
        Log.v(tag, String.format(Locale.getDefault(), msg, format));
    }

/*    public static void verbose(String tag, String msg, Object... format) {
        if (!AndroidUtil.isDebug()) return;
        Log.v(tag, String.format(Locale.getDefault(), msg, format));
    }*/

    public static void error(String msg) {
        if (!AndroidUtil.isDebug()) return;
        //error(tag, msg);
    }

  public static void error(String tag, String msg) {
        if (!AndroidUtil.isDebug()) return;
        Log.e(LogKit.tag, msg);
    }
/*
    public static void error(String tag, String msg, Exception ex) {
        if (!AndroidUtil.isDebug()) return;
        Log.e(LogKit.tag, msg, ex);
    }*/

    public static boolean isAndroid() {
        return System.getProperty("java.vm.name").equalsIgnoreCase("Dalvik");
    }

    public static void log(String unit, String message) {
        if (BuildConfig.DEBUG) {
            if (isAndroid()) {
                Log.d(unit, message);
            } else {
                System.out.println(unit + ": " + message);
            }
        }
    }

    public static void logE(String unit, String message) {
        if (isAndroid()) {
            Log.d(unit, "FATAL ERROR: " + message);
        } else {
            System.out.println(unit + ": FATAL ERROR: " + message);
        }

        throw new RuntimeException(unit + ": FATAL ERROR: " + message);
    }

    public static void logInfo(String text) {
        if (!AndroidUtil.isDebug()) return;
        logInfo(tag, text);
    }

    public static void logInfo(String tag, String text) {
        if (!AndroidUtil.isDebug()) return;
        Log.i(tag, text);
    }
}
