package com.dreampany.framework.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.dreampany.framework.R;
import com.dreampany.framework.data.adapter.FragmentAdapter;
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabpager;
    }

    public int getViewPagerId() {
        return R.id.viewPager;
    }

    public int getTabLayoutId() {
        return R.id.tabLayout;
    }

    @Override
    public void startUi() {

        ViewPager viewPager = AndroidUtil.getViewPager(getView(), getViewPagerId());
        TabLayout tabLayout = AndroidUtil.getTabLayout(getView(), getTabLayoutId());

        if (AndroidUtil.isNull(viewPager) || AndroidUtil.isNull(tabLayout)) return;

        FragmentAdapter<BaseFragment> fragmentAdapter = resolveFragmentAdapter();

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);


        Color color = getColor();
        if (color == null) {

        }

        //tabLayout.setBackgroundColor(ColorUtil.getColor(getContext(), color.getColorPrimaryId()));


        tabLayout.setSelectedTabIndicatorColor(
                ColorUtil.getColor(getContext(), color.getColorAccentId())
        );

        tabLayout.setTabTextColors(
                ColorUtil.getColor(getContext(), color.getColorAccentId()),
                ColorUtil.getColor(getContext(), color.getColorPrimaryId())
        );

        // fragmentAdapter.removeAll();

        final FragmentAdapter<BaseFragment> fixedAdapter = fragmentAdapter;
        final String[] pageTitles = pageTitles();
        final Class[] pageClasses = pageClasses();

        final Runnable pagerRunnable = new Runnable() {
            @Override
            public void run() {
                for (int index = 0; index < pageTitles.length; index++) {
                    fixedAdapter.addPage(pageTitles[index], pageClasses[index]);
                }
            }
        };

        AndroidUtil.postDelay(pagerRunnable);
    }


    @Override
    public BaseFragment getCurrentFragment() {
        return getCurrentPagerFragment();
    }

    @Override
    public boolean performBackPressed() {
        BaseFragment currentFragment = getCurrentPagerFragment();
        return currentFragment.performBackPressed();
    }

    public BaseFragment getCurrentPagerFragment() {
        ViewPager viewPager = AndroidUtil.getViewPager(getView(), getViewPagerId());
        FragmentAdapter<BaseFragment> pagerAdapter = getFragmentAdapter();
        if (!AndroidUtil.isNull(viewPager) && !AndroidUtil.isNull(pagerAdapter)) {
            return pagerAdapter.getFragment(viewPager.getCurrentItem()).getCurrentFragment();
        }
        return null;
    }

    public <T extends BaseFragment> FragmentAdapter<T> getFragmentAdapter() {
        ViewPager viewPager = AndroidUtil.getViewPager(getView(), getViewPagerId());
        PagerAdapter pagerAdapter = ViewUtil.getAdapter(viewPager);

        if (!AndroidUtil.isNull(pagerAdapter)) {
            return (FragmentAdapter<T>) pagerAdapter;
        }

        return null;
    }

    public <T extends BaseFragment> FragmentAdapter<T> resolveFragmentAdapter() {
        FragmentAdapter<T> fragmentAdapter = getFragmentAdapter();

        if (AndroidUtil.isNull(fragmentAdapter)) {
            fragmentAdapter = (FragmentAdapter<T>) FragmentAdapter.newAdapter(getFragmentManager());
        }

        return fragmentAdapter;
    }
}
