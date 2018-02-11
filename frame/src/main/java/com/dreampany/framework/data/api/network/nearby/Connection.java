package com.dreampany.framework.data.api.network.nearby;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dreampany.framework.data.structure.SmartQueue;
import com.dreampany.framework.data.thread.Runner;
import com.dreampany.framework.data.util.LogKit;
import com.dreampany.framework.data.util.NumberUtil;
import com.dreampany.framework.data.util.TimeUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by air on 10/10/17.
 */
class Connection {

    private enum State {
        FOUND, LOST, REQUESTING, REQUEST_SUCCESS, REQUEST_FAILED, INITIATED, ACCEPTED, REJECTED, DISCONNECTED
    }

    interface Callback {
        void onConnection(long peerId, boolean connected);

        void onPayload(long peerId, Payload payload);

        void onPayloadStatus(long peerId, PayloadTransferUpdate status);
    }

    private Context context;
    private long serviceId;
    private long peerId;
    private Callback callback;
    private GoogleApiClient google;
    private AdvertisingOptions advertisingOptions;
    private DiscoveryOptions discoveryOptions;

    private boolean advertising = false;
    private boolean discovering = false;

    private BiMap<Long, String> cache;
    private BiMap<Long, String> endpoints; // peerId to endpointId
    private Map<String, State> states;     // endpointId to State
    private Map<String, Boolean> directs;
    private SmartQueue<String> pendingEndpoints;

    //private Executor executor;

    private RequestThread requestThread;


