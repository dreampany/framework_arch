package com.dreampany.framework.data.api.network.data.model;

import com.dreampany.framework.data.enums.Type;
import com.dreampany.framework.data.model.BaseSerial;

/**
 * Created by air on 26/12/17.
 */

public class Network<T extends Type> extends BaseSerial {
    private T type;
    private boolean connected;

    public Network(T type) {
        this.type = type;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public T getType() {
        return type;
    }

    public boolean isConnected() {
        return connected;
    }
}
