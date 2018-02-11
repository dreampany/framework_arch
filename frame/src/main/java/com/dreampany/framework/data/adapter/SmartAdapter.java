package com.dreampany.framework.data.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.dreampany.framework.data.model.BaseItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import eu.davidea.flexibleadapter.databinding.BindingFlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by air on 9/15/17.
 */

public class SmartAdapter<T extends BaseItem> extends BindingFlexibleAdapter<T> {

    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    public SmartAdapter(Object listener) {
        super(listener);
        if (listener instanceof View.OnClickListener) {
            clickListener = (View.OnClickListener) listener;
        }
        if (listener instanceof View.OnLongClickListener) {
            longClickListener = (View.OnLongClickListener) listener;
        }
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public View.OnLongClickListener getLongClickListener() {
        return longClickListener;
    }

    @Override
    public boolean addItem(@NonNull T item) {
        if (contains(item)) {
            updateItem(item);
            return true;
        } else {
            return super.addItem(item);
        }
    }

    @Override
    public boolean addItem(int position, @NonNull T item) {
        if (contains(item)) {
            updateItem(item);
            return true;
        } else {
            return super.addItem(position, item);
        }
    }

    public boolean addItem(@NonNull T item, Comparator<IFlexible> comparator) {
        if (contains(item)) {
            updateItem(item);
            return true;
        } else {
            return super.addItem(calculatePositionFor(item, comparator), item);
        }
    }

    @Override
    public void toggleSelection(int position) {
        super.toggleSelection(position);
        notifyItemChanged(position);
    }

    public int getPosition(T item) {
        return getGlobalPositionOf(item);
    }

    public boolean isAnySelected() {
        return getSelectedItemCount() > 0;
    }

    public void addSelection(T item, boolean selected) {
        int position = getGlobalPositionOf(item);
        if (position >= 0) {
            if (selected) {
                addSelection(position);
            } else {
                removeSelection(position);
            }
            notifyItemChanged(position);
        }
    }

    public void selectAll() {
        super.selectAll();
        notifyDataSetChanged();
    }

    @Override
    public void clearSelection() {
        super.clearSelection();
        notifyDataSetChanged();
    }

    public boolean isSelected(T item) {
        return isSelected(getGlobalPositionOf(item));
    }

    public List<T> getSelectedItems() {
        if (getSelectedItemCount() > 0) {
            List<Integer> positions = getSelectedPositions();
            List<T> items = new ArrayList<>(positions.size());
            for (int position : positions) {
                items.add(getItem(position));
            }
            return items;
        }
        return null;
    }
}
