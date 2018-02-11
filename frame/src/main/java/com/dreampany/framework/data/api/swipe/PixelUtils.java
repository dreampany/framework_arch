package com.dreampany.framework.data.api.swipe;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

class PixelUtils {
    /**
     * Converts dps to pixels nicely.
     * @param context the Context for getting the resources
     * @param dp dimension in dps
     * @return dimension in pixels
     */
    protected static float dpToPixel(Context context, float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }
}