package com.dreampany.framework.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dreampany.framework.R;
import com.dreampany.framework.data.model.Color;
import com.dreampany.framework.data.util.ViewUtil;

public abstract class BaseNavigationActivity extends BaseMenuActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected int currentNavId;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void startUi(Bundle state) {
        super.startUi(state);
        initDrawer();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        closeDrawer();
        int targetNavId = item.getItemId();
        if (targetNavId != currentNavId) {
            onNavigationItem(targetNavId);
            currentNavId = targetNavId;
        }
        return true;
    }

    protected void onDrawerOpening() {
    }

    protected void onDrawerClosing() {
    }

    protected int getDefaultSelectedNavId() {
        return 0;
    }

    protected int getDrawerLayoutId() {
        return 0;
    }

    protected int getNavigationViewId() {
        return 0;
    }

    protected int getNavigationHeaderId() {
        return 0;
    }

    protected String getNavigationTitle(int navigationItemId) {
        return null;
    }


    protected void onNavigationItem(int navigationItemId) {
    }

    protected void initDrawer() {

        final DrawerLayout drawerLayout = getDrawerLayout();
        final Toolbar toolbar = ViewUtil.getToolbar(this, getToolbarId());
        final NavigationView navigationView = getNavigationView();
        final Color color = getColor();

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        if (newState == DrawerLayout.STATE_SETTLING) {
                            if (!isDrawerOpen()) {
                                onDrawerOpening();
                                ViewUtil.setBackground(getNavigationHeader(), color.getColorPrimaryId());
                            } else {
                                onDrawerClosing();
                            }
                        }
                    }
                };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(getDefaultSelectedNavId());
        navigationView.getMenu().performIdentifierAction(getDefaultSelectedNavId(), 0);
    }

    protected DrawerLayout getDrawerLayout() {
        return (DrawerLayout) findViewById(getDrawerLayoutId());
    }

    protected View getNavigationHeader() {
        return findViewById(getNavigationHeaderId());
    }

    protected NavigationView getNavigationView() {
        return (NavigationView) findViewById(getNavigationViewId());
    }

    protected boolean closeDrawer() {

        if (isDrawerOpen()) {
            DrawerLayout drawerLayout = getDrawerLayout();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        return false;
    }

    private boolean isDrawerOpen() {
        DrawerLayout drawerLayout = getDrawerLayout();
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            return true;
        }
        return false;
    }
}
