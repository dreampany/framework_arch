package com.dreampany.framework.data.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.dreampany.framework.data.event.WifiSignalStrengthChanged;

/**
 * Created by air on 4/21/17.
 */

public final class WifiSignalStrengthReceiver extends BaseReceiver {

    public WifiSignalStrengthReceiver(Context context) {
        super(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // We need to start WiFi scan after receiving an Intent
        // in order to get update with fresh data as soon as possible
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        post(new WifiSignalStrengthChanged(context));
    }
}
