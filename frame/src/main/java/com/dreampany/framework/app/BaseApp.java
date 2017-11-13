package com.dreampany.framework.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.dreampany.framework.data.model.Color;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.Indexable;
import com.dreampany.framework.data.util.AndroidUtil;

import java.lang.ref.WeakReference;

/**
 * Created by nuc on 11/29/2016.
 */

public class BaseApp extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    protected static BaseApp instance;
    protected Context context;
    protected WeakReference<Activity> activityReference;
    protected Action action;
    protected Indexable indexable;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.context = getApplicationContext();
        registerActivityLifecycleCallbacks(this);
        AndroidUtil.setDebug(context);
        enableAppUpdate();
        rate();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        activityReference = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        activityReference = null;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public Activity getCurrentActivity() {
        return activityReference != null ? activityReference.get() : null;
    }

    public static BaseApp getInstance() {
        return instance;
    }

    public Color getColor() {
        return null;
    }

    public void trackMe(String screenName) {

    }

    public void appIndex(String name, String description, String url) {

    }

    public void startIndexingMe() {
    }

    public void stopIndexingMe() {

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

    protected Class getJumpingClass() {
        return null;
    }

    protected boolean enableAppUpdate() {
        return false;
    }

    protected boolean enableRate() {
        return false;
    }

    private void startAppUpdate() {
        if (!enableAppUpdate()) {
            return;
        }
/*        if (updater == null) {
            updater = new AppUpdater(getApplicationContext());
            updater.start();
        }*/
        // new GoogleChecker(this, false);
    }


    public void rate() {
        if (!enableRate()) {
            return;
        }
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
}
