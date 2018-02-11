package com.dreampany.framework.data.api.network.nearby;

import android.content.Context;

import com.dreampany.framework.data.structure.SmartQueue;
import com.dreampany.framework.data.thread.Runner;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.LogKit;
import com.dreampany.framework.data.util.TimeUtil;
import com.dreampany.framework.data.api.network.data.model.Id;
import com.dreampany.framework.data.api.network.data.model.Peer;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by nuc on 8/6/2017.
 */

class Api implements Connection.Callback {

    volatile boolean inited;
    Executor executor;
    NearbyApi.Processor processor;
    Set<NearbyApi.Callback> callbacks;
    Context context;
    long serviceId;
    Peer peer;

    volatile Connection connection;
    Map<Long, Peer> peers; // peerId to Peer
    Map<Long, Peer.State> states; // peerId to Boolean
    Map<Long, Long> liveTimes;
    Map<Long, Boolean> synced; // peerId to Boolean

    Map<String, Long> endpoints; // endpoint to peerId
    Map<Long, Payload> payloads; // keep only file and stream payload;
    Map<Id, Long> payloadIds;    // keep only file and stream payload;

    Map<Id, MutablePair<Long, Long>> timeouts; // key to (timeout, startingOfTransfer)

    SmartQueue<Long> syncingPeers;
    SmartQueue<MutablePair<Long, Payload>> outputs; // peerId and payload # peerId to resolve endpoint dynamic way
    SmartQueue<MutablePair<Long, Payload>> inputs; // endpoint and payload # endpoint to resolve peerId dynamic way

    Runner syncingThread, outputThread, inputThread, downloadThread;

    Api() {
        executor = Executors.newCachedThreadPool();
        callbacks = Collections.synchronizedSet(new HashSet<NearbyApi.Callback>());

        peers = Maps.newConcurrentMap();
        states = Maps.newConcurrentMap();
        liveTimes = Maps.newConcurrentMap();
        synced = Maps.newConcurrentMap();

        endpoints = Maps.newConcurrentMap();
        payloads = Maps.newConcurrentMap();
        payloadIds = Maps.newConcurrentMap();

        syncingPeers = new SmartQueue<>();
        outputs = new SmartQueue<>();
        inputs = new SmartQueue<>();

        timeouts = Maps.newConcurrentMap();

    }

    void start() {
        if (!inited) {
            return;
        }
        connection = new Connection(this.context, serviceId, peer.id, this);
        connection.start();
    }

    void stop() {
        if (!inited) {
            return;
        }
        stopInputThread();
        stopOutputThread();
        stopSyncingThread();
        connection.stop();
    }

    @Override
    public void onConnection(long peerId, boolean connected) {

        if (connected) {
            LogKit.verbose("Peer " + peerId + " connected");
        } else {
            LogKit.verbose("Peer " + peerId + " disconnected");
        }

        if (!peers.containsKey(peerId)) {
            Peer peer = new Peer();
            peer.id = peerId;
            peers.put(peerId, peer);
        }
        states.put(peerId, connected ? Peer.State.LIVE : Peer.State.DEAD);
        if (connected) {
            syncingPeers.insertLastUniquely(peerId);
            startSyncingThread();
        } else {
            syncingPeers.remove(peerId);
        }
    }

    @Override
    public void onPayload(long peerId, Payload payload) {
        LogKit.verbose("Payload Received from: " + peerId);
        resolvePayload(peerId, payload);
    }

    @Override
    public void onPayloadStatus(long peerId, PayloadTransferUpdate status) {

    }


