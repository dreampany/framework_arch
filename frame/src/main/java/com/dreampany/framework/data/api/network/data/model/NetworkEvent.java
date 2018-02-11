package com.dreampany.framework.data.api.network.data.model;

import com.dreampany.framework.data.event.Event;

/**
 * Created by air on 11/6/17.
 */

public class NetworkEvent extends Event<Network> {
    private boolean connected;
    private boolean internet;

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setInternet(boolean internet) {
        this.internet = internet;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isInternet() {
        return internet;
    }
}
