package com.dreampany.framework.data.provider.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by nuc on 12/10/2016.
 */

public abstract class BasePrefManager {

    private Context context;
    private SharedPreferences defaultPref;

    protected BasePrefManager(Context context) {
        this.context = context.getApplicationContext();
        defaultPref = PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    protected Context getContext() {
        return context;
    }

    protected SharedPreferences getDefaultPref() {
        return defaultPref;
    }


    public <V> void setData(V v) {
        setData(v.getClass().getName(), v);
    }

    public <K, V> void setData(K k, V v) {
        SharedPreferences pref = getDefaultPref();
        SharedPreferences.Editor editor = pref.edit();

        String key = String.class.isInstance(k) ? (String) k : k.getClass().getName();
        //String value = String.class.isInstance(v) ? (String) v : v.toString();

        Gson gson = new Gson();
        String value = gson.toJson(v);

        editor.putString(key, value);
        editor.apply();
    }

    public <V> V getData(Class<V> classOfV) {
        return getData(classOfV.getName(), classOfV);
    }

    public <K, V> V getData(K k, Class<V> classOfV) {
        SharedPreferences pref = getDefaultPref();
        if (pref == null) return null;

        String key = String.class.isInstance(k) ? (String) k : k.getClass().getName();
        String value = pref.getString(key, null);

        Gson gson = new Gson();
        return gson.fromJson(value, classOfV);
    }

    public <T> long getSyncTime(T t) {
        SharedPreferences pref = getDefaultPref();
        if (pref == null) return 0L;

        String key = String.class.isInstance(t) ? (String) t : t.getClass().getName();
        return pref.getLong(key, 0L);
    }

    public <T> void setSyncTime(T t, long time) {
        SharedPreferences pref = getDefaultPref();
        SharedPreferences.Editor editor = pref.edit();

        String key = String.class.isInstance(t) ? (String) t : t.getClass().getName();
        editor.putLong(key, time);
        editor.apply();
    }
}
