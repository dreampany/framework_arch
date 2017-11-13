package com.dreampany.framework.ui.activity;

import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dreampany.framework.data.util.BarUtil;
import com.dreampany.framework.data.util.ColorUtil;

/**
 * Created by nuc on 8/9/2016.
 */
public abstract class BaseMenuActivity extends BaseActivity implements ActionMode.Callback {

    private final int defaultMenuId = 0;

    private int menuId = defaultMenuId;
    private int contextualMenuId = defaultMenuId;

    protected ActionMode actionMode;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuId = getMenuId();
        if (menuId != defaultMenuId) { //this need clear
            menu.clear();
            getMenuInflater().inflate(menuId, menu);
            this.menuId = menuId;
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected int getMenuId() {
        return menuId;
    }

    protected int getContextualMenuId() {
        return contextualMenuId;
    }

    public void refreshMenu() {
        supportInvalidateOptionsMenu();
    }

    public void setOptionMenu(int menuId) {
        this.menuId = menuId;
        refreshMenu();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(getContextualMenuId(), menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        BarUtil.backupStatusColor(this);
        BarUtil.setStatusColor(this, ColorUtil.getBlackColor());
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        BarUtil.restoreStatusColor(this);
    }

    public void startActionMode() {
        if (actionMode == null) {
            actionMode = startSupportActionMode(this);
        }
    }

    public void stopActionMode() {
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }
}
