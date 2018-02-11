package com.dreampany.framework.data.api.swipe;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dreampany.framework.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by air on 11/3/17.
 */

public class SwipeSelector extends FrameLayout {

    private static final int DEFAULT_INDICATOR_SIZE = 6;
    private static final int DEFAULT_INDICATOR_MARGIN = 8;

    private static final String STATE_SELECTOR = "STATE_SELECTOR";

    private SwipeAdapter adapter;
    private ViewPager pager;
    private ViewGroup layoutCircle;

    private int itemsXmlResource;
    private String unselectedItemTitle;
    private String unselectedItemDescription;

    public SwipeSelector(@NonNull Context context) {
        this(context, null);
    }

    public SwipeSelector(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeSelector(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwipeSelector(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs) {
        initializeViews(context);
        populateAttrsAndInitAdapter(context, attrs);
        populateItems();
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.content_swipe, this);

        pager = findViewById(R.id.swipePager);
        layoutCircle = findViewById(R.id.layoutCircle);
    }

    private void populateAttrsAndInitAdapter(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SwipeSelector, 0, 0);

        try {
            itemsXmlResource = ta.getResourceId(R.styleable.Swipe_swipe_itemsXmlResource, 0);
            unselectedItemTitle = ta.getString(R.styleable.Swipe_swipe_unselectedItemTitle);
            unselectedItemDescription = ta.getString(R.styleable.Swipe_swipe_unselectedItemDescription);

            int indicatorSize = (int) ta.getDimension(R.styleable.SwipeSelector_swipe_indicatorSize,
                    PixelUtils.dpToPixel(context, DEFAULT_INDICATOR_SIZE));
            int indicatorMargin = (int) ta.getDimension(R.styleable.SwipeSelector_swipe_indicatorMargin,
                    PixelUtils.dpToPixel(context, DEFAULT_INDICATOR_MARGIN));
            int indicatorInActiveColor = ta.getColor(R.styleable.SwipeSelector_swipe_indicatorInActiveColor,
                    ContextCompat.getColor(context, R.color.swipeselector_color_indicator_inactive));
            int indicatorActiveColor = ta.getColor(R.styleable.SwipeSelector_swipe_indicatorActiveColor,
                    ContextCompat.getColor(context, R.color.swipeselector_color_indicator_active));

            int leftButtonResource = ta.getResourceId(R.styleable.SwipeSelector_swipe_leftButtonResource,
                    R.drawable.ic_action_navigation_chevron_left);
            int rightButtonResource = ta.getResourceId(R.styleable.SwipeSelector_swipe_rightButtonResource,
                    R.drawable.ic_action_navigation_chevron_right);

            String customFontPath = ta.getString(R.styleable.SwipeSelector_swipe_customFontPath);
            int titleTextAppearance = ta.getResourceId(R.styleable.SwipeSelector_swipe_titleTextAppearance,
                    -1);
            int descriptionTextAppearance = ta.getResourceId(R.styleable.SwipeSelector_swipe_descriptionTextAppearance,
                    -1);
            int descriptionGravity = ta.getInteger(R.styleable.SwipeSelector_swipe_descriptionGravity,
                    -1);

            adapter = new SwipeAdapter.Builder()
                    .viewPager(pager)
                    .layoutCircle(layoutCircle)
                    .indicatorSize(indicatorSize)
                    .indicatorMargin(indicatorMargin)
                    .normalColor(indicatorInActiveColor)
                    .activeColor(indicatorActiveColor)
                    .customFontPath(customFontPath)
                    .titleTextAppearance(titleTextAppearance)
                    .descriptionTextAppearance(descriptionTextAppearance)
                    .descriptionGravity(descriptionGravity)
                    .build();
            pager.setAdapter(adapter);
        } finally {
            ta.recycle();
        }

    }

    private void populateItems() {
        List<SwipeItem> pendingItems = new ArrayList<>();

        if (unselectedItemTitle != null && unselectedItemDescription != null) {
            SwipeItem item = new SwipeItem(
                    SwipeItem.UNSELECTED_ITEM_VALUE,
                    unselectedItemTitle,
                    unselectedItemDescription
            );

            pendingItems.add(item);
        }

        inflateItemsFromXml(pendingItems, itemsXmlResource);
    }

    private void inflateItemsFromXml(List<SwipeItem> pendingItems, int itemsXmlResource) {
        if (itemsXmlResource != 0) {
            SwipeItemParser parser = new SwipeItemParser(getContext(), itemsXmlResource);
            pendingItems.addAll(parser.parseItems());

            adapter.setItems(pendingItems);
        }
    }

    public void setOnItemSelectedListener(OnSwipeItemSelectedListener listener) {
        adapter.setOnItemSelectedListener(listener);
    }

    public void clear() {
        adapter.clear();
    }

    public void setItems(SwipeItem... swipeItems) {
        adapter.setItems(Arrays.asList(swipeItems));
    }

    public boolean hasSelection() {
        return getSelectedItem().isRealItem();
    }

    public SwipeItem getSelectedItem() {
        if (adapter.getCount() == 0) {
            throw new UnsupportedOperationException("The SwipeSelector " +
                    "doesn't have any items! Use the setItems() method " +
                    "for setting the items before calling getSelectedItem().");
        }

        return adapter.getSelectedItem();
    }

    public void selectItemAt(int position) {
        selectItemAt(position, true);
    }

    public void selectItemAt(int position, boolean animate) {
        adapter.selectItemAt(position, animate);
    }

    public void selectItemWithValue(String value) {
        selectItemWithValue(value, true);
    }

    public void selectItemWithValue(@NonNull String value, boolean animate) {
        adapter.selectItemWithValue(value, animate);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = adapter.onSaveInstanceState();
        bundle.putParcelable(STATE_SELECTOR, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {//Shouldn't be needed, just in case
            Bundle bundle = (Bundle) state;
            adapter.onRestoreInstanceState(bundle);
            state = bundle.getParcelable(STATE_SELECTOR);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

}
