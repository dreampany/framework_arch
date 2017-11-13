package com.dreampany.framework.data.provider.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.dreampany.framework.data.util.AndroidUtil;
import com.github.pwittchen.prefser.library.rx2.Prefser;

/**
 * Created by air on 9/18/17.
 */

public class PrefManager {

    private static PrefManager instance;

    private Prefser publicPref;
    private Prefser privatePref;

    private PrefManager(Context context) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        publicPref = new Prefser(context.getApplicationContext());
        String prefName = AndroidUtil.getApplicationId(context.getApplicationContext());
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
        privatePref = new Prefser(pref);
    }

    synchronized public static PrefManager onInstance(Context context) {
        if (instance == null) {
            return new PrefManager(context);
        }
        return instance;
    }

    synchronized public static PrefManager onInstance() {
        return onInstance(null);
    }

    public Prefser publicPref() {
        return publicPref;
    }

    public Prefser privatePref() {
        return privatePref;
    }
}
