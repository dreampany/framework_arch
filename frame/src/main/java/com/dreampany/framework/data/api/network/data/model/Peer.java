package com.dreampany.framework.data.api.network.data.model;

import com.dreampany.framework.data.model.BaseSerial;
import com.google.common.primitives.Longs;

/**
 * Created by nuc on 6/17/2017.
 */

public class Peer extends BaseSerial {
    public long id;
    public byte[] data;

    public Peer() {
    }

    @Override
    public boolean equals(Object inObject) {
        if (Peer.class.isInstance(inObject)) {
            Peer peer = (Peer) inObject;
            return id == peer.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Longs.hashCode(id);
    }

    @Override
    public String toString() {
        return "peer: \n" + "id - " + id + "\n" +
                "data - " + (data == null ? "null" : new String(data));
    }

    public enum State {
        LIVE,
        DEAD
    }
}
