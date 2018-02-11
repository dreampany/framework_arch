package com.dreampany.framework.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.dreampany.framework.R;
import com.dreampany.framework.data.adapter.SmartPagerAdapter;
import com.dreampany.framework.data.model.Color;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.ColorUtil;
import com.dreampany.framework.data.util.ViewUtil;

/**
 * Created by nuc on 10/9/2016.
 */

public abstract class BaseStateFragment extends BaseMenuFragment {

    protected abstract String[] pageTitles();

    protected abstract Class[] pageClasses();

    protected abstract boolean keepAllPage();

    protected abstract boolean enableTabColor();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tabpager;
    }

    protected int getViewPagerId() {
        return R.id.view_pager;
    }

    protected int getTabLayoutId() {
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

        SmartPagerAdapter<BaseFragment> fragmentAdapter = resolveFragmentAdapter();

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (enableTabColor()) {
            Color color = getColor();
            tabLayout.setBackgroundColor(ColorUtil.getColor(getContext(), color.getColorPrimaryId()));
            tabLayout.setSelectedTabIndicatorColor(
                    ColorUtil.getColor(getContext(), R.color.colorWhite)
            );

            tabLayout.setTabTextColors(
                    ColorUtil.getColor(getContext(), R.color.colorWhite2),
                    ColorUtil.getColor(getContext(), R.color.colorWhite)
            );
        }

        // fragmentAdapter.removeAll();

        final SmartPagerAdapter fixedAdapter = fragmentAdapter;
        final String[] pageTitles = pageTitles();
        final Class[] pageClasses = pageClasses();

        if (keepAllPage()) {
            viewPager.setOffscreenPageLimit(pageClasses.length);
        } else {
            //viewPager.setOffscreenPageLimit(pageClasses.length);
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
        SmartPagerAdapter pagerAdapter = getFragmentAdapter();
        if (viewPager != null && pagerAdapter != null) {
            BaseFragment fragment = pagerAdapter.getFragment(viewPager.getCurrentItem());
            if (fragment != null) {
                return fragment.getCurrentFragment();
            }
        }
        return null;
    }

    public <T extends BaseFragment> SmartPagerAdapter getFragmentAdapter() {
        ViewPager viewPager = ViewUtil.getViewPager(getView(), getViewPagerId());
        PagerAdapter pagerAdapter = ViewUtil.getAdapter(viewPager);
        if (pagerAdapter != null) {
            return (SmartPagerAdapter) pagerAdapter;
        }

        return null;
    }

    public <T extends BaseFragment> SmartPagerAdapter resolveFragmentAdapter() {
        SmartPagerAdapter fragmentAdapter = getFragmentAdapter();

        if (fragmentAdapter == null) {
            fragmentAdapter = SmartPagerAdapter.newAdapter(getChildFragmentManager());
        }

        return fragmentAdapter;
    }
}
