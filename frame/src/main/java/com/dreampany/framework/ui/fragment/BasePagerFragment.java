package com.dreampany.framework.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.dreampany.framework.R;
import com.dreampany.framework.data.adapter.SupportFragmentAdapter;
import com.dreampany.framework.data.model.Color;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.ColorUtil;
import com.dreampany.framework.data.util.ViewUtil;

/**
 * Created by nuc on 10/9/2016.
 */

public abstract class BasePagerFragment extends BaseMenuFragment {

    public abstract String[] pageTitles();

    public abstract Class[] pageClasses();

    public abstract boolean keepAllPage();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabpager;
    }

    public int getViewPagerId() {
        return R.id.view_pager;
    }

    public int getTabLayoutId() {
        return R.id.tab_layout;
    }

    @Override
    protected void startUi(Bundle state) {
        super.startUi(state);

        ViewPager viewPager = ViewUtil.getViewPager(getView(), getViewPagerId());
        TabLayout tabLayout = ViewUtil.getTabLayout(getView(), getTabLayoutId());

        if (viewPager == null || tabLayout == null) {
            return;
        }

        SupportFragmentAdapter<BaseFragment> fragmentAdapter = resolveFragmentAdapter();

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);


        Color color = getColor();
        if (color != null) {
            //tabLayout.setBackgroundColor(ColorUtil.getColor(getContext(), color.getColorPrimaryId()));


            tabLayout.setSelectedTabIndicatorColor(
                    ColorUtil.getColor(getContext(), color.getColorAccentId())
            );

            tabLayout.setTabTextColors(
                    ColorUtil.getColor(getContext(), color.getColorPrimaryId()),
                    ColorUtil.getColor(getContext(), color.getColorAccentId())
            );
        }

        final SupportFragmentAdapter<BaseFragment> fixedAdapter = fragmentAdapter;
        final String[] pageTitles = pageTitles();
        final Class[] pageClasses = pageClasses();

        if (keepAllPage()) {
            viewPager.setOffscreenPageLimit(pageClasses.length);
        }

        final Runnable pagerRunnable = () -> {
            for (int index = 0; index < pageClasses.length; index++) {
                fixedAdapter.addPage(pageTitles[index], pageClasses[index]);
            }
        };

        AndroidUtil.postDelay(pagerRunnable);
    }


    @Override
    public BaseFragment getCurrentFragment() {
        return getCurrentPagerFragment();
    }

    @Override
    public boolean beBackPressed() {
        BaseFragment currentFragment = getCurrentPagerFragment();
        if (currentFragment != null) {
            return currentFragment.beBackPressed();
        }
        return super.beBackPressed();
    }

    public BaseFragment getCurrentPagerFragment() {
        ViewPager viewPager = ViewUtil.getViewPager(getView(), getViewPagerId());
        SupportFragmentAdapter pagerAdapter = getFragmentAdapter();
        if (viewPager != null && pagerAdapter != null) {
            BaseFragment compat = (BaseFragment) pagerAdapter.getFragment(viewPager.getCurrentItem());
            if (compat != null) {
                return compat.getCurrentFragment();
            }
        }
        return null;
    }

    public <T extends BaseFragment> SupportFragmentAdapter getFragmentAdapter() {
        ViewPager viewPager = ViewUtil.getViewPager(getView(), getViewPagerId());
        PagerAdapter pagerAdapter = ViewUtil.getAdapter(viewPager);

        if (pagerAdapter != null) {
            return (SupportFragmentAdapter) pagerAdapter;
        }

        return null;
    }

    public <T extends BaseFragment> SupportFragmentAdapter<T> resolveFragmentAdapter() {
        SupportFragmentAdapter fragmentAdapter = getFragmentAdapter();

        if (fragmentAdapter == null) {
            fragmentAdapter = SupportFragmentAdapter.newAdapter(getFragmentManager());
        }

        return fragmentAdapter;
    }
}
