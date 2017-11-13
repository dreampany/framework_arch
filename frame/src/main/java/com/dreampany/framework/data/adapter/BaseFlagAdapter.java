package com.dreampany.framework.data.adapter;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nuc on 11/19/2016.
 */

public abstract class BaseFlagAdapter<T> extends BaseSearchAdapter<T> {

    public interface Callback<T> extends BaseSearchAdapter.Callback<T> {
        boolean resolveFlag(Context context, T item);
        void resultOfFlag(int position, boolean favourite);
    }

    //  protected abstract boolean resolveFlag(Context context, T item);

    private final HashMap<T, Boolean> flagItems;
    private Callback<T> callback;

    public BaseFlagAdapter() {
        this(null);
    }

    public BaseFlagAdapter(Callback<T> callback) {
        flagItems = new HashMap<>();
        this.callback = callback;
    }

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void clear() {
        flagItems.clear();
        super.clear();
    }

    public void resolveItem(List<T> items) {
        super.resolveItem(items);
    }

    public int removeItem(T item) {
        flagItems.remove(item);
        return super.removeItem(item);
    }

    public boolean isFavourite(Context context, int position) {
        if (flagItems.containsKey(getItem(position))) {
            return flagItems.get(getItem(position));
        }

        boolean favourite = false;
        if (callback != null) {
            callback.resolveFlag(context, getItem(position));//resolveFlag(context, getItem(position));
        }
        flagItems.put(getItem(position), favourite);
        return favourite;
    }

    public void setFavourite(int position, boolean favourite) {
        if (!isAbsoluteValid(position)) return;
        flagItems.put(getItem(position), favourite);
        notifyItemChanged(position);
    }

/*    public void toggleFavourite(int position) {
        if (!isAbsoluteValid(position)) return;

        if (flagItems.get(position, false)) {
            flagItems.delete(position);
        } else {
            flagItems.put(position, true);
        }

        notifyItemChanged(position);
    }*/
}
