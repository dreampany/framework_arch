package com.dreampany.framework.data.provider.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nuc on 1/12/2017.
 */

public abstract class BasePref {

    protected int getMode() {
        return Context.MODE_PRIVATE;
    }

    protected String getPrefName() {
        return context.getPackageName();
    }

    protected void release() {
        context = null;
        publicPref = null;
        privatePref = null;
    }

    protected Context context;
    private SharedPreferences publicPref;
    private SharedPreferences privatePref;

    protected BasePref(Context context) {
        this.context = context.getApplicationContext();
        publicPref = PreferenceManager.getDefaultSharedPreferences(context);
        privatePref = this.context.getSharedPreferences(getPrefName(), getMode());
    }

    public void setPublicValue(String key, String value) {
        SharedPreferences.Editor editor = publicPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setPublicValue(String key, boolean value) {
        SharedPreferences.Editor editor = publicPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void setPrivateValue(String key, String value) {
        SharedPreferences.Editor editor = privatePref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setPrivateValue(String key, int value) {
        SharedPreferences.Editor editor = privatePref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void setPrivateValue(String key, long value) {
        SharedPreferences.Editor editor = privatePref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void setPrivateValue(String key, boolean value) {
        SharedPreferences.Editor editor = privatePref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getPublicString(String key, String defaultValue) {
        return publicPref.getString(key, defaultValue);
    }

    public String getPrivateString(String key, String defaultValue) {
        return privatePref.getString(key, defaultValue);
    }

    public int getPrivateInt(String key, int defaultValue) {
        return privatePref.getInt(key, defaultValue);
    }

    public long getPrivateLong(String key, long defaultValue) {
        return privatePref.getLong(key, defaultValue);
    }

    public boolean getPrivateBoolean(String key, boolean defaultValue) {
        return privatePref.getBoolean(key, defaultValue);
    }

    public boolean getPublicBoolean(String key, boolean defaultValue) {
        return publicPref.getBoolean(key, defaultValue);
    }

    public void removePublic(String key) {
        publicPref.edit().remove(key).apply();
    }

    public void removePrivate(String key) {
        privatePref.edit().remove(key).apply();
    }

    public boolean hasPublic(String key) {
        return publicPref.contains(key);
    }

    public boolean hasPrivate(String key) {
        return privatePref.contains(key);
    }
}
