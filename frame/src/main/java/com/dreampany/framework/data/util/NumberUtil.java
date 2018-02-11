package com.dreampany.framework.data.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by nuc on 3/6/2016.
 */
public class NumberUtil {
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    private static Random random = new Random();

    static {
        suffixes.put(1_000L, "K");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String formatCount(long count) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (count == Long.MIN_VALUE) return formatCount(Long.MIN_VALUE + 1);
        if (count < 0) return "-" + formatCount(-count);
        if (count < 1000) return Long.toString(count); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(count);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = count / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String format(long number) {
        //  Locale.setDefault(new Locale("pl", "PL"));
        String formattedNumber = String.format(Locale.getDefault(), "%,d", number);
        return formattedNumber;
    }

    public static int nextRand(int upper) {
        if (upper <= 0) return -1;
        return random.nextInt(upper);
    }

    public static int nextRand(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static int getPercentage(long credits, long total) {
        float ratio = (float) credits / total;

        int percentage = (int) (ratio * 100);

        if (percentage > 100) {
            percentage = 100;
        }

        return percentage;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int percentage(int current, int total) {
        int percent = (current * 100) / total;
        return percent;
    }


}
