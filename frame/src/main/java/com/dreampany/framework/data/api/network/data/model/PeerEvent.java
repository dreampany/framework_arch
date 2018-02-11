package com.dreampany.framework.data.api.network.data.model;

import com.dreampany.framework.data.event.Event;

/**
 * Created by air on 9/18/17.
 */

public class PeerEvent extends Event {

    public Peer peer;
    public Peer.State state;

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!PeerEvent.class.isInstance(obj)) {
            return false;
        }

        PeerEvent event = (PeerEvent) obj;

        return peer.equals(event.peer);
    }
}
