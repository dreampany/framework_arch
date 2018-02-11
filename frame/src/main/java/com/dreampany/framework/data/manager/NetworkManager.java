/*
package com.dreampany.framework.data.manager;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.dreampany.framework.data.enums.NetworkStatus;
import com.dreampany.framework.data.event.NetworkEvent;
import com.dreampany.framework.data.receiver.InternetReceiver;
import com.dreampany.framework.data.receiver.NetworkReceiver;
import com.dreampany.framework.data.receiver.WifiSignalStrengthReceiver;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

*/
/**
 * Created by nuc on 6/10/2017.
 *//*


*/
/**
 * Register for NetworkEvent using EventBus
 *//*

public final class NetworkManager extends Manager {

    private static NetworkManager instance;
    private Context context;
    private InternetManager internetManager;
    private NetworkReceiver networkReceiver;
    private InternetReceiver internetReceiver;
    private WifiSignalStrengthReceiver wifiSignalStrengthReceiver;
    private boolean wifiAccessPointsScanEnabled;
    public static NetworkStatus networkStatus = NetworkStatus.NONE;
    private Executor executor;

    private NetworkManager(Context context) {
        executor = Executors.newCachedThreadPool();
        this.context = context.getApplicationContext();
        this.internetManager = new InternetManager(executor);
        this.networkReceiver = new NetworkReceiver(this.context, internetManager);
        this.internetReceiver = new InternetReceiver(this.context);
        this.wifiSignalStrengthReceiver = new WifiSignalStrengthReceiver(this.context);
    }

    synchronized public static NetworkManager onInstance(Context context) {
        if (instance == null) {
            instance = new NetworkManager(context);
        }
        return instance;
    }


    @Override
    public void start() {
        super.start();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(networkReceiver, filter);

        filter = new IntentFilter();
        filter.addAction(InternetReceiver.INTENT);
        context.registerReceiver(internetReceiver, filter);

        if (wifiAccessPointsScanEnabled) {
            filter = new IntentFilter(WifiManager.RSSI_CHANGED_ACTION);
            context.registerReceiver(wifiSignalStrengthReceiver, filter);
            // start WiFi scan in order to refresh access point list
            // if this won't be called WifiSignalStrengthChanged may never occur
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.startScan();
        }
    }

    @Override
    public void stop() {
        context.unregisterReceiver(networkReceiver);
        context.unregisterReceiver(internetReceiver);
        if (wifiAccessPointsScanEnabled) {
            context.unregisterReceiver(wifiSignalStrengthReceiver);
        }
        super.stop();
    }

    @Override
    protected boolean looping() throws InterruptedException {
        return false;
    }

*/
/*    @Override
    public void onInternet(boolean internet) {
        for (NetworkCallback callback : callbacks) {
            callback.onInternet(internet);
        }
    }*//*


*/
/*    @Override
    public void onNetworkEvent(NetworkEvent event) {
        boolean hasInternet = false;
        switch (event.getNetworkStatus()) {
            case WIFI_CONNECTED_HAS_INTERNET:
            case MOBILE_CONNECTED:
                hasInternet = true;
                break;
        }

        for (NetworkCallback callback : callbacks) {
            callback.onNetworkEvent(event);
            callback.onInternet(hasInternet);
        }
    }*//*


    public NetworkManager enableWifiScan() {
        this.wifiAccessPointsScanEnabled = true;
        return this;
    }

    public NetworkManager enableInternetCheck() {
        networkReceiver.enableInternetCheck();
        return this;
    }

    public NetworkManager setPingParameters(String host, int port, int timeoutInMs) {
        internetManager.setPingParameters(host, port, timeoutInMs);
        return this;
    }

    public boolean hasInternet() {
        return internetManager.hasInternet();
    }

    public boolean hasConnection() {
        return networkReceiver.hasConnection();
    }
}
*/
