package com.dreampany.framework.data.model;

import com.google.common.primitives.Longs;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by air on 10/4/17.
 */

public abstract class BaseItem<VH extends FlexibleViewHolder> extends AbstractFlexibleItem<VH> {

    protected long id;

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof BaseItem) {
            BaseItem item = (BaseItem) inObject;
            return this.id == item.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Longs.hashCode(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
