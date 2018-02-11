package com.dreampany.framework.data.api.network.data.model;


import com.dreampany.framework.data.util.DataUtil;

public class Wifi implements Comparable<Wifi> {

    private String bssid;
    private String ssid;
    private String capabilities;

    public Wifi() {
    }

    @Override
    public boolean equals(Object inObject) {

        if (this == inObject) {
            return true;
        }

        if (!Wifi.class.isInstance(inObject)) {
            return false;
        }
        Wifi ap = (Wifi) inObject;
        return bssid.equals(ap.bssid);
    }

    @Override
    public int hashCode() {
        return bssid.hashCode();
    }

    @Override
    public int compareTo(Wifi wifi) {
        return bssid.compareTo(wifi.bssid);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("wifiap#");

        builder.append(" bssid: " + bssid);
        builder.append(" ssid: " + ssid);
        builder.append(" open: " + isOpen());

        return builder.toString();
    }

    public long getHash() {
        return DataUtil.getSha256(bssid);
    }

    public Wifi setBssid(String bssid) {
        this.bssid = bssid;
        return this;
    }

    public Wifi setSsid(String ssid) {
        this.ssid = ssid;
        return this;
    }

    public Wifi setCapabilities(String capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public String getBssid() {
        return bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public boolean isOpen() {
        return capabilities == null ||
                !(capabilities.contains("WEP") || capabilities.contains("PSK") || capabilities.contains("EAP"));
    }
}