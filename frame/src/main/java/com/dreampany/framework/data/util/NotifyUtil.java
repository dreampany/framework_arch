package com.dreampany.framework.data.util;


import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public final class NotifyUtil {
    private NotifyUtil() {
    }

    public static void longToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void shortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, final String text) {
        toast(context, text, Toast.LENGTH_SHORT);
    }

    public static void toast(final Context context, final String text, final int duration) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            Toast.makeText(context, text, duration).show();
        } else {
            AndroidUtil.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, text, duration).show();
                }
            });
        }
    }
}
