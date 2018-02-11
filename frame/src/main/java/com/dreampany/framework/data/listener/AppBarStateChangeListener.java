package com.dreampany.framework.data.listener;

import android.support.design.widget.AppBarLayout;

/**
 * Created by air on 28/12/17.
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State state;

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (state != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            state = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (state != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            state = State.COLLAPSED;
        } else {
            if (state != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            state = State.IDLE;
        }
    }
}
