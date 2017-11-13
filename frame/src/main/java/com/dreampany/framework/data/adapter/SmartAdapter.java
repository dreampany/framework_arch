package com.dreampany.framework.data.adapter;

import android.support.annotation.Nullable;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by air on 9/15/17.
 */

public class SmartAdapter extends FlexibleAdapter<AbstractFlexibleItem> {

    public SmartAdapter(@Nullable List<AbstractFlexibleItem> items) {
        super(items);
    }

    public SmartAdapter(@Nullable List<AbstractFlexibleItem> items, @Nullable Object listeners) {
        super(items, listeners);
    }

    public SmartAdapter(@Nullable List<AbstractFlexibleItem> items, @Nullable Object listeners, boolean stableIds) {
        super(items, listeners, stableIds);
    }
}
