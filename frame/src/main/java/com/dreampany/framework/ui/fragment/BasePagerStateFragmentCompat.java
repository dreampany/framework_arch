/*
package com.dreampany.framework.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.dreampany.framework.R;
import com.dreampany.framework.data.adapter.SupportFragmentStateAdapter;
import com.dreampany.framework.data.model.Color;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.ColorUtil;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.ViewUtil;

*/
/**
 * Created by nuc on 10/9/2016.
 *//*


public abstract class BasePagerStateFragmentCompat extends BaseMenuFragmentCompat {

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
    public void startUi() {

        ViewPager viewPager = ViewUtil.getViewPager(getView(), getViewPagerId());
        TabLayout tabLayout = ViewUtil.getTabLayout(getView(), getTabLayoutId());

        if (viewPager == null || tabLayout == null) return;

        SupportFragmentStateAdapter<BaseFragmentCompat> fragmentAdapter = resolveFragmentAdapter();

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);


        Color color = getColor();
        if (color != null) {
            //tabLayout.setBackgroundColor(ColorUtil.getColor(getContext(), color.getColorPrimaryId()));
            tabLayout.setSelectedTabIndicatorColor(
                    ColorUtil.getColor(getContext(), color.getColorAccentId())
            );

            tabLayout.setTabTextColors(
                    ColorUtil.getColor(getContext(), color.getColorAccentId()),
                    ColorUtil.getColor(getContext(), color.getColorPrimaryId())
            );
        }

        // fragmentAdapter.removeAll();

        final SupportFragmentStateAdapter fixedAdapter = fragmentAdapter;
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
    public BaseFragmentCompat getCurrentFragment() {
        return getCurrentPagerFragment();
    }

    @Override
    public boolean performBackPressed() {
        BaseFragmentCompat currentFragment = getCurrentPagerFragment();
        if (currentFragment != null) {
            return currentFragment.performBackPressed();
        }
        return super.performBackPressed();
    }

    public BaseFragmentCompat getCurrentPagerFragment() {
        ViewPager viewPager = ViewUtil.getViewPager(getView(), getViewPagerId());
        SupportFragmentStateAdapter pagerAdapter = getFragmentAdapter();
        if (viewPager != null && pagerAdapter != null) {
            BaseFragmentCompat compat = (BaseFragmentCompat) pagerAdapter.getFragment(viewPager.getCurrentItem());
            if (compat != null) {
                return compat.getCurrentFragment();
            }
        }
        return null;
    }

    public <T extends BaseFragmentCompat> SupportFragmentStateAdapter getFragmentAdapter() {
        ViewPager viewPager = ViewUtil.getViewPager(getView(), getViewPagerId());
        PagerAdapter pagerAdapter = ViewUtil.getAdapter(viewPager);

        if (pagerAdapter != null) {
            return (SupportFragmentStateAdapter) pagerAdapter;
        }

        return null;
    }

    public <T extends BaseFragmentCompat> SupportFragmentStateAdapter resolveFragmentAdapter() {
        SupportFragmentStateAdapter fragmentAdapter = getFragmentAdapter();

        if (fragmentAdapter == null) {
            fragmentAdapter = SupportFragmentStateAdapter.newAdapter(getFragmentManager());
        }

        return fragmentAdapter;
    }
}
*/
