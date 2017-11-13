/*package com.dreampany.framework.data.provider.pref;

import android.content.Context;

*//**
 * Created by nuc on 5/1/2017.
 *//*

public final class FramePref extends BasePref {

    private static final String tour = "tour";
    private static final String register = "register";

    private static FramePref instance;

    private FramePref(Context context) {
        super(context);
    }

    synchronized public static FramePref onInstance(Context context) {
        if (instance == null) {
            instance = new FramePref(context);
        }
        return instance;
    }

    @Override
    protected String getPrefName() {
        return getClass().getSimpleName();
    }

    public void setValue(String key, long value) {
        setPrivateValue(key, value);
    }

    public long getLong(String key) {
        return getPrivateLong(key, 0L);
    }

    public boolean isToured() {
        boolean toured = getPublicBoolean(tour, false);
        return toured;
    }

    public boolean isRegistered() {
        boolean registered = getPublicBoolean(register, false);
        return registered;
    }

    public void tourCompleted() {
        setPublicValue(tour, true);
    }

    public void registerCompleted() {
        setPublicValue(register, true);
    }
}*/
