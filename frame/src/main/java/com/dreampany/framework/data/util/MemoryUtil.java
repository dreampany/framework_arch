package com.dreampany.framework.data.util;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by nuc on 10/16/2016.
 */

public final class MemoryUtil {
    private MemoryUtil() {
    }

    private static final long defaultDelayTime = 60 * 1000L;
    private static long lastCheckedTime;

    private static final long MIN_SIZE_MB = 10L;
    private static final long MAX_SIZE_MB = Long.MAX_VALUE;

    public static final int ERROR = -1;

    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * get available memory in MB in external storage if exists
     *
     * @return free space in MB, if external storage not exists, returns -1
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = getBlockSize(stat);
            long availableBlocks = getAvailableBlocks(stat);
            return (availableBlocks * blockSize) / (1024 * 1024);
        } else {
            return ERROR;
        }
    }

    static public long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = getBlockSize(stat);
            long totalBlocks = getBlockCount(stat);
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    public static StatFs getInternalStatFs() {
        File path = Environment.getDataDirectory();
        return new StatFs(path.getPath());
    }

    public static StatFs getExternalStatFs() {
        File path = Environment.getExternalStorageDirectory();
        return new StatFs(path.getPath());
    }

    public static long getAvailableInternalMemorySize() {
        StatFs stat = getInternalStatFs();
        long blockSize = getBlockSize(stat);
        long availableBlocks = getAvailableBlocks(stat);
        return (availableBlocks * blockSize) / (1024 * 1024);
    }

    public static boolean hasInternalMemory() {
        if (!DataUtil.isEmpty(lastCheckedTime) && TimeUtil.isExpired(lastCheckedTime, defaultDelayTime)) {
            return true;
        }

        long availableInternalMemory = getAvailableInternalMemorySize();
        return availableInternalMemory >= MIN_SIZE_MB;
    }

    public static long getTotalInternalMemorySize(StatFs stat) {
        long blockSize = getBlockSize(stat);
        long totalBlocks = getBlockCount(stat);
        return totalBlocks * blockSize;
    }

    private static long getBlockSize(StatFs stat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return stat.getBlockSizeLong();
        } else {
            return stat.getBlockSize();
        }
    }

    private static long getBlockCount(StatFs stat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return stat.getBlockCountLong();
        } else {
            return stat.getBlockCount();
        }
    }

    private static long getAvailableBlocks(StatFs stat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return stat.getAvailableBlocksLong();
        } else {
            return stat.getAvailableBlocks();
        }
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }
}
