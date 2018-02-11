package com.dreampany.framework.data.api.network.data.model;


import com.dreampany.framework.data.util.DataUtil;

/**
 * Created by nuc on 7/22/2017.
 */

public class Ap implements Comparable<Ap> {

    private String bssid;
    private String ssid;
    private String capabilities;

    public Ap() {
    }

    @Override
    public boolean equals(Object inObject) {

        if (this == inObject) {
            return true;
        }

        if (!Ap.class.isInstance(inObject)) {
            return false;
        }
        Ap ap = (Ap) inObject;
        return bssid.equals(ap.bssid);
    }

    @Override
    public int hashCode() {
        return bssid.hashCode();
    }

    @Override
    public int compareTo(Ap ap) {
        return bssid.compareTo(ap.bssid);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("ap#");

        builder.append(" bssid: " + bssid);
        builder.append(" ssid: " + ssid);
        builder.append(" open: " + isOpen());

        return builder.toString();
    }

    public long getHash() {
        return DataUtil.getSha256(bssid);
    }

   public Ap setBssid(String bssid) {
        this.bssid = bssid;
        return this;
    }

    public Ap setSsid(String ssid) {
        this.ssid = ssid;
        return this;
    }

    public Ap setCapabilities(String capabilities) {
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
