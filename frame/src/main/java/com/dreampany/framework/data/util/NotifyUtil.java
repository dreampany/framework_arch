package com.dreampany.framework.data.util;


import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.dreampany.framework.R;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

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
            AndroidUtil.getHandler().post(() -> Toast.makeText(context, text, duration).show());
        }
    }

    public static void showInfo(Context context, String info) {
        StyleableToast st = new StyleableToast
                .Builder(context)
                .text(info)
                .textColor(android.graphics.Color.WHITE)
                .backgroundColor(ColorUtil.getColor(context, R.color.colorGreen700))
                .duration(Toast.LENGTH_SHORT)
                .build();
        st.show();
    }

    public static void showError(Context context, String error) {
        StyleableToast st = new StyleableToast
                .Builder(context)
                .text(error)
                .textColor(android.graphics.Color.WHITE)
                .backgroundColor(ColorUtil.getColor(context, R.color.colorRed700))
                .duration(Toast.LENGTH_SHORT)
                .build();
        st.show();
    }
}
