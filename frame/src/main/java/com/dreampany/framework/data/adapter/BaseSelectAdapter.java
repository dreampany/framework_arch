package com.dreampany.framework.data.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuc on 10/14/2016.
 */

public abstract class BaseSelectAdapter<T> extends BaseAdapter<T> {

    private final List<T> selectedItems;

    public BaseSelectAdapter() {
        selectedItems = new ArrayList<>();
    }

    public void clear() {
        super.clear();
        selectedItems.clear();
    }

    public void toggleSelection(int position) {
        T item = getItem(position);
        if (item != null) {
            toggleSelection(item);
        }
    }

    public void toggleSelection(T item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }

        notifyItemChanged(item);
    }

    public void targetSelection(int position) {
        T item = getItem(position);
        if (item != null) {
            targetSelection(item);
        }
    }

    public void targetSelection(T item) {
        clearSelection();
        selectedItems.add(item);
        notifyItemChanged(item);
    }

    public List<T> getSelectedItems() {
        return selectedItems;
    }

    public T getSelectedItem() {
        return hasSelected() ? selectedItems.get(0) : null;
    }

    public boolean hasSelected() {
        return !selectedItems.isEmpty();
    }

    public boolean isSelected(int position) {
        return selectedItems.contains(getItem(position));
    }

    public boolean isSelected(T item) {
        return selectedItems.contains(item);
    }

    public void selectAll() {
        selectedItems.clear();
        selectedItems.addAll(items);
        notifyDataSetChanged();
    }

    public boolean allSelected() {
        return selectedItems.size() == getItemCount();
    }

    public void clearSelection() {
        boolean needRefresh = !selectedItems.isEmpty();
        selectedItems.clear();
        if (needRefresh) {
            notifyDataSetChanged();
        }
    }
}
