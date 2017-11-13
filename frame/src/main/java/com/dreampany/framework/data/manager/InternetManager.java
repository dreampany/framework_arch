package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.util.NetworkUtil;

import java.io.IOException;
import java.util.concurrent.Executor;

public class InternetManager {

    public static final String DEFAULT_PING_HOST = "www.google.com";
    public static final int DEFAULT_PING_PORT = 80;
    public static final int DEFAULT_PING_TIMEOUT_IN_MS = 2000;

    private String pingHost;
    private int pingPort;
    private int pingTimeout;
    private boolean hasInternet;
    private Executor executor;

    public InternetManager(Executor executor) {
        this.executor = executor;
        this.pingHost = DEFAULT_PING_HOST;
        this.pingPort = DEFAULT_PING_PORT;
        this.pingTimeout = DEFAULT_PING_TIMEOUT_IN_MS;
    }

    public void check() {
        executor.execute(() -> {
            try {
                hasInternet = NetworkUtil.hasInternet(pingHost, pingPort, pingTimeout);
            } catch (IOException e) {
                hasInternet = false;
            } finally {
                sendBroadcast(hasInternet);
            }
        });
    }

    public void setPingParameters(String host, int port, int timeoutInMs) {
        this.pingHost = host;
        this.pingPort = port;
        this.pingTimeout = timeoutInMs;
    }

    public boolean hasInternet() {
        return hasInternet;
    }

    private void sendBroadcast(boolean hasInternet) {
/*        Intent intent = new Intent(InternetReceiver.INTENT);
        intent.putExtra(InternetReceiver.INTENT_EXTRA, hasInternet);
        context.sendBroadcast(intent);*/
    }
}