    /*private final EndpointDiscoveryCallback discoveryCallback = new EndpointDiscoveryCallback() {

        @Override
        public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
            long peerId = Long.parseLong(info.getEndpointName());
            LogKit.verbose("endpoint found " + endpointId + " id " + peerId);

            endpoints.put(endpointId, peerId);
            resolvePeer(peerId, endpointId, PeerState.FOUND);
        }

        @Override
        public void onEndpointLost(String endpointId) {
            LogKit.verbose("endpoint lost " + endpointId);
            long peerId = endpoints.get(endpointId);

            peers.get(peerId).setRight(PeerState.LOST);
            Peer peer = peers.get(peerId).getLeft();
            peerCallback(peer, Peer.State.DEAD);
        }
    };*/

/*    private final ConnectionLifecycleCallback connectionCallback = new ConnectionLifecycleCallback() {

        @Override
        public void onConnectionInitiated(String endpointId, ConnectionInfo info) {
            LogKit.verbose("Connected initiated...endpoint: " + endpointId + " id: " + info.getEndpointName());

            if (!endpoints.containsKey(endpointId)) {
                long peerId = Long.parseLong(info.getEndpointName());
                endpoints.put(endpointId, peerId);
            }
            long peerId = endpoints.get(endpointId);
            resolvePeer(peerId, endpointId, PeerState.INITIATED);
            if (apiState == ApiState.DISCOVERED) {
                Nearby.Connections.acceptConnection(google, endpointId, payloadCallback);
            } else {
                Nearby.Connections.rejectConnection(google, endpointId);
            }
        }

        @Override
        public void onConnectionResult(String endpointId, ConnectionResolution resolution) {
            *//*LogKit.verbose("Connection Result endpoint: " + endpointId + " accepted " + accepted);
            long peerId = endpoints.get(endpointId);
            resolvePeer(peerId, endpointId, (accepted ? PeerState.ACCEPTED : PeerState.REJECTED));*//*
        }

        @Override
        public void onDisconnected(String endpointId) {
            LogKit.verbose("Disconnected endpoint: " + endpointId);
            long peerId = endpoints.get(endpointId);
            peers.get(peerId).setRight(PeerState.DISCONNECTED);
            PeerState state = peers.get(peerId).getRight();
            Peer peer = peers.get(peerId).getLeft();
            peerCallback(peer, Peer.State.DEAD);
        }
    };*/

/*    private final PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String endpointId, Payload payload) {
            LogKit.verbose("Payload Received from: " + endpointId);
            resolvePayload(endpointId, payload);
        }

        @Override
        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
            //LogKit.verbose("onPayloadTransferUpdate " + endpointId);
            long payloadId = update.getPayloadId();
            MutablePair<MutableTriple<Long, Long, Payload>, Long> out = null;
            *//*if (outgoing.containsKey(payloadId)) {
                out = outgoing.get(payloadId);
            }

            switch (update.getStatus()) {

                case PayloadTransferUpdate.Status.IN_PROGRESS:
                    if (out != null) {
                        statusCallback(payloadId, NearbyApi.PayloadState.PROGRESS, update.getTotalBytes(), update.getBytesTransferred());
                    }
                    break;

                case PayloadTransferUpdate.Status.SUCCESS:
                    if (out != null) {
                        outgoing.remove(payloadId);
                        statusCallback(payloadId, NearbyApi.PayloadState.SUCCESS, update.getTotalBytes(), update.getBytesTransferred());
                    }

                    break;
                case PayloadTransferUpdate.Status.FAILURE:
                    if (out != null) {
                        outgoing.remove(payloadId);
                        statusCallback(payloadId, NearbyApi.PayloadState.FAILURE, update.getTotalBytes(), update.getBytesTransferred());
                    }
                    break;
            }*//*

        }
    };*/

    void sendPacket(Id id, byte[] packet, long timeout) {
        Payload payload = Payload.fromBytes(packet);
        sendPayload(id, payload);
        resolveTimeout(id, timeout, 0L);
    }

    void sendFile(Id id, File file, long timeout) {
        try {
            Payload payload = Payload.fromFile(file);
            byte[] filePayloadId = Packets.getFilePayloadIdPacket(id.id, payload.getId());
            sendPacket(id, filePayloadId, 0L);
            sendPayload(id, payload);
            resolveTimeout(id, timeout, 0L);
        } catch (FileNotFoundException e) {
            LogKit.verbose("error: " + e.toString());
        }
    }

    void sendPayload(Id id, Payload payload) {
        // payloads.put(payload.getId(), payload);
        // payloadIds.put(id, payload.getId());
        outputs.add(MutablePair.of(id.target, payload));
        startOutputThread();
    }

    void resolveTimeout(Id id, long timeout, long starting) {
        if (!timeouts.containsKey(id)) {
            timeouts.put(id, MutablePair.of(0L, 0L));
        }
        timeouts.get(id).setLeft(timeout);
        timeouts.get(id).setRight(starting);
    }

    void resolvePayload(long peerId, Payload payload) {
        inputs.add(MutablePair.of(peerId, payload));
        startInputThread();
    }

    private void resolvePacket(long peerId, byte[] packet) {

        LogKit.verbose("received " + packet.length);

        if (Packets.isPeer(packet)) {
            LogKit.verbose("received peer packet " + packet.length);
            resolvePeerPacket(peerId, packet);
            return;
        }

        if (Packets.isData(packet)) {
            byte[] bytes = DataUtil.copy(packet, 1);
            resolveDataPacket(peerId, bytes);
            return;
        }

        if (Packets.isFile(packet)) {
            // byte[] filePacket = DataUtil.copy(packet, 1);
            resolveFilePacket(peerId, packet);
            return;
        }
    }

