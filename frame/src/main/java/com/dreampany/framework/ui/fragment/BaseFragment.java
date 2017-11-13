package com.dreampany.framework.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.dreampany.framework.data.callback.UiCallback;
import com.dreampany.framework.data.listener.RecyclerClickListener;
import com.dreampany.framework.data.model.Color;
import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.ui.activity.BaseActivity;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * Created by nuc on 3/12/2016.
 */
public abstract class BaseFragment extends PreferenceFragment implements UiCallback, View.OnClickListener, CompoundButton.OnCheckedChangeListener, RecyclerClickListener.OnItemClickListener, RecyclerClickListener.OnItemChildClickListener, PermissionListener {

    private final int defaultLayoutId = 0;
    private final int defaultPreferenceLayoutId = 0;
    private ViewDataBinding binding;
    private Task<?> currentTask;
    protected UiCallback activityCallback;
    protected UiCallback fragmentCallback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (BaseFragment.class.isInstance(parentFragment) && UiCallback.class.isInstance(parentFragment)) {
            fragmentCallback = (UiCallback) parentFragment;
        }

        startUi();
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
    public void onClick(View view) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

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

    protected int getLayoutId() {
        return defaultLayoutId;
    }

    public int getPreferenceLayoutId() {
        return defaultPreferenceLayoutId;
    }

    public void startUi() {
    }

    public void stopUi() {
    }

    public String getTitle() {
        return null;
    }

    public String getSubtitle() {
        return null;
    }

    public BaseFragment getCurrentFragment() {
        return this;
    }

    public Task<?> getCurrentTask() {
        if (currentTask == null) {
            currentTask = getIntentValue(Task.class.getName());
        }
        return currentTask;
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

    public <T extends ViewDataBinding> T getBinding() {
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


    public <T> T getIntentValue(String key) {
        return getIntentValue(key, getArguments());
    }

    public <T> T getIntentValue(String key, Bundle bundle) {
        T t = bundle == null ? null : (T) bundle.getParcelable(key);
        return t;
    }

    public Color getColor() {
        Activity activity = getActivity();
        if (BaseActivity.class.isInstance(activity)) {
            return ((BaseActivity) activity).getColor();
        }
        return null;
    }

    @Override
    public Context getContext() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return super.getContext();
        }

        Context context = getActivity();
        if (context != null) return context;

        View view = getView();
        if (view != null) {
            return view.getContext();
        }

        return null;
    }

    public void showFragment() {
        View view = getView();
/*        if (view != null && !ViewUtil.isVisible(view)) {
            view.setVisibility(View.VISIBLE);
        }*/
    }

    public void initSwipeRefreshLayout(SwipeRefreshLayout.OnRefreshListener refreshListener) {
/*        SwipeRefreshLayout swipeRefreshLayout = getSwipeRefreshLayout();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            swipeRefreshLayout.setOnRefreshListener(refreshListener);
        }*/
    }

    public void startSwipeRefresh() {
/*        final SwipeRefreshLayout swipeRefreshLayout = getSwipeRefreshLayout();
        if (swipeRefreshLayout != null) {
            Runnable startRunnable = new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            };
            swipeRefreshLayout.post(startRunnable);
        }*/
    }

    public void stopSwipeRefresh() {
/*        final SwipeRefreshLayout swipeRefreshLayout = getSwipeRefreshLayout();
        if (swipeRefreshLayout != null) {

            Runnable stopRunnable = new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            };

            swipeRefreshLayout.post(stopRunnable);
        }*/
    }

    public void initFabButton(View.OnClickListener clickListener) {
        /*FloatingActionButton fab = getFabButton();
        if (fab != null) {
            fab.setOnClickListener(clickListener);

            Color color = getColor();
            if (color != null) {
                fab.setBackgroundTintList(ColorStateList.valueOf(ColorUtil.getColor(getContext(), color.getColorPrimaryId())));
                fab.setRippleColor(ColorUtil.getColor(getContext(), color.getColorPrimaryDarkId()));
            }
        }*/
    }


}
