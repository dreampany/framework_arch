package com.dreampany.framework.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.dreampany.framework.data.util.BarUtil;
import com.dreampany.framework.data.util.ColorUtil;
import com.dreampany.framework.ui.activity.BaseActivity;
import com.dreampany.framework.ui.activity.BaseMenuActivity;

/**
 * Created by nuc on 8/13/2016.
 */
public abstract class BaseMenuFragment extends BaseFragment
        implements AdapterView.OnItemSelectedListener, MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener,
        CompoundButton.OnCheckedChangeListener, ActionMode.Callback, android.view.ActionMode.Callback
{

    private final int defaultMenuId = 0;
    private final int defaultContextualMenuId = 0;

    protected Menu menu;
    protected ActionMode actionMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        int menuId = getMenuId();

        if (menuId > defaultMenuId) {
            menu.clear();
            inflater.inflate(menuId, menu);
            this.menu = menu;
            onCreatedMenu(menu);
        }
    }

    protected void onCreatedMenu(Menu menu) {

    }

    protected int getMenuId() {
        return defaultMenuId;
    }

    protected int getContextualMenuId() {
        return defaultContextualMenuId;
    }

    @Override
    public void onStart() {
        super.onStart();

        Activity activity = getActivity();
        if (BaseMenuActivity.class.isInstance(activity)) {
            //((BaseMenuActivity) activity).setOptionMenu(getMenuId());
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        super.onCheckedChanged(compoundButton, checked);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(getContextualMenuId(), menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        BarUtil.backupStatusColor(getActivity());
        BarUtil.setStatusColor(getActivity(), ColorUtil.getBlackColor());
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        BarUtil.restoreStatusColor(getActivity());
    }

    @Override
    public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(getContextualMenuId(), menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
        BarUtil.backupStatusColor(getActivity());
        BarUtil.setStatusColor(getActivity(), ColorUtil.getBlackColor());
        return false;
    }

    @Override
    public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(android.view.ActionMode mode) {
        BarUtil.restoreStatusColor(getActivity());
    }

    protected void setMenuIcon(int menuItemId, int iconRes) {
        if (menu != null) {
            menu.findItem(menuItemId).setIcon(iconRes);
        }
    }

    protected void setMenuVisible(int menuItemId, boolean visible) {
        if (menu != null) {
            menu.findItem(menuItemId).setVisible(visible);
        }
    }

    protected void startActionMode() {
        if (actionMode == null) {
            BaseActivity activity = (BaseActivity) getActivity();
            actionMode = activity.startSupportActionMode(this);
        }
    }

    protected void stopActionMode() {
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }
}
