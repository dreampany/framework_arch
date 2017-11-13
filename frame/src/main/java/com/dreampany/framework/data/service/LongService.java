package com.dreampany.framework.data.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;

import com.dreampany.framework.data.util.LogKit;


/**
 * Created by nuc on 2/21/2017.
 */

public abstract class LongService extends Service {

    private Messenger messenger;
    private Messenger replyTo;

    @Override
    public void onCreate() {
        super.onCreate();
        LogKit.verbose("LongService.onCreate()");

        messenger = new Messenger(LongService.this.new ServiceHandler());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        LogKit.verbose("LongService.onStartCommand()");
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogKit.verbose("LongService.onDestroy()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    protected void handleServiceMessage(int what, Messenger replyTo, Bundle data) {
    }

    protected void setReplyTo(Messenger replyTo) {
        this.replyTo = replyTo;
    }

    private final class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            handleServiceMessage(msg.what, msg.replyTo, msg.getData());
        }
    }
}