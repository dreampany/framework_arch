package com.dreampany.framework.data.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.FragmentUtil;
import com.dreampany.framework.ui.fragment.BaseFragment;

import java.util.List;


public abstract class BaseStateAdapter<T extends BaseFragment> extends FragmentStatePagerAdapter {

    final FragmentManager manager;
    final SparseArray<T> fragments;
    final SparseArray<String> pageTitles;
    final SparseArray<Class<T>> pageClasses;

    BaseStateAdapter(FragmentManager manager) {
        super(manager);
        this.manager = manager;
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
        return pageTitles.get(position, null);
    }

    @Override
    public int getItemPosition(Object inFragment) {
        int position = fragments.indexOfValue((T) inFragment);
        if (position >= 0) {
            return position;
        }
        return FragmentStatePagerAdapter.POSITION_NONE;

/*        if (fragments.indexOfValue((T) inFragment) < 0) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }
        return super.getItemPosition(inFragment);*/
    }

    Class<T> getClazz(int position) {
        return pageClasses.get(position, null);
    }


    public BaseStateAdapter<T> addPage(String pageTitle, Class<T> pageClass) {
        if (contains(pageTitle)) {
            return this;
        }
        int index = getCount();
        setPageTitle(index, pageTitle);
        setPageClass(index, pageClass);
        notifyDataSetChanged();
        return this;
    }

    public boolean contains(Class<T> pageClass) {
        return pageClasses.indexOfValue(pageClass) >= 0;
    }

    public boolean contains(String title) {
        return pageTitles.indexOfValue(title) >= 0;
    }

    public BaseStateAdapter<T> removePage(int position) {
        return this;
    }

    public BaseStateAdapter<T> removeAll() {
        if (getCount() > 0) {
            fragments.clear();
            pageTitles.clear();
            pageClasses.clear();
            notifyDataSetChanged();
        }
        return this;
    }

    public T getFragment(int position) {
        return fragments.get(position, null);
    }

    private BaseStateAdapter<T> setFragment(int position, T fragment) {
        fragments.put(position, fragment);
        return this;
    }

    private BaseStateAdapter<T> setPageTitle(int position, String pageTitle) {
        pageTitles.put(position, pageTitle);
        return this;
    }

    private BaseStateAdapter<T> setPageClass(int position, Class<T> pageClass) {
        pageClasses.put(position, pageClass);
        return this;
    }

    T newFragment(int position) {
        Class<T> clazz = pageClasses.get(position);
        String tag = pageTitles.get(position);
        T fragment = FragmentUtil.getSupportFragment(manager, clazz, tag, null);
        if (fragment != null) {
            fragment.setRetainInstance(true);
            fragment.addData(tag);
            setFragment(position, fragment);
        }
        return fragment;
    }

    public List<T> getCurrentFragments() {
        if (fragments.size() > 0) {
            return DataUtil.asList(fragments);

        }
        return null;
    }

/*    T newFragment(int position) {
        T fragment = FragmentUtil.newSupportFragment(pageClasses.get(position, null));
        if (fragment != null) {
            fragment.setRetainInstance(true);
            fragment.addData(getPageTitle(position).toString());
            setFragment(position, fragment);
        }
        return fragment;
    }*/
}
