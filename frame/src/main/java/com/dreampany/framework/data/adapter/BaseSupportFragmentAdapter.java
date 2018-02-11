package com.dreampany.framework.data.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.FragmentUtil;


public abstract class BaseSupportFragmentAdapter<T extends Fragment> extends FragmentPagerAdapter {

    final SparseArray<T> fragments;
    final SparseArray<String> pageTitles;
    final SparseArray<Class<T>> pageClasses;

    BaseSupportFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments = new SparseArray<>();
        pageTitles = new SparseArray<>();
        pageClasses = new SparseArray<>();
    }

    @Override
    public int getCount() {
        int titleSize = pageTitles.size();
        int classSize = pageClasses.size();
        return titleSize <= classSize ? titleSize : classSize;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position);
    }

    @Override
    public int getItemPosition(Object inFragment) {
        if (fragments.indexOfValue((T) inFragment) < 0) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }
        return super.getItemPosition(inFragment);
    }

    public BaseSupportFragmentAdapter<T> addPage(String pageTitle, Class<T> pageClass) {
        int index = getCount();
        setPageTitle(index, pageTitle);
        setPageClass(index, pageClass);
        notifyDataSetChanged();
        return this;
    }

    public BaseSupportFragmentAdapter<T> removePage(int position) {
        return this;
    }

    public BaseSupportFragmentAdapter<T> removeAll() {
        fragments.clear();
        pageTitles.clear();
        pageClasses.clear();
        notifyDataSetChanged();
        return this;
    }

    public T getFragment(int position) {
        return fragments.get(position, null);
    }

    private BaseSupportFragmentAdapter<T> setFragment(int position, T fragment) {
        fragments.put(position, fragment);
        return this;
    }

    private BaseSupportFragmentAdapter<T> setPageTitle(int position, String pageTitle) {
        pageTitles.put(position, pageTitle);
        return this;
    }

    private BaseSupportFragmentAdapter<T> setPageClass(int position, Class<T> pageClass) {
        pageClasses.put(position, pageClass);
        return this;
    }

    T newFragment(int position) {

        T fragment = FragmentUtil.newSupportFragment(pageClasses.get(position, null));

        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("page_title", getPageTitle(position).toString());
            fragment.setArguments(bundle);
            setFragment(position, fragment);
        }
        return fragment;
    }

    T resolveFragment(int position) {
        T fragment = getFragment(position);
        return fragment != null ? fragment : newFragment(position);
    }
}
