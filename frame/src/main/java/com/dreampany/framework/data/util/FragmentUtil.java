package com.dreampany.framework.data.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.dreampany.framework.data.model.Task;

import java.io.Serializable;

/**
 * Created by nuc on 4/12/2016.
 */
public final class FragmentUtil {
    private FragmentUtil() {
    }

    public static <T extends Fragment> T getSupportFragmentByTag(AppCompatActivity activity, String fragmentTag) {
        return (T) activity.getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Fragment getFragmentById(Fragment fragment, int fragmentId) {
        return fragment.getChildFragmentManager().findFragmentById(fragmentId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static <F extends Fragment> F getFragmentByTag(Fragment fragment, String fragmentTag) {
        return (F) fragment.getChildFragmentManager().findFragmentByTag(fragmentTag);
    }

    public static <F extends Fragment> F getFragmentByTag(FragmentManager manager, String fragmentTag) {
        return (F) manager.findFragmentByTag(fragmentTag);
    }

    public static <F extends Fragment> F newSupportFragment(Class<F> fragmentClass) {
        try {
            return fragmentClass.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    public static <F extends Fragment, T extends Parcelable> F getSupportFragment(AppCompatActivity activity, Class<F> fragmentClass, Task task) {

        F fragment = getSupportFragmentByTag(activity, fragmentClass.getName());

        if (fragment == null) {
            fragment = newSupportFragment(fragmentClass);
            if (fragment != null && task != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Task.class.getName(), task);
                fragment.setArguments(bundle);
            }
        } else if (task != null) {
            if (task instanceof Parcelable) {
                fragment.getArguments().putParcelable(Task.class.getName(), (Parcelable) task);
            } else if (task instanceof Serializable) {
                fragment.getArguments().putSerializable(Task.class.getName(), task);
            }
        }

        return fragment;
    }

    public static <F extends Fragment, T extends Parcelable> F getSupportFragment(Fragment parent, Class<F> fragmentClass, Task task) {

        F fragment = getFragmentByTag(parent, fragmentClass.getName());

        if (fragment == null) {
            fragment = newSupportFragment(fragmentClass);
           // LogKit.verbose("New Fragment " + fragment);
            if (fragment != null && task != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Task.class.getName(), task);
                fragment.setArguments(bundle);
            }
        } else if (task != null) {
            if (task instanceof Parcelable) {
                fragment.getArguments().putParcelable(Task.class.getName(), (Parcelable) task);
            } else if (task instanceof Serializable) {
                fragment.getArguments().putSerializable(Task.class.getName(), task);
            }
        }

        return fragment;
    }

    public static <F extends Fragment, T extends Parcelable> F getSupportFragment(FragmentManager parent, Class<F> fragmentClass, String tag, Task task) {

        F fragment = getFragmentByTag(parent, tag);

        if (fragment == null) {
            fragment = newSupportFragment(fragmentClass);
           // LogKit.verbose("New Fragment " + fragment);
            if (fragment != null && task != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Task.class.getName(), task);
                fragment.setArguments(bundle);
            }
        } else if (task != null) {
            if (task instanceof Parcelable) {
                fragment.getArguments().putParcelable(Task.class.getName(), (Parcelable) task);
            } else if (task instanceof Serializable) {
                fragment.getArguments().putSerializable(Task.class.getName(), task);
            }
        }

        return fragment;
    }

    public static <F extends Fragment, T extends Parcelable> F commitFragment(final AppCompatActivity activity, final F fragment, final int parentId) {

        Runnable commitRunnable = () -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(parentId, fragment, fragment.getClass().getName())
                    .commitAllowingStateLoss();
        };

        AndroidUtil.postDelay(commitRunnable);

        return fragment;
    }

    public static <F extends Fragment, T extends Parcelable> F commitFragment(final AppCompatActivity activity, final Class<F> fragmentClass, final int parentId) {

        final F fragment = getSupportFragment(activity, fragmentClass, null);

        Runnable commitRunnable = () -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            activity.getSupportFragmentManager().
                    beginTransaction().
                    //setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).
                            replace(parentId, fragment, fragmentClass.getName()).
                    commitAllowingStateLoss();
        };

        AndroidUtil.postDelay(commitRunnable);

        return fragment;
    }

    public static <F extends Fragment, T extends Parcelable> F commitPersistentFragment(final AppCompatActivity activity, final Class<F> fragmentClass, final int parentId) {

        final F fragment = getSupportFragment(activity, fragmentClass, null);

        Runnable commitRunnable = () -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            //fragment.setRetainInstance(true);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(parentId, fragment, fragmentClass.getName())
                    //.addToBackStack(null)
                    .commitAllowingStateLoss();
        };

        AndroidUtil.postDelay(commitRunnable);

        return fragment;
    }

    public static <F extends Fragment, T extends Parcelable> F commitFragment(final AppCompatActivity activity, final Class<F> fragmentClass, final int parentId, Task task) {

        final F fragment = getSupportFragment(activity, fragmentClass, task);

        Runnable commitRunnable = () -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            activity.getSupportFragmentManager().
                    beginTransaction().
                    //setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).
                            replace(parentId, fragment, fragmentClass.getName()).
                    commitAllowingStateLoss();
        };

        AndroidUtil.postDelay(commitRunnable);

        return fragment;
    }

    public static <F extends Fragment, T extends Parcelable> F commitFragment(final Fragment parent, final Class<F> fragmentClass, final int parentId) {

        final F fragment = getSupportFragment(parent, fragmentClass, null);

        Runnable commitRunnable = () -> parent.getChildFragmentManager().
                beginTransaction().
                //setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).
                        replace(parentId, fragment, fragmentClass.getName()).
                        commitAllowingStateLoss();

        AndroidUtil.post(commitRunnable, 500L); // need a good time to load parent fragment

        return fragment;
    }
}

