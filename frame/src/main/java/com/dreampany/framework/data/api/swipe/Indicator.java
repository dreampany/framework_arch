package com.dreampany.framework.data.api.swipe;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

/**
 * Created by air on 11/3/17.
 */

class Indicator {

    static ShapeDrawable newOne(int size, int color) {
        ShapeDrawable indicator = new ShapeDrawable(new OvalShape());
        indicator.setIntrinsicWidth(size);
        indicator.setIntrinsicHeight(size);
        indicator.getPaint().setColor(color);
        return indicator;
    }
}
