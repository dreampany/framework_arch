package com.dreampany.framework.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Looper;

import com.dreampany.framework.data.enums.NetworkStatus;
import com.dreampany.framework.data.event.ConnectivityChanged;
import com.dreampany.framework.data.event.NetworkEvent;
import com.dreampany.framework.data.util.AndroidUtil;

public abstract class BaseReceiver extends BroadcastReceiver {
    private final Context context;

    public BaseReceiver(Context context) {
        this.context = context;
    }

/*    protected boolean hasChanged(NetworkStatus networkStatus) {
        return NetworkManager.networkStatus != networkStatus;
    }*/

    protected void postStatus(NetworkStatus networkStatus) {
        //NetworkManager.networkStatus = networkStatus;
        post(new ConnectivityChanged(context, networkStatus));
    }

    protected void post(final NetworkEvent event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
           // callback.onNetworkEvent(event);
        } else {
            AndroidUtil.post(new Runnable() {
                @Override
                public void run() {
                    //callback.onNetworkEvent(event);
                }
            });
        }
    }
}