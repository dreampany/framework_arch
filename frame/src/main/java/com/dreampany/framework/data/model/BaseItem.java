package com.dreampany.framework.data.model;

import com.dreampany.framework.data.util.ColorUtil;
import com.google.common.base.Objects;

import java.io.Serializable;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by air on 10/4/17.
 */

public abstract class BaseItem<VH extends FlexibleViewHolder> extends AbstractFlexibleItem<VH> implements IFlexible<VH>, IFilterable, Serializable {

    protected String id;
    protected Color color;
    protected int layoutId;

    protected BaseItem() {
        color = ColorUtil.getRandColor();
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof BaseItem) {
            BaseItem item = (BaseItem) inObject;
            return this.id.equals(item.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public int getLayoutId() {
        return layoutId;
    }
}
