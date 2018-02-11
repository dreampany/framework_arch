package com.dreampany.framework.data.provider.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.dreampany.framework.data.util.AndroidUtil;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.github.pwittchen.prefser.library.rx2.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuc on 1/12/2017.
 */

public abstract class BasePref {

    protected Prefser publicPref;
    protected Prefser privatePref;

    protected BasePref(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }

        publicPref = new Prefser(context.getApplicationContext());
        String prefName = AndroidUtil.getApplicationId(context.getApplicationContext());
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
        privatePref = new Prefser(pref);
    }

    public <T> void setListItem(String key, T item) {
        TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {
        };
        List<T> inputs = new ArrayList<>();
        inputs.add(item);
        List<T> items = privatePref.get(key, typeToken, inputs);
        if (!items.contains(item)) {
            items.add(item);
        }
        privatePref.put(key, items, typeToken);
    }

    public <T> void setListItems(String key, List<T> items) {
        TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {
        };
        privatePref.put(key, items, typeToken);
    }

    public <T> List<T> getListItems(String key) {
        TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {
        };
        List<T> inputs = new ArrayList<>();
        return privatePref.get(key, typeToken, inputs);
    }

    public void setValue(String key, long value) {
        privatePref.put(key, value);
    }

    public void putString(String key, String value) {
        privatePref.put(key, value);
    }

    public String getString(String key) {
        return privatePref.get(key, String.class, null);
    }

    public long getLong(String key, long defaultValue) {
        return privatePref.get(key, Long.class, defaultValue);
    }
}
