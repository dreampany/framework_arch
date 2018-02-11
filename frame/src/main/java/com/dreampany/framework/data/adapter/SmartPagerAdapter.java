package com.dreampany.framework.data.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.dreampany.framework.ui.fragment.BaseFragment;

public class SmartPagerAdapter<T extends BaseFragment> extends BaseStateAdapter<T> {

    private SmartPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public static SmartPagerAdapter<?> newAdapter(FragmentManager fragmentManager) {
        return new SmartPagerAdapter<>(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return newFragment(position);
    }
}
