package com.dreampany.framework.data.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by air on 5/3/17.
 */

public class TimerService extends Service {

    private Messenger messenger;
    private CountDownTimer timer;
    private long currentMillis;
    private int signature;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        messenger = intent.getParcelableExtra("messenger");
        signature = intent.getIntExtra("signature", 0);

        if (timer != null) {
            timer.cancel();
        } else {
            currentMillis = intent.getLongExtra("millis", 0);
        }
        timer = new CountDownTimer(currentMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentMillis = millisUntilFinished;
                postTime(currentMillis);
            }

            @Override
            public void onFinish() {
                postTime(0);
                timer = null;
            }
        }.start();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }


    private void postTime(long timeInMillis) {
        Message m = Message.obtain();
        m.what = signature;
        m.obj = timeInMillis;
        try {
            messenger.send(m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
