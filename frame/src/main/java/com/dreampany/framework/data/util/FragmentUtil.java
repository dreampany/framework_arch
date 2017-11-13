package com.dreampany.framework.data.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.dreampany.framework.data.enums.Type;
import com.dreampany.framework.data.model.Base;
import com.dreampany.framework.data.model.Task;

/**
 * Created by nuc on 4/12/2016.
 */
public final class FragmentUtil {
    private FragmentUtil() {
    }

    public static Fragment getFragmentById(Activity activity, int fragmentId) {
        return activity.getFragmentManager().findFragmentById(fragmentId);
    }

    public static <T extends Fragment> T getFragmentByTag(Activity activity, String fragmentTag) {
        return (T) activity.getFragmentManager().findFragmentByTag(fragmentTag);
    }

    public static <T extends android.support.v4.app.Fragment> T getSupportFragmentByTag(AppCompatActivity activity, String fragmentTag) {
        return (T) activity.getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static android.support.v4.app.Fragment getFragmentById(android.support.v4.app.Fragment fragment, int fragmentId) {
        return fragment.getChildFragmentManager().findFragmentById(fragmentId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static <F extends android.support.v4.app.Fragment> F getFragmentByTag(android.support.v4.app.Fragment fragment, String fragmentTag) {
        return (F) fragment.getChildFragmentManager().findFragmentByTag(fragmentTag);
    }

    public static <F extends Fragment> F newFragment(Class<F> fragmentClass) {
        try {
            return fragmentClass.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    public static <F extends android.support.v4.app.Fragment> F newSupportFragment(Class<F> fragmentClass) {
        try {
            return fragmentClass.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }


    public static <F extends Fragment, T extends Base, X extends Type> F getFragment(Activity activity, Class<F> fragmentClass, Task<T> task) {

        F fragment = getFragmentByTag(activity, fragmentClass.getName());

        if (fragment == null) {

            fragment = newFragment(fragmentClass);

            if (fragment != null && task != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Task.class.getName(), task);
                fragment.setArguments(bundle);
            }
        } else if (task != null) {
            fragment.getArguments().putSerializable(Task.class.getName(), task);
        }

        return fragment;
    }


    public static <F extends Fragment, T extends Parcelable> F getFragment(Activity activity, Class<F> fragmentClass) {

        F fragment = getFragmentByTag(activity, fragmentClass.getName());

        if (fragment == null) {
            fragment = newFragment(fragmentClass);
            LogKit.verbose("New Fragment " + fragment);
           /* if (fragment != null && config != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(config.getClass().getName(), config);
                fragment.setArguments(bundle);
            }*/
        } /*else if (config != null) {
            fragment.getArguments().putParcelable(config.getClass().getName(), config);
        }*/

        return fragment;
    }

    public static <F extends android.support.v4.app.Fragment, T extends Parcelable> F getSupportFragment(AppCompatActivity activity, Class<F> fragmentClass, Task<?> task) {

        F fragment = getSupportFragmentByTag(activity, fragmentClass.getName());

        if (fragment == null) {
            fragment = newSupportFragment(fragmentClass);
            LogKit.verbose("New Fragment " + fragment);
            if (fragment != null && task != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Task.class.getName(), task);
                fragment.setArguments(bundle);
            }
        } else if (task != null) {
            fragment.getArguments().putSerializable(Task.class.getName(), task);
        }

        return fragment;
    }

    public static <F extends android.support.v4.app.Fragment, T extends Parcelable> F getSupportFragment(android.support.v4.app.Fragment parent, Class<F> fragmentClass, Task<?> task) {

        F fragment = getFragmentByTag(parent, fragmentClass.getName());

        if (fragment == null) {
            fragment = newSupportFragment(fragmentClass);
            LogKit.verbose("New Fragment " + fragment);
            if (fragment != null && task != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Task.class.getName(), task);
                fragment.setArguments(bundle);
            }
        } else if (task != null) {
            fragment.getArguments().putSerializable(Task.class.getName(), task);
        }

        return fragment;
    }

    public static <F extends android.support.v4.app.Fragment, T extends Parcelable> F commitFragment(final AppCompatActivity activity, final F fragment, final int parentId) {

        Runnable commitRunnable = new Runnable() {
            @Override
            public void run() {
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(parentId, fragment, fragment.getClass().getName())
                        .commitAllowingStateLoss();
            }
        };

        AndroidUtil.postDelay(commitRunnable);

        return fragment;
    }

    public static <F extends Fragment, T extends Base> Fragment commitFragment(final Activity activity, final Class<F> fragmentClass, final Task<T> task, final int parentLayoutId) {

        final Fragment fragment = getFragment(activity, fragmentClass, task);

        Runnable commitRunnable = new Runnable() {
            @Override
            public void run() {
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
                activity.getFragmentManager().
                        beginTransaction().
                        setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).
                        replace(parentLayoutId, fragment, fragmentClass.getName()).
                        // addToBackStack(fragmentClass.getName()).
                                commitAllowingStateLoss();
            }
        };

        AndroidUtil.postDelay(commitRunnable);

        return fragment;
    }

    public static <F extends Fragment, T extends Parcelable> F commitFragment(final Activity activity, final Class<F> fragmentClass, final int parentId) {

        final F fragment = getFragment(activity, fragmentClass);

        Runnable commitRunnable = new Runnable() {
            @Override
            public void run() {
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
                activity.getFragmentManager().
                        beginTransaction().
                        setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).
                        replace(parentId, fragment, fragmentClass.getName()).
                        commitAllowingStateLoss();
            }
        };

        AndroidUtil.postDelay(commitRunnable);

        return fragment;
    }

    public static <F extends android.support.v4.app.Fragment, T extends Parcelable> F commitFragmentCompat(final AppCompatActivity activity, final Class<F> fragmentClass, final int parentId) {

        final F fragment = getSupportFragment(activity, fragmentClass, null);

        Runnable commitRunnable = new Runnable() {
            @Override
            public void run() {
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
                activity.getSupportFragmentManager().
                        beginTransaction().
                        //setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).
                                replace(parentId, fragment, fragmentClass.getName()).
                        commitAllowingStateLoss();
            }
        };

        AndroidUtil.postDelay(commitRunnable);

        return fragment;
    }

    public static <F extends android.support.v4.app.Fragment, T extends Parcelable> F commitFragmentCompat(final AppCompatActivity activity, final Class<F> fragmentClass, final int parentId, Task<?> task) {

        final F fragment = getSupportFragment(activity, fragmentClass, task);

        Runnable commitRunnable = new Runnable() {
            @Override
            public void run() {
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
                activity.getSupportFragmentManager().
                        beginTransaction().
                        //setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).
                                replace(parentId, fragment, fragmentClass.getName()).
                        commitAllowingStateLoss();
            }
        };

        AndroidUtil.postDelay(commitRunnable);

        return fragment;
    }

    public static <F extends android.support.v4.app.Fragment, T extends Parcelable> F commitFragmentCompat(final android.support.v4.app.Fragment parent, final Class<F> fragmentClass, final int parentId) {

        final F fragment = getSupportFragment(parent, fragmentClass, null);

        Runnable commitRunnable = new Runnable() {
            @Override
            public void run() {
                parent.getChildFragmentManager().
                        beginTransaction().
                        //setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).
                                replace(parentId, fragment, fragmentClass.getName()).
                        commitAllowingStateLoss();
            }
        };

        AndroidUtil.post(commitRunnable, 500L); // need a good time to load parent fragment

        return fragment;
    }
}

