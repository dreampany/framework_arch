package com.dreampany.framework.data.enums;

public enum MobileNetworkType {
    NONE("none"),
    LTE("LTE"),
    HSPAP("HSPAP"),
    EDGE("EDGE"),
    GPRS("GPRS");

    private final String type;

    MobileNetworkType(String status) {
        this.type = status;
    }

    @Override
    public String toString() {
        return type;
    }
}