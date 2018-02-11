/*
package com.dreampany.framework.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dreampany.framework.R;
import com.dreampany.framework.app.BaseApp;
import com.dreampany.framework.data.callback.UiCallback;
import com.dreampany.framework.data.listener.RecyclerClickListener;
import com.dreampany.framework.data.model.Color;
import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.ColorUtil;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.FragmentUtil;
import com.dreampany.framework.ui.activity.BaseActivity;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.List;

import eu.davidea.flipview.FlipView;

*/
/**
 * Created by nuc on 3/12/2016.
 *//*

public abstract class BaseFragmentCompat extends PreferenceFragmentCompat implements UiCallback, View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener, TabLayout.OnTabSelectedListener, RecyclerClickListener.OnItemClickListener, RecyclerClickListener.OnItemChildClickListener, PermissionListener, MultiplePermissionsListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private final int defaultLayoutId = 0;
    private final int defaultPreferenceLayoutId = 0;
    private ViewDataBinding binding;
    private Task<?> currentTask;
    protected UiCallback activityCallback;
    protected UiCallback fragmentCallback;
    private BaseFragmentCompat childFragmentCompat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        int preferenceId = getPreferenceLayoutId();
        if (preferenceId != defaultPreferenceLayoutId) {
            addPreferencesFromResource(preferenceId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        if (layoutId != defaultLayoutId) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
            return binding.getRoot();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // this will be worked when activity and fragment relation
        Activity activity = getActivity();
        if (BaseActivity.class.isInstance(activity) && UiCallback.class.isInstance(activity)) {
            activityCallback = (UiCallback) activity;
        }

        // this will be worked when parent and child fragment relation
        Fragment parentFragment = getParentFragment();
        if (BaseFragmentCompat.class.isInstance(parentFragment) && UiCallback.class.isInstance(parentFragment)) {
            fragmentCallback = (UiCallback) parentFragment;
        }

        FlipView.resetLayoutAnimationDelay(true, 1000L);
        startUi();
        FlipView.stopLayoutAnimation();
    }

    @Override
    public void onStart() {
        super.onStart();
        titleSubtitle();
        registerEventBus();
    }

    @Override
    public void onStop() {
        unregisterEventBus();
        super.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            titleSubtitle();
        }
    }

    @Override
    public void onDestroy() {
        stopUi();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return super.getContext();
        }

        Context context = getParent();
        if (context != null) {
            return context;
        }

        View view = getView();
        if (view != null) {
            return view.getContext();
        }

        return null;
    }

    public Context getAppContext() {
        Context context = getContext();
        if (context != null) {
            return context.getApplicationContext();
        }
        return null;
    }

    protected BaseActivity getParent() {
        Activity activity = getActivity();
        if (!BaseActivity.class.isInstance(activity) || activity.isFinishing() || activity.isDestroyed()) {
            return null;
        }
        return (BaseActivity) activity;
    }


    protected void setResult(Intent result) {
        getParent().setResult(Activity.RESULT_OK, result);
        finish();
    }

    protected void finish() {
        BaseActivity activity = getParent();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void onTask(Task task) {

    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {

    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {

    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onItemLongClick(View view, int position) {
    }

    @Override
    public void onChildItemClick(View view, int position) {

    }

    @Override
    public void onChildItemLongClick(View view, int position) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }


    protected int getLayoutId() {
        return defaultLayoutId;
    }

    protected int getPreferenceLayoutId() {
        return defaultPreferenceLayoutId;
    }

    protected void startUi() {
    }

    protected void stopUi() {
    }

    public String getTitle() {
        return null;
    }

    public String getSubtitle() {
        return null;
    }

    public BaseFragmentCompat getChildFragmentCompat() {
        return childFragmentCompat;
    }

    public void setChildFragmentCompat(BaseFragmentCompat childFragmentCompat) {
        this.childFragmentCompat = childFragmentCompat;
    }

    protected <T extends BaseFragmentCompat> T commitFragmentCompat(final Class<T> fragmentClass, final int parentId) {
        T currentFragment = FragmentUtil.commitFragmentCompat(this, fragmentClass, parentId);
        setChildFragmentCompat(currentFragment);
        return currentFragment;
    }

    public BaseFragmentCompat getCurrentFragment() {
        return this;
    }

    public Task<?> getCurrentTask() {
        if (currentTask == null) {
            currentTask = getIntentValue(Task.class.getName());
        }
        return currentTask;
    }

    public Task getCurrentTask(Intent data) {
        return getIntentValue(Task.class.getName(), data.getExtras());
    }

    public boolean performBackPressed() {
        return false;
    }

    public void registerEventBus() {
    }

    public void unregisterEventBus() {
    }

    public String getPageTitle() {
        Bundle bundle = getArguments();
        return bundle.getString("page_title", null);
    }

    protected <T extends ViewDataBinding> T getBinding() {
        return (T) binding;
    }

    public void titleSubtitle() {
        String title = getTitle();
        if (!DataUtil.isEmpty(title)) {
            setTitle(title);
        }

        String subtitle = getSubtitle();
        if (!DataUtil.isEmpty(subtitle)) {
            setSubtitle(subtitle);
        }
    }

    protected void setTitle(String title) {
        Activity activity = getActivity();
        if (BaseActivity.class.isInstance(activity)) {
            ((BaseActivity) activity).setTitle(title);
        }
    }

    protected void setSubtitle(String subtitle) {
        Activity activity = getActivity();
        if (BaseActivity.class.isInstance(activity)) {
            ((BaseActivity) activity).setSubtitle(subtitle);
        }
    }

    public <T> T getValue(String key) {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(key)) {
            return (T) bundle.get(key);
        }
        return (T) null;
    }

    public <T> T getIntentValue(String key) {
        return getIntentValue(key, getArguments());
    }

    public <T> T getIntentValue(String key, Bundle bundle) {
        T t = null;
        if (bundle != null) {
            t = (T) bundle.getParcelable(key);
        }

        if (bundle != null && t == null) {
            t = (T) bundle.getSerializable(key);
        }
        return t;
    }

    public Color getColor() {
        Activity activity = getActivity();
        if (BaseActivity.class.isInstance(activity)) {
            return ((BaseActivity) activity).getColor();
        }
        return null;
    }


    public void showFragment() {
        View view = getView();
*/
/*        if (view != null && !ViewUtil.isVisible(view)) {
            view.setVisibility(View.VISIBLE);
        }*//*

    }

    public void initSwipeRefreshLayout(SwipeRefreshLayout.OnRefreshListener refreshListener) {
*/
/*        SwipeRefreshLayout swipeRefreshLayout = getSwipeRefreshLayout();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            swipeRefreshLayout.setOnRefreshListener(refreshListener);
        }*//*

    }

    public void startSwipeRefresh() {
*/
/*        final SwipeRefreshLayout swipeRefreshLayout = getSwipeRefreshLayout();
        if (swipeRefreshLayout != null) {
            Runnable startRunnable = new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            };
            swipeRefreshLayout.post(startRunnable);
        }*//*

    }

    public void stopSwipeRefresh() {
*/
/*        final SwipeRefreshLayout swipeRefreshLayout = getSwipeRefreshLayout();
        if (swipeRefreshLayout != null) {

            Runnable stopRunnable = new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            };

            swipeRefreshLayout.post(stopRunnable);
        }*//*

    }

    public void initFabButton(View.OnClickListener clickListener) {
        */
/*FloatingActionButton fab = getFabButton();
        if (fab != null) {
            fab.setOnClickListener(clickListener);

            Color color = getColor();
            if (color != null) {
                fab.setBackgroundTintList(ColorStateList.valueOf(ColorUtil.getColor(getContext(), color.getColorPrimaryId())));
                fab.setRippleColor(ColorUtil.getColor(getContext(), color.getColorPrimaryDarkId()));
            }
        }*//*

    }

    protected void showProgressDialog(String message) {

        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showProgressDialog(message);
        }
    }

    protected void hideProgressDialog() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.hideProgressDialog();
        }
    }

    public BaseApp getApplication() {
        Activity activity = getActivity();
        return (BaseApp) activity.getApplication();
    }

    protected void hideFab(View view) {
        ViewCompat.animate(view)
                .scaleX(0f).scaleY(0f)
                .alpha(0f).setDuration(50)
                .start();
    }

    protected void showFab(View view, long delay) {
        AndroidUtil.post(() -> ViewCompat.animate(view)
                .scaleX(1f).scaleY(1f)
                .alpha(1f).setDuration(200)
                .start(), delay);
    }


    protected boolean isParentActive() {
        Activity activity = getParent();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return false;
        }
        return true;
    }
}
*/
