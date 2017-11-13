package com.dreampany.framework.data.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuc on 3/21/2017.
 */

public class BasePagerAdapter<T> extends PagerAdapter {

    protected final List<T> items;
    protected int itemLayoutId;

    protected BasePagerAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(getItemLayoutId(), container, false);
        bindView(itemView);
        container.addView(itemView);
        return itemView;
    }

    protected int getItemLayoutId() {
        return itemLayoutId;
    }

    protected void bindView(View itemView) {
    }
}
