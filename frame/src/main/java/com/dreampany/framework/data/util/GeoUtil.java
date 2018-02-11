package com.dreampany.framework.data.util;

import android.content.Context;

import java.util.Locale;

/**
 * Created by air on 2/2/18.
 */

public final class GeoUtil {
    public static String getCountry(Context context) {
        Locale locale;
        if (AndroidUtil.hasNougat()) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale.getDisplayCountry();
    }
}
