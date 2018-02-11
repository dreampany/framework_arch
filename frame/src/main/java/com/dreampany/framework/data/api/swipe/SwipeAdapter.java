package com.dreampany.framework.data.api.swipe;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreampany.framework.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by air on 11/3/17.
 */

public class SwipeAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private static final String STATE_CURRENT_POSITION = "STATE_CURRENT_POSITION";
    private static final String TAG_CIRCLE = "TAG_CIRCLE";

    private final Context context;

    private final ViewPager pager;
    private final ViewGroup layoutCircle;

    private final LinearLayout.LayoutParams circleParams;
    private final ShapeDrawable normalCircleDrawable;
    private final ShapeDrawable activeCircleDrawable;


    private Typeface customTypeFace;
    private final int titleTextAppearance;
    private final int descriptionTextAppearance;
    private final int descriptionGravity;

    private OnSwipeItemSelectedListener listener;
    private final List<SwipeItem> items;
    private int currentPosition;

    private SwipeAdapter(Builder builder) {
        context = builder.pager.getContext();

        pager = builder.pager;
        pager.addOnPageChangeListener(this);

        layoutCircle = builder.layoutCircle;
        circleParams = new LinearLayout.LayoutParams(builder.indicatorSize, builder.indicatorSize);
        circleParams.leftMargin = builder.indicatorMargin;

        normalCircleDrawable = Indicator.newOne(builder.indicatorSize, builder.normalColor);
        activeCircleDrawable = Indicator.newOne(builder.indicatorSize, builder.activeColor);

        if (builder.customFontPath != null && (!builder.customFontPath.isEmpty() || builder.customFontPath.length() > 0)) {
            customTypeFace = Typeface.createFromAsset(context.getAssets(),
                    builder.customFontPath);
        }

        titleTextAppearance = builder.titleTextAppearance;
        descriptionTextAppearance = builder.descriptionTextAppearance;
        descriptionGravity = getGravity(builder.descriptionGravity);

        items = new ArrayList<>();
    }

    void setOnItemSelectedListener(OnSwipeItemSelectedListener listener) {
        this.listener = listener;
    }

    void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    void setItems(List<SwipeItem> items) {
        clear();
        this.items.addAll(items);
        currentPosition = 0;
        setActiveIndicator(0);
        notifyDataSetChanged();
    }

    SwipeItem getSelectedItem() {
        if (currentPosition < items.size() && !items.isEmpty()) {
            return items.get(currentPosition);
        }
        return null;
    }

    void selectItemAt(int position, boolean animate) {
        if (position < 0 || position >= items.size()) {
            throw new IndexOutOfBoundsException("This SwipeSelector does " +
                    "not have an item at position " + position + ".");
        }

        pager.setCurrentItem(position, animate);
    }

    void selectItemWithValue(@NonNull String value, boolean animate) {
        boolean itemExists = false;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getValue().equals(value)) {
                pager.setCurrentItem(i, animate);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            throw new IllegalArgumentException("This SwipeSelector " +
                    "does not have an item with the given value " + value + ".");
        }
    }

    Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt(STATE_CURRENT_POSITION, currentPosition);
        return bundle;
    }

    void onRestoreInstanceState(Bundle state) {
        pager.setCurrentItem(state.getInt(STATE_CURRENT_POSITION), true);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LinearLayout layout = (LinearLayout) View.inflate(context, R.layout.item_swipe, null);
        TextView title = layout.findViewById(R.id.viewTitle);
        TextView description = layout.findViewById(R.id.viewDescription);

        SwipeItem item = items.get(position);
        title.setText(item.getTitle());

        if (TextUtils.isEmpty(item.getDescription())) {
            description.setVisibility(View.GONE);
        } else {
            description.setVisibility(View.VISIBLE);
            description.setText(item.getDescription());
        }

        if (customTypeFace != null) {
            title.setTypeface(customTypeFace);
            description.setTypeface(customTypeFace);
        }

        if (titleTextAppearance != -1) {
            setTextAppearanceCompat(title, titleTextAppearance);
        }

        if (descriptionTextAppearance != -1) {
            setTextAppearanceCompat(description, descriptionTextAppearance);
        }

        if (descriptionGravity != -1) {
            description.setGravity(descriptionGravity);
        }

      /*  layout.setPadding(contentLeftPadding,
                sixteenDp,
                contentRightPadding,
                sixteenDp);*/

        container.addView(layout);
        return layout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (getCount() == 0) {
            return;
        }
        setActiveIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setTextAppearanceCompat(TextView textView, int appearanceRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(appearanceRes);
        } else {
            textView.setTextAppearance(textView.getContext(), appearanceRes);
        }
    }

    private void setActiveIndicator(int position) {
        if (layoutCircle.findViewWithTag(TAG_CIRCLE) == null) {
            // No indicators yet, let's make some. Only run once per configuration.
            for (int i = 0; i < getCount(); i++) {
                ImageView indicator = (ImageView) View.inflate(context, R.layout.item_circle, null);

                if (i == position) {
                    indicator.setImageDrawable(activeCircleDrawable);
                } else {
                    indicator.setImageDrawable(normalCircleDrawable);
                }

                indicator.setLayoutParams(circleParams);
                indicator.setTag(TAG_CIRCLE);
                layoutCircle.addView(indicator);
            }
            return;
        }

        ImageView previousActiveIndicator = (ImageView) layoutCircle.getChildAt(currentPosition);
        ImageView nextActiveIndicator = (ImageView) layoutCircle.getChildAt(position);

        previousActiveIndicator.setImageDrawable(normalCircleDrawable);
        nextActiveIndicator.setImageDrawable(activeCircleDrawable);

        currentPosition = position;

        if (listener != null) {
            listener.onItemSelected(getSelectedItem());
        }
    }

    private int getGravity(int gravity) {
        if (gravity == -1) {
            return -1;
        }

        int realGravityValue;

        switch (gravity) {
            case 0:
                realGravityValue = Gravity.START;
                break;
            case 1:
                realGravityValue = Gravity.CENTER_HORIZONTAL;
                break;
            case 2:
                realGravityValue = Gravity.END;
                break;
            default:
                throw new IllegalArgumentException("Invalid value " +
                        "specified for swipe_descriptionGravity. " +
                        "Use \"left\", \"center\", \"right\" or leave " +
                        "blank for default.");
        }

        return realGravityValue;
    }

    static class Builder {

        private ViewPager pager;
        private ViewGroup layoutCircle;

        private int indicatorSize;
        private int indicatorMargin;

        private int normalColor;
        private int activeColor;

        private String customFontPath;
        private int titleTextAppearance;
        private int descriptionTextAppearance;
        private int descriptionGravity;

        Builder() {
        }

        Builder viewPager(ViewPager pager) {
            this.pager = pager;
            return this;
        }

        Builder layoutCircle(ViewGroup layoutCircle) {
            this.layoutCircle = layoutCircle;
            return this;
        }

        Builder indicatorSize(int indicatorSize) {
            this.indicatorSize = indicatorSize;
            return this;
        }

        Builder indicatorMargin(int indicatorMargin) {
            this.indicatorMargin = indicatorMargin;
            return this;
        }

        Builder normalColor(int normalColor) {
            this.normalColor = normalColor;
            return this;
        }

        Builder activeColor(int activeColor) {
            this.activeColor = activeColor;
            return this;
        }

        Builder customFontPath(String customFontPath) {
            this.customFontPath = customFontPath;
            return this;
        }

        Builder titleTextAppearance(int titleTextAppearance) {
            this.titleTextAppearance = titleTextAppearance;
            return this;
        }

        Builder descriptionTextAppearance(int descriptionTextAppearance) {
            this.descriptionTextAppearance = descriptionTextAppearance;
            return this;
        }

        Builder descriptionGravity(int descriptionGravity) {
            this.descriptionGravity = descriptionGravity;
            return this;
        }

        SwipeAdapter build() {
            return new SwipeAdapter(this);
        }
    }


}
