package com.dreampany.framework.data.api.network.data.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dreampany.framework.data.api.network.data.model.NetworkEvent;
import com.dreampany.framework.data.manager.EventManager;
import com.dreampany.framework.data.util.NetworkUtil;
import com.dreampany.framework.data.util.RxUtil;
import com.dreampany.framework.data.util.TimeUtil;
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by air on 11/6/17.
 */

public final class NetworkManager {

    private static NetworkManager instance;
    private volatile boolean internetChecking;
    private int internet;
    private final NetworkEvent internetEvent = new NetworkEvent();


    private final int initialIntervalInMs = (int) TimeUtil.secondToMilli(5);
    private final int interval = (int) TimeUtil.secondToMilli(30);
    private final String host = "http://clients3.google.com/generate_204";
    private final int port = 80;
    private final int timeout = 5000;

    private Disposable networkDisposable;
    private volatile static boolean hasInternet;

    public NetworkManager() {

    }

    public boolean hasInternet() {
        return hasInternet;
    }

    public void networkChecker(Context context) {
        if (networkDisposable != null) {
            return;
        }

        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean isConnectedToTheInternet) throws Exception {
                        hasInternet = isConnectedToTheInternet;
                    }
                });

        networkDisposable = ReactiveNetwork
                .observeNetworkConnectivity(context.getApplicationContext())
                .subscribeOn(Schedulers.io())
                .filter(ConnectivityPredicate.hasState(NetworkInfo.State.CONNECTED, NetworkInfo.State.DISCONNECTED))
                .filter(ConnectivityPredicate.hasType(ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE))
                .observeOn(Schedulers.computation())
                .subscribe(this::checkConnectivity);
    }

    public void stopChecker() {
        if (networkDisposable != null) {
            RxUtil.safelyDispose(networkDisposable);
        }
    }

    private void checkConnectivity(Connectivity connectivity) {
        NetworkInfo.State state = connectivity.getState();
        if (state == NetworkInfo.State.CONNECTED) {
            hasInternet = NetworkUtil.hasInternet();
        } else {
            hasInternet = false;
        }
        internetEvent.setInternet(hasInternet);
        EventManager.post(internetEvent);
    }
}