    private void resolvePeerPacket(long peerId, byte[] packet) {
        if (Packets.isMeta(packet)) {

            LogKit.verbose("received meta packet " + packet.length);
            ByteBuffer meta = DataUtil.copyToBuffer(packet, 2);
            long peerHash = meta.getLong();

            long ownHash = DataUtil.getSha256(this.peer.data);

            LogKit.verbose("remote end own hash " + peerHash + " own hash " + ownHash);

            if (peerHash == ownHash) {
                byte[] okay = Packets.getPeerOkayPacket();
                Id id = new Id();
                id.source = peer.id;
                id.target = peerId;
                sendPacket(id, okay, 0L);
            } else {
                byte[] peerData = Packets.getPeerDataPacket(this.peer.data);
                LogKit.verbose("send peer data: " + peerData.length);
                Id id = new Id();
                id.source = peer.id;
                id.target = peerId;
                sendPacket(id, peerData, 0L);
            }

            return;
        }

        if (Packets.isOkay(packet)) {
            LogKit.verbose("received okay packet " + packet.length);
            Peer peer = peers.get(peerId);
            peerCallback(peer, Peer.State.LIVE);
            return;
        }

        if (Packets.isPeerData(packet)) {
            LogKit.verbose("received data packet " + packet.length);
            ByteBuffer buf = DataUtil.copyToBuffer(packet, 2);
            Peer peer = peers.get(peerId);
            peer.data = buf.array();

            LogKit.verbose("received peer data: " + peer.data.length);

            peers.get(peerId);
            peerCallback(peer, Peer.State.LIVE);
            return;
        }
    }

    private void resolveDataPacket(long peerId, byte[] data) {
        Peer peer = peers.get(peerId);
        peerCallback(peer, data);
    }

    private void resolveFilePacket(long peerId, byte[] packet) {
       /* if (Packets.isId(packet)) {
            ByteBuffer idBuffer = DataUtil.copyToBuffer(packet, 2);
            long fileId = idBuffer.getLong();
            long source = idBuffer.getLong();
            Id id = new Id(fileId, source);
           // keepDownloads(peerId, fileId);
            return;
        }*/

        /*if (Packets.isMeta(packet)) {
            ByteBuffer metaBuffer = DataUtil.copyToBuffer(packet, 2);
            long fileId = metaBuffer.getLong();
            byte[] meta = new byte[metaBuffer.remaining()];
            metaBuffer.get(meta);

           // keepDownloads(peerId, fileId, meta);
            return;
        }*/

/*        if (Packets.isFilePayloadId(packet)) {
            ByteBuffer metaBuffer = DataUtil.copyToBuffer(packet, 2);
            long fileId = metaBuffer.getLong();
            long payloadId = metaBuffer.getLong();
           // downloadPayloadIds.put(fileId, payloadId);
            return;
        }*/

/*        if (Packets.isMetaRequest(packet)) {
            ByteBuffer metaBuffer = DataUtil.copyToBuffer(packet, 1);
            long fileId = metaBuffer.getLong();

            Meta meta = processor.getMeta(fileId);
            byte[] metaPacket = Packets.getFileMetaPacket(fileId, meta);
            sendPacket(peerId, 0L, metaPacket);
            return;
        }*/

        /*if (Packets.isDataRequest(packet)) {
            ByteBuffer dataBuffer = DataUtil.copyToBuffer(packet, 1);
            long fileId = dataBuffer.getLong();
            File file = processor.getFile(fileId);

            try {
                Payload payload = Payload.fromFile(file);
                byte[] filePayloadId = Packets.getFilePayloadIdPacket(fileId, payload.getId());

                sendPacket(peerId, 0L, filePayloadId);
                sendPayload(peerId, 0L, payload);
            } catch (FileNotFoundException e) {

            }
            return;
        }*/
    }

    private void peerCallback(Peer peer, Peer.State state) {
        for (NearbyApi.Callback callback : callbacks) {
            callback.onPeer(peer, state);
        }
    }

    private void peerCallback(Peer peer, byte[] data) {
        for (NearbyApi.Callback callback : callbacks) {
            callback.onData(peer, data);
        }
    }

    private void statusCallback(long payloadId, NearbyApi.PayloadState status, long totalBytes, long bytesTransferred) {
        for (NearbyApi.Callback callback : callbacks) {
            callback.onStatus(payloadId, status, totalBytes, bytesTransferred);
        }
    }

    //thread management

    void startOutputThread() {
        if (outputThread == null || !outputThread.isRunning()) {
            outputThread = new OutputThread();
            outputThread.start();
        }
        outputThread.notifyRunner();
    }

    void stopOutputThread() {
        if (outputThread != null) {
            outputThread.stop();
        }
    }

