package com.dreampany.framework.data.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dreampany.framework.data.enums.NetworkStatus;
import com.dreampany.framework.data.manager.InternetManager;
import com.dreampany.framework.data.util.LogKit;

/**
 * Created by air on 4/21/17.
 */

public class NetworkReceiver extends BaseReceiver {

    private boolean internetCheckEnabled;
    private final InternetManager internetManager;
    private boolean hasConnection;

    public NetworkReceiver(Context context, InternetManager internetManager) {
        super(context);
        this.internetManager = internetManager;
    }

    public void enableInternetCheck() {
        this.internetCheckEnabled = true;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        onPostReceive(getStatus(context));
    }

    public void onPostReceive(final NetworkStatus networkStatus) {

        LogKit.logInfo(getClass().getSimpleName(), networkStatus.toString());

/*        if (!hasChanged(networkStatus)) {
            return;
        }*/

        boolean isConnectedToWifi = networkStatus == NetworkStatus.WIFI_CONNECTED;

        if (internetCheckEnabled && isConnectedToWifi) {
            internetManager.check();
        } else {
            postStatus(networkStatus);
        }
    }

    public boolean hasConnection() {
        return hasConnection;
    }

    private NetworkStatus getStatus(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                hasConnection = true;
                return NetworkStatus.WIFI_CONNECTED;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                hasConnection = true;
                return NetworkStatus.MOBILE_CONNECTED;
            }
        }

        hasConnection = false;

        return NetworkStatus.OFFLINE;
    }
}
