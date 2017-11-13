package com.dreampany.framework.data.enums;

/**
 * Created by air on 4/20/17.
 */

public enum NetworkStatus {

    NONE("none"),
    WIFI_CONNECTED("connected to WiFi"),
    WIFI_CONNECTED_HAS_INTERNET("connected to WiFi (Internet available)"),
    WIFI_CONNECTED_HAS_NO_INTERNET("connected to WiFi (Internet not available)"),
    MOBILE_CONNECTED("connected to mobile network"),
    OFFLINE("offline");

    private final String status;

    NetworkStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
