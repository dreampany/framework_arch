package com.dreampany.framework.data.util;

/**
 * Created by nuc on 7/11/2015.
 */
public interface Constant {

    int DEFAULT_COUNT = 10;

   // enum Task {INSERT, UPDATE, SELECT, SYNC, ACK}

    long SECOND = 1000L;
    long MINUTE = 60 * SECOND;
    long HALF_MINUTE = 30 * SECOND;
    long HOUR = 60 * MINUTE;
    long DAY = 24 * HOUR;

    long SYNC_PERIOD = 30 * DAY;
    long FEED_PARSE_PERIOD = HOUR;
    long INTERSTITIAL_AD_TIME_PERIOD = HOUR;

    String CHECKED = "checked";
    String SEP_SPACE = " ";

    String YOUTUBE_ANDROID_API_KEY = "AIzaSyBKuoC0E3POnySbHB778HY_ol9LXkIYYpM";
    String YOUTUBE_SERVER_API_KEY = "AIzaSyDmR0vcdHn025CT-0nxjD_HT5UqGqoE77I";
    String ALCHEMY_API_KEY = "445612e666143e124d4abba638c9cacac67f163c";
}