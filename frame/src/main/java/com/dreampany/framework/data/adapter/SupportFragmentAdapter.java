package com.dreampany.framework.data.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class SupportFragmentAdapter<T extends Fragment> extends BaseSupportFragmentAdapter<T> {

    private SupportFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public static SupportFragmentAdapter<?> newAdapter(FragmentManager fragmentManager) {
        return new SupportFragmentAdapter<>(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return resolveFragment(position);
    }
}
