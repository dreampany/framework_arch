package com.dreampany.framework.data.adapter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuc on 10/14/2016.
 */

public abstract class BaseSearchAdapter<T> extends BaseSelectAdapter<T> {

    public interface Callback<T> {
        List<T> getNewSearchItems(String newText);

        List<T> getNewSearchItems(String newText, List<T> items);
    }

    private final List<T> backupItems;
    private final List<T> searchItems;
    private String searchText;
    private Callback<T> callback;

    public BaseSearchAdapter() {
        this(null);
    }

    public BaseSearchAdapter(Callback<T> callback) {
        backupItems = new ArrayList<>();
        searchItems = new ArrayList<>();
        this.callback = callback;
    }

    public void setCallback(BaseFlagAdapter.Callback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void clear() {
        backupItems.clear();
        searchItems.clear();
        super.clear();
    }

    public void setSearchText(String searchText) {
        if (TextUtils.isEmpty(searchText)) {
            restoreItems();
        } else if (hasNewSearchText(searchText)) {
            filterItems(searchText);
            this.searchText = searchText;
        }
    }

    public boolean hasSearchText() {
        return !TextUtils.isEmpty(searchText);
    }

    public boolean hasNewSearchText(String newText) {
        if (!TextUtils.isEmpty(searchText)) {
            return !searchText.equalsIgnoreCase(newText);
        }
        return !TextUtils.isEmpty(newText);
        //return true;
    }

    public void backupItems() {
        backupItems.clear();
        backupItems.addAll(items);
    }

    public void restoreItems() {
        if (hasSearchText()) {
            items.clear();
            items.addAll(backupItems);
            notifyDataSetChanged();

            searchItems.clear();
            this.searchText = null;
        }
    }


    public void filterItems(String newText) {
        if (callback == null) return;

        if (hasSearchText()) {
            // filter from searchItems
            List<T> newItems = callback.getNewSearchItems(newText, searchItems);
            showSearchItems(newItems);

        } else {
            // fill up searchItems
            List<T> newItems = callback.getNewSearchItems(newText);
            searchItems.clear();
            searchItems.addAll(newItems);
            showSearchItems(searchItems);
        }
    }

    private void showSearchItems(List<T> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }
}
