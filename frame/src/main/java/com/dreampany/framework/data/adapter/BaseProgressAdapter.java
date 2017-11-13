package com.dreampany.framework.data.adapter;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by nuc on 11/19/2016.
 */

public abstract class BaseProgressAdapter<T> extends BaseFlagAdapter<T> {

    private final Map<T, Integer> progress;

    public BaseProgressAdapter() {
        progress = Maps.newConcurrentMap();
    }

    public void updateProgress(T item, int progress) {
        this.progress.put(item, progress);
        notifyItemChanged(item);
    }

    public int getProgress(T item) {
        return progress.containsKey(item) ? progress.get(item) : 0;
    }
}
