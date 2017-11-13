package com.dreampany.framework.data.manager;

/**
 * Created by nuc on 6/5/2017.
 */

public class SmsManager {

    private static SmsManager instance;

    private SmsManager() {
    }

    synchronized public static SmsManager onInstance() {
        if (instance == null) {
            instance = new SmsManager();
        }
        return instance;
    }

    public void send(String text) {

    }
}