    void startInputThread() {
        if (inputThread == null || !inputThread.isRunning()) {
            inputThread = new InputThread();
            inputThread.start();
        }
        inputThread.notifyRunner();
    }

    void stopInputThread() {
        if (inputThread != null) {
            inputThread.stop();
        }
    }

    void startDownloadThread() {
        if (downloadThread == null || !downloadThread.isRunning()) {
            downloadThread = new DownloadThread();
            downloadThread.start();
        }
        downloadThread.notifyRunner();
    }

    private void startSyncingThread() {
        if (syncingThread == null || !syncingThread.isRunning()) {
            syncingThread = new SyncingThread();
            syncingThread.start();
        }
        syncingThread.notifyRunner();
    }

    private void stopSyncingThread() {
        if (syncingThread != null) {
            syncingThread.stop();
        }
    }

    private class SyncingThread extends Runner {

        long delay;
        Map<Long, Long> times; //peerId to time

        SyncingThread() {
            times = Maps.newHashMap();
            delay = 20 * TimeUtil.second;
        }

        @Override
        protected boolean looping() throws InterruptedException {

            Long peerId = syncingPeers.pollFirst();

            if (peerId == null) {
                waitRunner(wait);
                wait += defaultWait;
                return true;
            }
            wait = defaultWait;

            if (synced.containsKey(peerId) && synced.get(peerId)) {
                return true;
            }

            if (!times.containsKey(peerId)) {
                times.put(peerId, 0L);
            }

            if (isExpired(times.get(peerId), pureWait)) {
                Peer peer = peers.get(peerId);
                LogKit.verbose("next syncing peer " + peer.id);

                long metaHash = DataUtil.getSha256(peer.data);
                byte[] metaPacket = Packets.getPeerMetaPacket(metaHash);

                Id id = new Id();
                id.source = Api.this.peer.id;
                id.target = peer.id;
                sendPacket(id, metaPacket, 0L);
                times.put(peerId, TimeUtil.currentTime());
            }

            waitRunner(periodWait);
            return true;
        }
    }

    private class OutputThread extends Runner {

        OutputThread() {
        }

        @Override
        protected boolean looping() throws InterruptedException {

            MutablePair<Long, Payload> output = outputs.pollFirst();

            if (output == null) {
                waitRunner(wait);
                wait += defaultWait;
                return true;
            }

            wait = defaultWait;

            long peerId = output.getLeft();
            Payload payload = output.getRight();
            boolean sent = connection.send(peerId, payload);

            //MutablePair<MutableTriple<Long, Long, Payload>, Long> pendingItem = MutablePair.of(output, System.currentTimeMillis());
            //   outgoing.put(payload.getId(), pendingItem);

            waitRunner(periodWait);
            return true;
        }
    }

    private class InputThread extends Runner {

        InputThread() {
        }

        @Override
        protected boolean looping() throws InterruptedException {

            MutablePair<Long, Payload> input = inputs.takeFirst();

            if (input == null) {
                waitRunner(wait);
                wait += defaultWait;
                return true;
            }
            wait = defaultWait;

            long peerId = input.getLeft();
            Payload payload = input.getRight();

            //TODO process the input payload

            switch (payload.getType()) {
                case Payload.Type.BYTES:
                    resolvePacket(peerId, payload.asBytes());
                    break;
            }
            waitRunner(periodWait);
            return true;
        }
    }

    private class DownloadThread extends Runner {

        long downloadDelay = 60 * TimeUtil.second;

        DownloadThread() {
        }

        Map.Entry<Long, MutableTriple<Long, byte[], File>> nextDownload() {
/*            for (Map.Entry<Long, MutableTriple<Long, byte[], File>> entry : downloads.entrySet()) {
                long fileId = entry.getKey();
                if (isExpired(fileId, downloadDelay)) {
                    return entry;
                }
            }*/
            return null;
        }


        @Override
        protected boolean looping() throws InterruptedException {

            Map.Entry<Long, MutableTriple<Long, byte[], File>> item = nextDownload();

            if (item == null) {
                waitRunner(wait);
                wait += defaultWait;
                return true;
            }

            wait = defaultWait;

            long fileId = item.getKey();
            long peerId = item.getValue().getLeft();
            byte[] fileMeta = item.getValue().getMiddle();
            File file = item.getValue().getRight();

            /*if (fileMeta == null || fileMeta.length == 0) {
                byte[] metaRequest = Packets.getFileMetaRequestPacket(fileId);
                sendPacket(peerId, 0L, metaRequest);
            } else if (file == null) {
                byte[] fileRequest = Packets.getFileDataRequestPacket(fileId);
                sendPacket(peerId, 0L, fileRequest);
            }*/

            waitRunner(periodWait);
            return false;
        }
    }
}