    Connection(Context context, long serviceId, long peerId, Callback callback) {

        this.context = context;
        this.serviceId = serviceId;
        this.peerId = peerId;
        this.callback = callback;

        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.context) != ConnectionResult.SUCCESS) {
            throw new IllegalStateException("Google Play service is not available");
        }

        google = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle connectionHint) {
                        LogKit.verbose("Google Api Connected");
                        startAdvertising();
                        startDiscovery();
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        stopAdvertising();
                        stopDiscovery();
                    }
                })
                //.addOnConnectionFailedListener(this)
                .addApi(Nearby.CONNECTIONS_API)
                .build();

        advertisingOptions = new AdvertisingOptions(Strategy.P2P_CLUSTER);
        discoveryOptions = new DiscoveryOptions(Strategy.P2P_CLUSTER);
        cache = HashBiMap.create();
        endpoints = HashBiMap.create();
        states = Maps.newConcurrentMap();
        directs = Maps.newConcurrentMap();
        pendingEndpoints = new SmartQueue<>();
        //executor = Executors.newCachedThreadPool();
    }

    void start() {
        if (google.isConnected() || google.isConnecting()) {
            return;
        }
        google.connect();
    }

    void stop() {
        stopRequestThread();
        if (google.isConnected() || google.isConnecting()) {
            google.disconnect();
        }
    }

    private void startAdvertising() {
        if (advertising) {
            return;
        }
        Nearby.Connections
                .startAdvertising(
                        google,
                        toString(peerId),
                        toString(serviceId),
                        lifecycleCallback,
                        advertisingOptions)
                .setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {
                    @Override
                    public void onResult(@NonNull Connections.StartAdvertisingResult result) {
                        advertising = result.getStatus().isSuccess();
                        LogKit.verbose("Advertising " + advertising);
                    }
                });
    }

    private void startDiscovery() {
        if (discovering) {
            return;
        }
        Nearby.Connections
                .startDiscovery(
                        google,
                        toString(serviceId),
                        discoveryCallback,
                        discoveryOptions)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        discovering = status.isSuccess();
                        LogKit.verbose("Discovering " + discovering);
                    }
                });
    }

    private void stopAdvertising() {
        Nearby.Connections.stopAdvertising(google);
    }

    private void stopDiscovery() {
        Nearby.Connections.stopDiscovery(google);
    }

    private void requestConnection(final String endpointId) {

        LogKit.verbose("Request Connection: " + states.get(endpointId));

        Nearby.Connections.requestConnection(
                google,
                toString(peerId),
                endpointId,
                lifecycleCallback)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        LogKit.verbose("Request Connection for " + endpointId + ": " + status.isSuccess());
                        states.put(endpointId, status.isSuccess() ? State.REQUEST_SUCCESS : State.REQUEST_FAILED);

                        if (status.isSuccess()) {
                            pendingEndpoints.remove(endpointId);
                        } else {
                            pendingEndpoints.insertLastUniquely(endpointId);
                            startRequestThread();
                        }
                    }
                });

        pendingEndpoints.remove(endpointId);
        states.put(endpointId, State.REQUESTING);
    }

    private final EndpointDiscoveryCallback discoveryCallback = new EndpointDiscoveryCallback() {

        @Override
        public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
            long serviceId = toLong(info.getServiceId());
            long peerId = toLong(info.getEndpointName());

            if (Connection.this.serviceId != serviceId) {
                return;
            }

            LogKit.verbose("endpoint found " + endpointId + " id " + peerId);

            //priority works: remove old endpoints if exists
            if (endpoints.containsKey(peerId)) {
                String oldEndpointId = endpoints.get(peerId);
                states.remove(oldEndpointId);
                directs.remove(oldEndpointId);
                pendingEndpoints.remove(oldEndpointId);
            }

            endpoints.put(peerId, endpointId);
            states.put(endpointId, State.FOUND);
            pendingEndpoints.insertLastUniquely(endpointId);
            startRequestThread();
        }

        @Override
        public void onEndpointLost(String endpointId) {
            LogKit.verbose("endpoint lost " + endpointId);
            states.put(endpointId, State.LOST);
        }
    };


    private final ConnectionLifecycleCallback lifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(String endpointId, ConnectionInfo info) {
            long peerId = toLong(info.getEndpointName());
            endpoints.put(peerId, endpointId);
            states.put(endpointId, State.INITIATED);
            directs.put(endpointId, info.isIncomingConnection());
            Nearby.Connections.acceptConnection(google, endpointId, payloadCallback);
            LogKit.verbose("Connected initiated for " + endpointId + " id: " + peerId + " incoming " + info.isIncomingConnection());
        }

        @Override
        public void onConnectionResult(String endpointId, ConnectionResolution result) {
            boolean accepted = false;

            if (result.getStatus().getStatusCode() == ConnectionsStatusCodes.STATUS_OK) {
                accepted = true;
            }

            LogKit.verbose("---Connection Result endpoint: " + endpointId + " accepted " + accepted);
            states.put(endpointId, accepted ? State.ACCEPTED : State.REJECTED);

            if (accepted) {
                pendingEndpoints.remove(endpointId);
                long peerId = endpoints.inverse().get(endpointId);
                callback.onConnection(peerId, true);
            } else {
                pendingEndpoints.insertLastUniquely(endpointId);
                startRequestThread();
            }
        }

        @Override
        public void onDisconnected(String endpointId) {
            LogKit.verbose("---Disconnected endpoint: " + endpointId);
            states.put(endpointId, State.DISCONNECTED);
            pendingEndpoints.insertLastUniquely(endpointId);
            long peerId = endpoints.inverse().get(endpointId);
            callback.onConnection(peerId, false);
            startRequestThread();
        }
    };

    private final PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String endpointId, Payload payload) {
            LogKit.verbose("Payload Received from: " + endpointId);
            long peerId = endpoints.inverse().get(endpointId);
            callback.onPayload(peerId, payload);
        }

        @Override
        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
            LogKit.verbose("Payload Transfer Update from: " + endpointId);
            long peerId = endpoints.inverse().get(endpointId);
            callback.onPayloadStatus(peerId, update);
        }
    };

    private void startRequestThread() {
        if (requestThread == null || !requestThread.isRunning()) {
            requestThread = new RequestThread();
            requestThread.start();
        }
        requestThread.notifyRunner();
    }

    private void stopRequestThread() {
        if (requestThread != null) {
            requestThread.stop();
        }
    }

    private class RequestThread extends Runner {
        int maxTry;
        Map<String, Long> times;
        Map<String, Long> delays;
        Map<String, Integer> tries;

        RequestThread() {
            times = Maps.newHashMap();
            delays = Maps.newHashMap();
            tries = Maps.newHashMap();
            maxTry = 5;
        }

        @Override
        protected boolean looping() throws InterruptedException {

            String endpointId = pendingEndpoints.pollFirst();
            // LogKit.verbose("Request Thread...." + endpointId);
            if (endpointId == null) {
                waitRunner(wait);
                wait += defaultWait;
                return true;
            }
            wait = defaultWait;

            //incoming endpoints: so don't make request on it
            if (directs.containsKey(endpointId) && directs.get(endpointId)) {
                return true;
            }

            if (!times.containsKey(endpointId)) {
                times.put(endpointId, TimeUtil.currentTime());
            }

            if (!delays.containsKey(endpointId)) {
                delays.put(endpointId, NumberUtil.nextRand(5, 15) * TimeUtil.second);
            }

            if (!tries.containsKey(endpointId)) {
                tries.put(endpointId, 0);
            }

            if (tries.get(endpointId) > maxTry) {
                //return true;
            }

            LogKit.verbose("Request Thread...." + endpointId + " " + isExpired(times.get(endpointId), delays.get(endpointId)));
            if (isExpired(times.get(endpointId), delays.get(endpointId))) {
                requestConnection(endpointId);
                times.remove(endpointId);
                delays.remove(endpointId);
                tries.put(endpointId, tries.get(endpointId) + 1);
            } else {
                pendingEndpoints.insertLastUniquely(endpointId);
            }
            waitRunner(2 * defaultWait);
            return true;
        }
    }

    //some public api
    boolean send(long peerId, Payload payload) {
        String acceptedEndpoint = getAcceptedEndpointId(peerId);
        if (acceptedEndpoint == null) {
            return false;
        }
        Nearby.Connections.sendPayload(google, acceptedEndpoint, payload);
        return true;
    }

    private String getAcceptedEndpointId(long peerId) {
        if (!endpoints.containsKey(peerId)) {
            return null;
        }

        String endpointId = endpoints.get(peerId);
        if (!states.containsKey(endpointId)) {
            return null;
        }

        if (states.get(endpointId) != State.ACCEPTED) {
            return null;
        }
        return endpointId;
    }

    private String toString(long value) {
        if (!cache.containsKey(value)) {
            cache.put(value, String.valueOf(value));
        }
        return cache.get(value);
    }

    private long toLong(String value) {
        if (!cache.containsValue(value)) {
            cache.put(Long.parseLong(value), value);
        }
        return cache.inverse().get(value);
    }
}
