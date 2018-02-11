package com.dreampany.framework.data.event;

import com.dreampany.framework.data.enums.NetworkStatus;

/**
 * Created by nuc on 6/11/2017.
 */

public class NetworkEvent extends Event {
    final NetworkStatus networkStatus;

    protected NetworkEvent(NetworkStatus networkStatus) {
        this.networkStatus = networkStatus;
    }

    public NetworkStatus getNetworkStatus() {
        return networkStatus;
    }
}
