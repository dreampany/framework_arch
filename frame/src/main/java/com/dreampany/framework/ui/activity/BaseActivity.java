package com.dreampany.framework.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.BottomNavBgMode;
import com.afollestad.aesthetic.BottomNavIconTextMode;
import com.afollestad.aesthetic.NavigationViewMode;
import com.dreampany.framework.R;
import com.dreampany.framework.app.BaseApp;
import com.dreampany.framework.data.callback.UiCallback;
import com.dreampany.framework.data.manager.EventManager;
import com.dreampany.framework.data.model.Color;
import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.BarUtil;
import com.dreampany.framework.data.util.ColorUtil;
import com.dreampany.framework.data.util.FragmentUtil;
import com.dreampany.framework.data.util.LogKit;
import com.dreampany.framework.data.util.ViewUtil;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.tapadoo.alerter.Alerter;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by nuc on 3/12/2016.
 */
public abstract class BaseActivity extends AppCompatActivity implements
        View.OnClickListener, View.OnLongClickListener, UiCallback, PermissionListener, MultiplePermissionsListener, EasyPermissions.PermissionCallbacks {

    private final int defaultLayoutId = 0;
    private final int defaultToolbarId = 0;

    private ViewDataBinding binding;
    private Task currentTask;
    private BaseFragment currentFragmentCompat;
    private Color color;
    private ProgressDialog progress;

    protected int getLayoutId() {
        return defaultLayoutId;
    }

    protected int getToolbarId() {
        return defaultToolbarId;
    }

    protected boolean enableFullScreen() {
        return false;
    }

    protected boolean enableColor() {
        return getApp().enableColor();
    }

    protected boolean applyColor() {
        return true;
    }

    private boolean enableTheme() {
        return getApp().enableTheme();
    }

    protected boolean enabledHomeUp() {
        return true;
    }

    protected boolean jumpRequired() {
        return true;
    }

    protected boolean enableEventBus() {
        return false;
    }

    protected boolean beBackPressed() {
        return true;
    }

/*    @Override
    protected void attachBaseContext(Context newBase) {
        if (getApp().enableCalligraphy()) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        } else {
            super.attachBaseContext(newBase);
        }
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AndroidUtil.hasLollipop()) {
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

/*        if (enableTheme()) {
            Aesthetic.attach(this);
        }*/

        super.onCreate(savedInstanceState);

        if (AndroidUtil.hasLollipop()) {
            getWindow().setEnterTransition(new Slide());
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        if (enableColor() || applyColor()) {
            Color color = getApp().getColor();
            if (color == null) {
                color = ColorUtil.getRandColor();
            }
            setColor(color);
        }

/*        if (enableTheme()) {
            Colorful.applyTheme(this);
        }*/

        if (jumpRequired()) {
            getApp().jumpIfNeeded(this);
        }


/*        if (Aesthetic.isFirstTime()) {
            Aesthetic.get()
                    .activityTheme(R.style.AppTheme_Frame)
                    .textColorPrimaryRes(R.color.text_color_primary)
                    .textColorSecondaryRes(R.color.text_color_secondary)
                    .colorPrimaryRes(R.color.md_white)
                    .colorAccentRes(R.color.md_blue)
                    .colorStatusBarAuto()
                    .colorNavigationBarAuto()
                    .tabLayoutBackgroundMode(TabLayoutBgMode.PRIMARY)
                    .textColorPrimary(android.graphics.Color.BLACK)
                    .navigationViewMode(NavigationViewMode.SELECTED_ACCENT)
                    .bottomNavigationBackgroundMode(BottomNavBgMode.PRIMARY)
                    .bottomNavigationIconTextMode(BottomNavIconTextMode.SELECTED_ACCENT)
                    .apply();
        }*/

        //if (savedInstanceState == null) {
        startUi(savedInstanceState);
        //}

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        hideAlert();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (!beBackPressed()) {
            return;
        }

        BaseFragment currentFragment = getCurrentFragment();
        if (currentFragment != null) {
            if (currentFragment.beBackPressed()) {
                return;
            }
        }

        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
  /*      if (enableTheme()) {
            Aesthetic.resume(this);
        }*/
      /*  if (FirebaseManager.onInstance().isAuthenticated()) {
            hideProgress();
        }*/
    }

    @Override
    protected void onPause() {
/*        if (enableTheme()) {
            Aesthetic.pause(this);
        }*/
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    @Override
    protected void onDestroy() {
        stopUi();
        super.onDestroy();
    }

    @Override
    public void onTask(Task task) {

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

    public Context getContext() {
        return this;
    }

    public Context getAppContext() {
        return getApplicationContext();
    }

    public Color getColor() {
        return color;
    }

    public void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void setSubtitle(String subtitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

    protected void startUi(Bundle state) {

        if (enableEventBus()) {
            registerEventBus();
        }

        boolean fullScreen = enableFullScreen();
        boolean applyColor = applyColor();
        boolean enableTheme = enableTheme();

        if (fullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            BarUtil.hide(this);
        }

        int layoutId = getLayoutId();
        if (layoutId == defaultLayoutId) {
            return;
        }

        //first checking for app theme color
/*        Color color = getApp().getColor();
        if (color == null) {
            color = ColorUtil.getRandColor();
        }
        setColor(color);*/
        binding = DataBindingUtil.setContentView(this, layoutId);

        if (fullScreen) {
            Toolbar toolbar = ViewUtil.getToolbar(this, getToolbarId());
            if (toolbar != null) {
                //LogKit.verbose("Toolbar found");
                BarUtil.hideToolbar(toolbar);
            }
        } else {
            if (applyColor && !enableTheme) {
                BarUtil.setStatusColor(this, color);
                // BarUtil.setActionBarColor(toolbar, color);
            }


            Toolbar toolbar = ViewUtil.getToolbar(this, getToolbarId());

            if (toolbar != null) {
                //LogKit.verbose("Toolbar found");
                setSupportActionBar(toolbar);

                if (enabledHomeUp()) {
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        actionBar.setHomeButtonEnabled(true);
                    }
                }

                if (applyColor && !enableTheme) {
                    //BarUtil.setStatusColor(this, color);
                    BarUtil.setActionBarColor(toolbar, color);
                }
            }
        }


        if (enableTheme()) {
            applyTheme();
        }
    }


    protected void stopUi() {
        if (enableEventBus()) {
            unregisterEventBus();
        }
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

    protected <T extends ViewDataBinding> T getBinding() {
        return (T) binding;
    }

    public <T> T getValue(String key) {
        Bundle bundle = getBundle();
        if (bundle != null && bundle.containsKey(key)) {
            return (T) bundle.get(key);
        }
        return (T) null;
    }

    public boolean getBoolean(String key) {
        Bundle bundle = getBundle();
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getBoolean(key, false);
        }
        return false;
    }

    public Task getCurrentTask(boolean freshTask) {
        if (currentTask == null || freshTask) {
            currentTask = getIntentValue(Task.class.getName());
        }
        return currentTask;
    }

    public Task getCurrentTask(Intent data) {
        if (currentTask == null) {
            currentTask = getIntentValue(Task.class.getName());
        }
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public BaseFragment getCurrentFragment() {
        return currentFragmentCompat;
    }

    public void setCurrentFragment(BaseFragment fragment) {
        this.currentFragmentCompat = fragment;
    }

    protected boolean performBackPressed() {
        return false;
    }


    public void registerEventBus() {
        EventManager.register(this);
    }


    public void unregisterEventBus() {
        EventManager.unregister(this);
    }


    protected <T> T getIntentValue(String key) {
        Bundle bundle = getBundle();
        return getIntentValue(key, bundle);
    }

    protected <T> T getIntentValue(String key, Bundle bundle) {
        T t = null;
        if (bundle != null) {
            t = (T) bundle.getParcelable(key);
        }

        if (bundle != null && t == null) {
            t = (T) bundle.getSerializable(key);
        }
        return t;
    }

    protected Bundle getBundle() {
        return getIntent().getExtras();
    }


    protected void setColor(Color color) {
        this.color = color;
    }


    protected void initFullView() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BarUtil.hide(this);

        DataBindingUtil.setContentView(this, getLayoutId());
    }

    protected <T extends BaseActivity> void openActivity(Class<T> aClass) {
        startActivity(new Intent(this, aClass));
    }

    protected <T extends BaseActivity> void openActivity(Class<T> aClass, String key, String value) {
        Intent intent = new Intent(this, aClass);
        intent.putExtra(key, value);
        startActivity(intent);
    }

    protected <T extends BaseActivity> void openActivity(Class<T> aClass, Task task) {
        Intent intent = new Intent(this, aClass);
        intent.putExtra(Task.class.getName(), (Parcelable) task);
        startActivity(intent);
    }

    protected <T extends BaseActivity> void openForResult(Class<T> aClass, Task task, int requestCode) {
        Intent intent = new Intent(this, aClass);
        startActivityForResult(intent, requestCode);
    }

    protected <T extends BaseFragment> T commitFragment(final Class<T> fragmentClass, final int parentId) {
        T currentFragment = FragmentUtil.commitFragment(this, fragmentClass, parentId);
        setCurrentFragment(currentFragment);
        return currentFragment;
    }

    protected <T extends BaseFragment> T commitPersistentFragment(final Class<T> fragmentClass, final int parentId) {
        T currentFragment = FragmentUtil.commitPersistentFragment(this, fragmentClass, parentId);
        setCurrentFragment(currentFragment);
        return currentFragment;
    }

    protected <T extends BaseFragment> T commitFragment(final Class<T> fragmentClass, final int parentId, Task task) {
        T currentFragment = FragmentUtil.commitFragment(this, fragmentClass, parentId, task);
        setCurrentFragment(currentFragment);
        return currentFragment;
    }

    public void showProgress(String message) {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setCancelable(true);
        } else if (progress.isShowing()) {
            return;
        }

        progress.setMessage(message + "...");
        progress.show();
    }

    public void hideProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    public BaseApp getApp() {
        return (BaseApp) getApplication();
    }

    public void showAlert(String title, String text, int backgroundColor, long timeout) {
        if (!Alerter.isShowing()) {
            Alerter.create(this)
                    .setTitle(title)
                    .setText(text)
                    .setBackgroundColorRes(backgroundColor)
                    .setIcon(R.drawable.alerter_ic_face)
                    .show();
            AndroidUtil.post(hideAlert, timeout);
        }
    }

    public void hideAlert() {
        if (Alerter.isShowing()) {
            Alerter.hide();
        }
    }

    private final Runnable hideAlert = () -> {
        if (isDestroyed() || isFinishing()) {
            return;
        }
        hideAlert();
    };

    public void showInfo(String info) {
        StyleableToast st = new StyleableToast
                .Builder(this)
                .text(info)
                .textColor(android.graphics.Color.WHITE)
                .backgroundColor(ColorUtil.getColor(this, R.color.colorGreen700))
                .duration(Toast.LENGTH_LONG)
                .build();
        st.show();
    }

    public void showError(String error) {
        StyleableToast st = new StyleableToast
                .Builder(this)
                .text(error)
                .textColor(android.graphics.Color.WHITE)
                .backgroundColor(ColorUtil.getColor(this, R.color.colorRed700))
                .duration(Toast.LENGTH_LONG)
                .build();
        st.show();
    }

    private void applyTheme() {
        if (Aesthetic.isFirstTime()) {

        }
        Color color = getColor();
        Aesthetic.get()
        /*
                .colorPrimaryDarkRes(color.getColorPrimaryDarkId())
                */
                .isDark(true)
                .textColorPrimaryRes(R.color.text_color_primary)
                .textColorSecondaryRes(R.color.text_color_secondary)
                .colorPrimaryRes(color.getColorPrimaryId())
                .colorAccentRes(color.getColorAccentId())
                .colorStatusBarAuto()
                .colorNavigationBarAuto()
                .textColorPrimaryRes(R.color.colorBlack)
                .navigationViewMode(NavigationViewMode.SELECTED_ACCENT)
                .bottomNavigationBackgroundMode(BottomNavBgMode.PRIMARY)
                .bottomNavigationIconTextMode(BottomNavIconTextMode.SELECTED_ACCENT)
                .apply();
    }


}
