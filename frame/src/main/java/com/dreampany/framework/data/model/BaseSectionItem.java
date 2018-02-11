package com.dreampany.framework.data.model;

import com.dreampany.framework.data.util.ColorUtil;
import com.google.common.primitives.Longs;

import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by air on 10/4/17.
 */

public abstract class BaseSectionItem<VH extends FlexibleViewHolder, H extends AbstractHeaderItem> extends AbstractSectionableItem<VH, H> {

    protected long id;
    protected Color color;
    private int order;

    protected BaseSectionItem(H header) {
        super(header);
        color = ColorUtil.getRandColor();
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof BaseSectionItem) {
            BaseSectionItem item = (BaseSectionItem) inObject;
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

    public void setColor(Color color) {
        this.color = color;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public int getOrder() {
        return order;
    }
}
