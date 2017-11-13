package com.dreampany.framework.data.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Pair;

import com.dreampany.framework.data.listener.TimerListener;
import com.dreampany.framework.data.service.TimerService;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by air on 5/4/17.
 */

public final class TimerManager {

    private static TimerManager manager;
    private Map<Integer, Pair<TimerListener, Intent>> clients;
    private Map<Integer, Long> currentMillis;
    private Map<Integer, Long> beginTimes;
    private Map<Integer, Boolean> downs;
    private Map<Integer, Boolean> timerRunning;
    private Messenger messenger;

    private TimerManager() {
        clients = Maps.newConcurrentMap();
        currentMillis = Maps.newConcurrentMap();
        beginTimes = Maps.newConcurrentMap();
        downs = Maps.newConcurrentMap();
        timerRunning = Maps.newConcurrentMap();
        messenger = new Messenger(handler);
    }

    public static synchronized TimerManager onManager() {
        if (manager == null) {
            manager = new TimerManager();
        }
        return manager;
    }

    public void register(Context context, int signature, TimerListener listener) {
        Context appContext = context.getApplicationContext();
        Intent timerIntent = new Intent(appContext, TimerService.class);
        clients.put(signature, Pair.create(listener, timerIntent));
    }

    public void unregister(int signature) {
        clients.remove(signature);
    }

    public void setInitialTime(int signature, long millisInFuture) {
        beginTimes.put(signature, millisInFuture);
        setCurrentTime(signature, millisInFuture);
    }

    public void setCurrentTime(int signature, long millisInFuture) {
        currentMillis.put(signature, millisInFuture);
        clients.get(signature).first.onTimerTick(millisInFuture);
    }

    public void start(Context context, int signature) {
        Context appContext = context.getApplicationContext();
        Pair<TimerListener, Intent> client = clients.get(signature);
        if (client != null) {
            timerRunning.put(signature, true);
            Intent timerIntent = client.second;
            timerIntent.putExtra("messenger", messenger);
            timerIntent.putExtra("millis", currentMillis.get(signature));
            timerIntent.putExtra("signature", signature);
            appContext.startService(timerIntent);
        }

    }

    public void reset(Context context, int signature) {
        stop(context, signature);
        currentMillis.put(signature, beginTimes.get(signature));
        // updateUI(currentMillis);
    }

    public void stop(Context context, int signature) {
        Pair<TimerListener, Intent> client = clients.get(signature);
        if (client != null) {
            Context appContext = context.getApplicationContext();
            timerRunning.put(signature, false);
            appContext.stopService(client.second);
        }
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int signature = msg.what;
            Pair<TimerListener, Intent> client = clients.get(signature);
            if (client != null) {
                long currentMillis = (Long) msg.obj;

                TimerListener listener = client.first;

                if (currentMillis == 0) {
                    listener.onTimerElapsed();
                    timerRunning.put(signature, true);
                }
                if (listener != null) {
                    listener.onTimerTick(currentMillis);
                }
            }


        }
    };

}
