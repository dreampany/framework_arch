package com.dreampany.framework.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.dreampany.framework.R;

import java.util.ArrayList;

/**
 * Created by nuc on 10/17/2016.
 */

public class ParallaxScrollView extends ScrollView {

    private static final int DEFAULT_PARALLAX_VIEWS = 1;
    private static final float DEFAULT_INNER_PARALLAX_FACTOR = 1.9F;
    private static final float DEFAULT_PARALLAX_FACTOR = 1.9F;
    private static final float DEFAULT_ALPHA_FACTOR = -1F;
    private int numOfParallaxViews = DEFAULT_PARALLAX_VIEWS;
    private float innerParallaxFactor = DEFAULT_PARALLAX_FACTOR;
    private float parallaxFactor = DEFAULT_PARALLAX_FACTOR;
    private float alphaFactor = DEFAULT_ALPHA_FACTOR;
    private ArrayList<ParallaxView> parallaxViews = new ArrayList<>();

    public ParallaxScrollView(Context context) {
        this(context, null);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

/*    public ParallaxScrollView(Context getContext, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(getContext, attrs, defStyleAttr, defStyleRes);
        init(getContext, attrs);
    }*/


    protected void init(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ParallaxScroll);
        this.parallaxFactor = typeArray.getFloat(R.styleable.ParallaxScroll_parallax_factor, DEFAULT_PARALLAX_FACTOR);
        this.alphaFactor = typeArray.getFloat(R.styleable.ParallaxScroll_alpha_factor, DEFAULT_ALPHA_FACTOR);
        this.innerParallaxFactor = typeArray.getFloat(R.styleable.ParallaxScroll_inner_parallax_factor, DEFAULT_INNER_PARALLAX_FACTOR);
        this.numOfParallaxViews = typeArray.getInt(R.styleable.ParallaxScroll_parallax_views_num, DEFAULT_PARALLAX_VIEWS);
        typeArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        makeViewsParallax();
    }

    private void makeViewsParallax() {
        if (getChildCount() > 0 && getChildAt(0) instanceof ViewGroup) {
            ViewGroup viewsHolder = (ViewGroup) getChildAt(0);
            int numOfParallaxViews = Math.min(this.numOfParallaxViews, viewsHolder.getChildCount());
            for (int i = 0; i < numOfParallaxViews; i++) {
                ParallaxView parallaxView = new ScrollViewParallaxItem(viewsHolder.getChildAt(i));
                parallaxViews.add(parallaxView);
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float parallax = parallaxFactor;
        float alpha = alphaFactor;
        for (ParallaxView parallaxView : parallaxViews) {
            parallaxView.setOffset((float) t / parallax);
            parallax *= innerParallaxFactor;
            if (alpha != DEFAULT_ALPHA_FACTOR) {
                float fixedAlpha = (t <= 0) ? 1 : (100 / ((float) t * alpha));
                parallaxView.setAlpha(fixedAlpha);
                alpha /= alphaFactor;
            }
            parallaxView.animateNow();
        }
    }

    protected class ScrollViewParallaxItem extends ParallaxView {

        public ScrollViewParallaxItem(View view) {
            super(view);
        }

        @Override
        protected void translatePreICS(View view, float offset) {
            view.offsetTopAndBottom((int) offset - lastOffset);
            lastOffset = (int) offset;
        }
    }
}