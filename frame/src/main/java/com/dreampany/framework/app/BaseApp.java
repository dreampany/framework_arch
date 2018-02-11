package com.dreampany.framework.app;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.dreampany.framework.R;
import com.dreampany.framework.data.manager.ServiceManager;
import com.dreampany.framework.data.model.Color;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.ui.activity.BaseActivity;
import com.firebase.jobdispatcher.JobService;
import com.github.javiersantos.appupdater.AppUpdater;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.oasisfeng.condom.CondomContext;
import com.oasisfeng.condom.CondomProcess;
import com.squareup.leakcanary.LeakCanary;

import org.polaric.colorful.Colorful;

import java.lang.ref.WeakReference;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by nuc on 11/29/2016.
 */

public abstract class BaseApp extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    protected static CondomContext context;
    private WeakReference<Activity> activityReference;
    private Action action;
    private Indexable indexable;
    private AppUpdater updater;
    private volatile static boolean visible;

    protected abstract void onOpen();

    protected abstract void onClose();

    protected abstract void onActivityOpen(Activity activity);

    protected abstract void onActivityClose(Activity activity);

    public abstract boolean enableTheme();

    public abstract boolean enableColor();

    public abstract Color getColor();

    protected abstract boolean enableAppIndex();

    protected abstract String getName();

    protected abstract String getDescription();

    protected abstract String getUrl();

    protected abstract boolean enableAppUpdate();

    protected abstract boolean enableRate();

    public abstract boolean enableCalligraphy();

    protected abstract String getFontPath();

    @Override
    public void onCreate() {
        super.onCreate();
        CondomProcess.installExceptDefaultProcess(this);
        context = CondomContext.wrap(this, getPackageName());
        registerActivityLifecycleCallbacks(this);
        AndroidUtil.setDebug(context);

        //Language.setFromPreference(context, getString(R.string.key_language));

        if (enableCalligraphy()) {
            //setupCalligraphy(getFontPath());
        }

        if (enableTheme()) {
            Colorful.init(context);
        }

        if (enableAppIndex()) {
            action = getAction(getDescription(), getUrl());
            indexable = Indexables.newSimple(getName(), getUrl());
            startAppIndex();
        }

/*        if (enableAppUpdate()) {
            startAppUpdate();
        }*/

        if (enableRate()) {
            startRating();
        }

        onOpen();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Language.setFromPreference(context, getString(R.string.key_language));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //todo calculate available memory and report
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopAppIndex();
        onClose();
    }

    public Activity getCurrentActivity() {
        return activityReference != null ? activityReference.get() : null;
    }

    public static CondomContext getContext() {
        return context;
    }


    private void startAppIndex() {
        if (AndroidUtil.isDebug()) return;
        FirebaseAppIndex.getInstance().update(indexable);
        FirebaseUserActions.getInstance().start(action);
    }

    private void stopAppIndex() {
        if (AndroidUtil.isDebug()) return;
        FirebaseUserActions.getInstance().end(action);
    }

    protected Action getAction(String description, String uri) {
        return new Action.Builder(Action.Builder.VIEW_ACTION)
                .setObject(description, uri)
                .build();
    }

    public void jumpIfNeeded(Activity activity) {
        if (needForceJump() && getJumpingClass() != null) {
            //Intent intent = new Intent(activity, getJumpingClass());
            //startActivity(intent);
            AndroidUtil.openDestroyActivity(activity, getJumpingClass());
            //activity.finish();
        }
    }

    protected boolean needForceJump() {
        return false;
    }

    protected <T extends BaseActivity> Class<T> getJumpingClass() {
        return null;
    }

    private void startAppUpdate() {
        if (updater == null) {
            updater = new AppUpdater(getCurrentActivity());
            updater.start();
        }
        // new GoogleChecker(this, false);
    }

    private void stopAppUpdate() {
        if (updater != null) {
            updater.stop();
            updater = null;
        }
    }

    public void startRating() {
        Activity activity = getCurrentActivity();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        final RatingDialog ratingDialog = new RatingDialog.Builder(activity)
                .threshold(3)
                .session(7)
                .onRatingBarFormSumbit(feedback -> {

                }).build();

        ratingDialog.show();
    }

    protected void setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(context)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        //enabledStrictMode();
        LeakCanary.install(this);
    }

    protected void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                .detectAll() //
                .penaltyLog() //
                .penaltyDeath() //
                .build());
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        activityReference = new WeakReference<>(activity);
        if (enableAppUpdate()) {
            startAppUpdate();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        visible = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        visible = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activityReference != null) {
            activityReference.clear();
        }
        if (enableAppUpdate()) {
            stopAppUpdate();
        }
    }

    public static boolean isVisible() {
        return visible;
    }

    private void setupCalligraphy(String fontPath) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(fontPath)
                .setFontAttrId(R.attr.fontPath)
                // .addCustomStyle(TextF.class, R.attr.textFieldStyle)
                .build()
        );
    }

    protected <T extends JobService> void openService(Class<T> clazz, int period) {
        ServiceManager.onInstance().scheduleService(context, clazz, period);
    }

    protected <T extends JobService> void openPowerService(Class<T> clazz, int period) {
        ServiceManager.onInstance().schedulePowerService(context, clazz, period);
    }
}
