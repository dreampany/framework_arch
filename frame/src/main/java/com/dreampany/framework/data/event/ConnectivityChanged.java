package com.dreampany.framework.data.event;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.dreampany.framework.data.enums.MobileNetworkType;
import com.dreampany.framework.data.enums.NetworkStatus;

public final class ConnectivityChanged extends NetworkEvent {
    //private static final String MESSAGE_FORMAT = "ConnectivityChanged: %s";
    private final Context context;

    public ConnectivityChanged(Context context, NetworkStatus networkStatus) {
        super(networkStatus);
        this.context = context;
        // String message = String.format(MESSAGE_FORMAT, networkStatus.toString());
    }

    public MobileNetworkType getMobileNetworkType() {

        if (networkStatus != NetworkStatus.MOBILE_CONNECTED) {
            return MobileNetworkType.NONE;
        }

        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case (TelephonyManager.NETWORK_TYPE_LTE):
                return MobileNetworkType.LTE;
            case (TelephonyManager.NETWORK_TYPE_HSPAP):
                return MobileNetworkType.HSPAP;
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                return MobileNetworkType.EDGE;
            case (TelephonyManager.NETWORK_TYPE_GPRS):
                return MobileNetworkType.GPRS;
            default:
                return MobileNetworkType.NONE;
        }
    }
}