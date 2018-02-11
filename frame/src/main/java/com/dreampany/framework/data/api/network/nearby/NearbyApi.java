package com.dreampany.framework.data.api.network.nearby;

import android.Manifest;
import android.content.Context;

import com.dreampany.framework.data.manager.PermissionManager;
import com.dreampany.framework.data.api.network.data.model.Id;
import com.dreampany.framework.data.api.network.data.model.Meta;
import com.dreampany.framework.data.api.network.data.model.Peer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;

/**
 * Created by Roman on 8/3/2017.
 */

public class NearbyApi extends Api {

    private static final String permission = Manifest.permission.ACCESS_FINE_LOCATION;



    public interface Processor {
        void putMeta(Id id, Meta meta);

        void putFile(Id id, File file);

        Meta getMeta(Id id);

        File getFile(Id id);
    }

    public enum PayloadState {
        SUCCESS, FAILURE, PROGRESS
    }

    public interface Callback {
        void onPeer(Peer peer, Peer.State state);

        void onData(Peer peer, byte[] data);

        void onStatus(long payloadId, PayloadState state, long totalBytes, long bytesTransferred);
    }

    public NearbyApi() {
        super();
    }

    synchronized public boolean init(Context context, long serviceId, long peerId, byte[] peerData) {

        if (inited) {
            return true;
        }

        if (context == null) {
            throw new IllegalArgumentException("Context may not null");
        }

        if (serviceId == 0L) {
            throw new IllegalArgumentException("ServiceId may not zero");
        }

        if (peerId == 0L) {
            throw new IllegalArgumentException("PeerId may not zero");
        }

        if (this.context == null) {
            this.context = context.getApplicationContext();
        }

        //check some permission and google api
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.context) != ConnectionResult.SUCCESS) {
            throw new IllegalStateException("Google Play service is not available");
        }

        if (!PermissionManager.isPermitted(this.context, permission) ) {
            throw new IllegalStateException("Location permission missing");
        }

        this.serviceId = serviceId;

        peer = new Peer();
        peer.id = peerId;
        peer.data = peerData;
        inited = true;
        return inited;
    }

    synchronized public void register(Callback callback) {
        callbacks.add(callback);
    }

    synchronized public void unregister(Callback callback) {
        callbacks.remove(callback);
    }

    synchronized public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    synchronized public void unsetProcessor() {
        this.processor = null;
    }

    synchronized public void start() {
        super.start();
    }

    synchronized public void stop() {
        super.stop();
    }

    /**
     * @param id
     * @param data
     * @param timeoutInMs timeout in millis
     */
    synchronized public void send(Id id, byte[] data, long timeoutInMs) {
        byte[] packet = Packets.getDataPacket(data);
        super.sendPacket(id, packet, timeoutInMs);
    }

    synchronized public void send(Id id, File file, long timeout) {
        super.sendFile(id, file, timeout);
    }
}