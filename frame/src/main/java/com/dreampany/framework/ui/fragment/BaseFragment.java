package com.dreampany.framework.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.dreampany.framework.app.BaseApp;
import com.dreampany.framework.data.callback.UiCallback;
import com.dreampany.framework.data.listener.RecyclerClickListener;
import com.dreampany.framework.data.manager.EventManager;
import com.dreampany.framework.data.model.Color;
import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.FragmentUtil;
import com.dreampany.framework.data.util.TextUtil;
import com.dreampany.framework.ui.activity.BaseActivity;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flipview.FlipView;
import icepick.Icepick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by nuc on 3/12/2016.
 */
public abstract class BaseFragment extends PreferenceFragmentCompat implements
        UiCallback,
        View.OnClickListener,
        View.OnLongClickListener,
        CompoundButton.OnCheckedChangeListener,
        TabLayout.OnTabSelectedListener,
        RecyclerClickListener.OnItemClickListener,
        RecyclerClickListener.OnItemChildClickListener,
        FlexibleAdapter.OnItemClickListener,
        FlexibleAdapter.OnItemLongClickListener,
        FlexibleAdapter.EndlessScrollListener,
        PermissionListener,
        MultiplePermissionsListener,
        EasyPermissions.PermissionCallbacks,
        SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener,
        TextWatcher {

    private final int defaultLayoutId = 0;
    private final int defaultPrefLayoutId = 0;
    private ViewDataBinding binding;
    private Task currentTask;
    protected UiCallback activityCallback;
    protected UiCallback fragmentCallback;
    private BaseFragment childFragment;
    private View view;
    private Color color;
    private final String key = "key";

    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        if (savedInstanceState != null) onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        int preferenceId = getPrefLayoutId();
        if (preferenceId != defaultPrefLayoutId) {
            addPreferencesFromResource(preferenceId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            return view;
        }

        int layoutId = getLayoutId();
        if (layoutId != defaultLayoutId) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
            view = binding.getRoot();
            return view;
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

        if (color == null) {
            color = getColor();
        }
        FlipView.resetLayoutAnimationDelay(true, 1000L);
        startUi(savedInstanceState);
        FlipView.stopLayoutAnimation();
    }

    @Override
    public void onStart() {
        super.onStart();
        //titleSubtitle();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //titleSubtitle();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopUi();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }
    }

    @Override
    public void onDestroy() {
        stopUi();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        if (AndroidUtil.hasMarshmallow()) {
            return super.getContext();
        }

        View view = getView();
        if (view != null) {
            return view.getContext();
        }
        return getParent();
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
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

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
    public void noMoreLoad(int newItemsSize) {

    }

    @Override
    public void onLoadMore(int lastPosition, int currentPage) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    @Override
    public boolean onItemClick(int position) {
        return false;
    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void addData(String value) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            setArguments(bundle);
        }
        bundle.putString(key, value);
    }

    public String getData() {
        return getData(key);
    }

    public int getInt(String key) {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getInt(key);
        }
        return -1;
    }

    public String getData(String key) {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key);
        }
        return null;
    }


    public Context getAppContext() {
        Context context = getContext();
        if (context != null) {
            return context.getApplicationContext();
        }
        return BaseApp.getContext();
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

    protected int getLayoutId() {
        return defaultLayoutId;
    }

    protected int getPrefLayoutId() {
        return defaultPrefLayoutId;
    }

    protected boolean enableEventBus() {
        return false;
    }

    protected void startUi(Bundle state) {
        if (enableEventBus()) {
            registerEventBus();
        }
    }

    protected void stopUi() {
        if (enableEventBus()) {
            unregisterEventBus();
        }
    }

    public String getTitle() {
        return null;
    }

    public String getSubtitle() {
        return null;
    }

    public BaseFragment getChildFragment() {
        return childFragment;
    }

    public void setChildFragment(BaseFragment childFragment) {
        this.childFragment = childFragment;
    }

    protected <T extends BaseFragment> T commitFragmentCompat(final Class<T> fragmentClass, final int parentId) {
        T currentFragment = FragmentUtil.commitFragment(this, fragmentClass, parentId);
        setChildFragment(currentFragment);
        return currentFragment;
    }

    public BaseFragment getCurrentFragment() {
        return this;
    }

    public Task getCurrentTask() {
        if (currentTask == null) {
            currentTask = getIntentValue(Task.class.getName());
        }
        return currentTask;
    }

    public Task getCurrentTask(Intent data) {
        return getIntentValue(Task.class.getName(), data.getExtras());
    }

    public boolean beBackPressed() {
        return false;
    }

    public void registerEventBus() {
        EventManager.register(this);
    }


    public void unregisterEventBus() {
        EventManager.unregister(this);
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

    protected void setTitle(int resId) {
        if (resId <= 0) {
            return;
        }
        setTitle(TextUtil.getString(getContext(), resId));
    }

    protected void setSubtitle(int resId) {
        if (resId <= 0) {
            return;
        }
        setSubtitle(TextUtil.getString(getContext(), resId));
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
        if (color != null) {
            return color;
        }
        Activity activity = getActivity();
        if (BaseActivity.class.isInstance(activity)) {
            return ((BaseActivity) activity).getColor();
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

    protected void showProgressDialog(String message) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showProgress(message);
        }
    }

    protected void hideProgressDialog() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.hideProgress();
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

    protected void forResult() {
        if (!isParentActive()) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Task.class.getName(), getCurrentTask());
        getParent().setResult(Activity.RESULT_OK, intent);
        getParent().finish();
    }

    public void showInfo(String info) {
        if (!isParentActive()) {
            return;
        }
        BaseActivity parent = getParent();
        parent.showInfo(info);
    }

    protected void showError(String error) {
        if (!isParentActive()) {
            return;
        }
        BaseActivity parent = getParent();
        parent.showError(error);
    }

    public void showAlert(String title, String text, int backgroundColor, long timeout) {
        if (!isParentActive()) {
            return;
        }
        BaseActivity parent = getParent();
        parent.showAlert(title, text, backgroundColor, timeout);
    }

    public void hideAlert() {
        if (!isParentActive()) {
            return;
        }
        BaseActivity parent = getParent();
        parent.hideAlert();
    }
}
