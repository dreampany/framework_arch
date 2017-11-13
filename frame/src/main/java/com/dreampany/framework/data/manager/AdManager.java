package com.dreampany.framework.data.manager;

import android.content.Context;
import android.view.View;

import com.dreampany.framework.data.enums.AdType;
import com.dreampany.framework.data.util.NetworkUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.dreampany.framework.data.event.NetworkEvent;
import com.dreampany.framework.data.model.AdEvent;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.TimeUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by nuc on 10/26/2016.
 */

public final class AdManager extends Manager {

    private static AdManager manager;
    private Context context;

    private enum State {NONE, LOADED, CLOSED, FAILED, LEFT, OPENED}

    private static final long defaultAdDelay = 1500L;
    private static final long interstitialAdPeriod = TimeUtil.minuteToMilli(5);
    private long lastInterstitialAdTime;

    private AdView bannerAdView;
    private InterstitialAd interstitialAd;

    private State bannerState = State.NONE;
    private State interstitialState = State.NONE;

    private final AdType bannerType = AdType.BANNER;

    private long bannerPoints = 100L;
    private long interstitialPoints = 500L;
    private Executor executor;

    private AdManager(Context context) {
        this.context = context.getApplicationContext();
        lastInterstitialAdTime = TimeUtil.currentTime();
        executor = Executors.newSingleThreadExecutor();
    }

    synchronized public static AdManager onInstance(Context context) {
        if (manager == null) {
            manager = new AdManager(context);
        }
        return manager;
    }

    public static AdManager onInstance() {
        return onInstance(null);
    }

    @Override
    public void start() {
        super.start();
        //NetworkManager.onInstance(context).register(this);
    }

    @Override
    public void stop() {
        super.stop();
        //NetworkManager.onInstance(context).unregister(this);
    }

    @Override
    protected boolean looping() throws InterruptedException {
        return false;
    }

/*    @Override
    public void onInternet(boolean internet) {
        if (internet) {
            if (!isBannerLoaded()) {
                AndroidUtil.post(() -> loadBanner(bannerAdView));
            }
        }
    }

    @Override
    public void onNetworkEvent(NetworkEvent event) {

    }*/

    public boolean isBannerLoaded() {
        return bannerState == State.LOADED;
    }

    public boolean isInterstitialLoaded() {
        return interstitialState == State.LOADED;
    }

    public void loadBanner(AdView adView) {
        if (adView == null) {
            return;
        }

        bannerAdView = adView;
        if (AndroidUtil.isDebug()) {
            return;
        }
        executor.execute(() -> {
            if (NetworkUtil.hasInternet()) {
                load(adView);
            }
        });
    }

    public void closeBanner() {
        if (bannerAdView != null && bannerAdView.isShown()) {
            View view = (View) bannerAdView.getParent();
            view.setVisibility(View.GONE);
            bannerAdView.destroy();
        }
    }

    public void loadBannerTest(AdView adView) {
        if (adView == null) {
            return;
        }

        bannerAdView = adView;
        loadTest(adView);
    }

    public void loadInterstitial(final int adUnitId) {

        if (interstitialAd == null) {
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(context.getString(adUnitId));
        }

        if (!expiredInterstitialTime()) {
            return;
        }

        if (!AndroidUtil.isDebug()) {
            load(interstitialAd);
        }
    }

    private void load(final AdView adView) {
        if (adView.getAdListener() == null) {
            adView.setAdListener(bannerListener);
        }
        adView.postDelayed(() -> {
            View view = (View) adView.getParent();
            view.setVisibility(View.VISIBLE);
            adView.loadAd(new AdRequest.Builder().build());
        }, defaultAdDelay);
    }

    private void loadTest(final AdView adView) {
        if (adView.getAdListener() == null) {
            adView.setAdListener(bannerListener);
        }
        //if (NetworkManager.onInstance(context).hasInternet()) {
            adView.postDelayed(() -> {
                View view = (View) adView.getParent();
                view.setVisibility(View.VISIBLE);
                adView.loadAd(new AdRequest.Builder().addTestDevice("33BE2250B43518CCDA7DE426D04EE232")
                        .build());
            }, defaultAdDelay);
       // }
    }

    private void load(final InterstitialAd interstitialAd) {
        if (interstitialAd.getAdListener() == null) {
            interstitialAd.setAdListener(interstitialListener);
        }
/*        if (NetworkManager.onInstance(context).hasInternet()) {
            AndroidUtil.post(() -> interstitialAd.loadAd(new AdRequest.Builder().build()), defaultAdDelay);

        }*/
    }

    private boolean expiredInterstitialTime() {
        long adPeriod = TimeUtil.currentTime() - lastInterstitialAdTime;
        return adPeriod > interstitialAdPeriod;
    }

    private final AdListener bannerListener = new AdListener() {

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            bannerState = State.CLOSED;
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            bannerState = State.LOADED;
        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
            bannerState = State.LEFT;
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            bannerState = State.OPENED;
            FrameManager.onInstance(context).trackPoints(
                    String.valueOf(DataUtil.getSha256()),
                    bannerType.value(),
                    bannerPoints,
                    "Opening the banner ad");

            AdEvent event = new AdEvent();
            event.points = bannerPoints;
            event.setType(bannerType);

            EventManager.post(event);
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            bannerState = State.LOADED;
        }
    };

    private final AdListener interstitialListener = new AdListener() {
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            interstitialState = State.CLOSED;
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            interstitialState = State.LOADED;
        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
            interstitialState = State.LEFT;
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            interstitialState = State.OPENED;
            //PointManager.onInstance().trackPoints(context, DataUtil.getSha256(), interstitialPoints, pointType);
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            interstitialState = State.LOADED;

            interstitialAd.show();
            lastInterstitialAdTime = TimeUtil.currentTime();
        }
    };
}
