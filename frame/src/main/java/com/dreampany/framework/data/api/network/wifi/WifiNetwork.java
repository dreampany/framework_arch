package com.dreampany.framework.data.api.network.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.dreampany.framework.data.manager.EventManager;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.LogKit;
import com.dreampany.framework.data.api.network.data.model.Ap;
import com.dreampany.framework.data.api.network.data.model.Wifi;


import com.github.pwittchen.reactivewifi.ReactiveWifi;

import java.util.List;
import java.util.Map;

import cc.mvdan.accesspoint.WifiApControl;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import com.google.common.collect.Maps;
/**
 * Created by air on 10/1/17.
 */

public class WifiNetwork {

    private static final String BSSID_FIX = "\"";
    private static final String BSSID_EMPTY = "";


    private static WifiNetwork instance;
    private Context context;
    private WifiManager wifi;
    private ConnectivityManager connectivity;
    private WifiApControl apControl;

    private Map<String, Wifi> wifis; //bssid to wifi

    private Subscription wifiSubscription;

    private WifiNetwork(Context context) {
        this.context = context.getApplicationContext();
        wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        apControl = WifiApControl.getInstance(this.context);
        wifis = Maps.newConcurrentMap();
    }

    synchronized public static WifiNetwork onInstance(Context context) {
        if (instance == null) {
            instance = new WifiNetwork(context);
        }
        return instance;
    }

    public boolean isWifiEnabled() {
        if (wifi == null) {
            return false;
        }
        return wifi.isWifiEnabled();
    }

    public boolean enableWifi() {
        if (wifi == null) {
            return false;
        }
        return wifi.setWifiEnabled(true);
    }

    public Wifi getConnectedWifi() {
        WifiInfo info = wifi.getConnectionInfo();

        String bssid = info.getBSSID();

        if (!wifis.containsKey(bssid)) {
            Wifi wifi = new Wifi();
            wifi.setBssid(bssid);
            wifi.setSsid(info.getSSID());
            wifis.put(bssid, wifi);
        }
        return wifis.get(bssid);
    }

    public Ap getAp() {

        if (apControl == null) {
            return null;
        }

        WifiConfiguration configuration = apControl.getWifiApConfiguration();

        if (configuration != null) {
            Ap ap = new Ap();
            ap.setBssid(configuration.BSSID);
            ap.setSsid(configuration.SSID);
            return ap;
        }

        return null;
    }

    public void scanWifi() {
        if (wifiSubscription != null) {
            return;
        }
        wifiSubscription = ReactiveWifi.observeWifiAccessPoints(context)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<List<ScanResult>>() {
                    @Override
                    public void call(List<ScanResult> scanResults) {

                        for (ScanResult sr : scanResults) {
                            LogKit.verbose("Bssid " + sr.BSSID);

                            String bssid = sr.BSSID;

                            if (!wifis.containsKey(bssid)) {
                                Wifi wifi = new Wifi();
                                wifi.setBssid(bssid);
                                wifi.setSsid(sr.SSID);
                                wifi.setCapabilities(sr.capabilities);
                                wifis.put(bssid, wifi);
                            }
                            Wifi wifi = wifis.get(bssid);
                            EventManager.post(wifi);
                        }

                    }
                });
    }

    public void stop() {
        safelyUnsubscribe(wifiSubscription);
    }


    int getNetworkId(String ssid) {
        return getNetworkId(ssid, null);
    }

    int getNetworkId(String ssid, String password) {

        if (wifi == null) {
            return -1;
        }

        WifiConfiguration conf = password == null ? getOpenConfig(ssid) : getSecureConfig(ssid, password);
        int networkId = wifi.addNetwork(conf);

        if (networkId != -1) {
            return networkId;
        }

        List<WifiConfiguration> configs = wifi.getConfiguredNetworks();
        if (DataUtil.isEmpty(configs)) {
            return -1;
        }

        for (WifiConfiguration config : configs) {
            String configSsid = config.SSID.replaceAll(BSSID_FIX, BSSID_EMPTY);
            if (configSsid.equals(ssid)) {
                return config.networkId;
            }
        }

        return networkId;
    }

    boolean connectNetwork(int networkId) {
        if (wifi != null && networkId != -1) {
            wifi.disconnect();
            wifi.enableNetwork(networkId, true);
            return wifi.reconnect();
        }
        return false;
    }

    boolean isConnected(String ssid) {
        if (wifi == null) {
            return false;
        }
        WifiInfo connectionInfo = wifi.getConnectionInfo();
        if (connectionInfo != null && connectionInfo.getSSID() != null) {
            String currentSsid = connectionInfo.getSSID().replaceAll(BSSID_FIX, BSSID_EMPTY);

            if (currentSsid.equals(ssid)) {
                if (connectivity != null
                        && connectivity.getActiveNetworkInfo() != null
                        && connectivity.getActiveNetworkInfo().isAvailable()
                        && connectivity.getActiveNetworkInfo().isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    private WifiConfiguration getOpenConfig(String ssid) {
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + ssid + "\"";
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        return config;
    }

    private WifiConfiguration getSecureConfig(String ssid, String password) {
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = String.format("\"%s\"", ssid);
        config.preSharedKey = String.format("\"%s\"", password);
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        return config;
    }

    private void safelyUnsubscribe(Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }
}
