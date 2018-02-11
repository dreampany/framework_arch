package com.dreampany.framework.data.manager;

import android.content.Context;
import android.view.View;

import com.dreampany.framework.data.enums.AdType;
import com.dreampany.framework.data.enums.PointSubtype;
import com.dreampany.framework.data.event.AdEvent;
import com.dreampany.framework.data.provider.pref.FramePref;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.TimeUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by nuc on 10/26/2016.
 */

public final class AdManager extends Manager {

    public static class Config {
        public long bannerExpireDelay;
        public long interstitialExpireDelay;
        public long rewardedExpireDelay;
    }

    private static final boolean OFF = false;

    private static AdManager manager;
    private Context context;

    private enum State {NONE, LOADED, STARTED, CLOSED, FAILED, LEFT, OPENED}

    private static final long defaultAdDelay = TimeUtil.secondToMilli(0);

    private AdView bannerAdView;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;

    private State bannerState = State.NONE;
    private State interstitialState = State.NONE;
    private State rewardedState = State.NONE;

    private static final int BANNER_MULTIPLIER = 1;
    private static final int INTERSTITIAL_MULTIPLIER = 2;
    private static final int REWARDED_MULTIPLIER = 4;
    private final Executor executor;


    private int points;

    private Config config;
    private FramePref pref;

    private AdManager(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.context = context.getApplicationContext();
        executor = Executors.newSingleThreadExecutor();
    }

    synchronized public static AdManager onInstance(Context context) {
        if (manager == null) {
            manager = new AdManager(context);
        }
        return manager;
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

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setPref(FramePref pref) {
        this.pref = pref;
    }

    public void initPoints(int points) {
        this.points = points;
    }

    public boolean isBannerLoaded() {
        return bannerState == State.LOADED;
    }

    public boolean isInterstitialLoaded() {
        return interstitialState == State.LOADED;
    }

    public boolean isRewardedLoaded() {
        return rewardedState == State.LOADED;
    }

    public void loadBanner(AdView adView) {
        if (OFF) {
            return;
        }

        if (adView == null) {
            return;
        }

        bannerAdView = adView;
        load(adView);
    }

    public void loadInterstitial(final int adUnitId) {

        if (OFF) {
            return;
        }

        if (!pref.isInterstitialTimeExpired(config.interstitialExpireDelay)) {
            return;
        }

        if (interstitialAd == null) {
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(context.getString(adUnitId));
        }

        load(interstitialAd);
    }

    public void loadRewarded(Context context, int adUnitId) {
        if (OFF) {
            return;
        }

        if (!pref.isRewardedTimeExpired(config.interstitialExpireDelay)) {
            return;
        }

        if (rewardedVideoAd == null) {
            rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
            rewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        }
        load(rewardedVideoAd, context.getString(adUnitId));
    }

    public void resumeRewarded(Context context) {
        if (rewardedVideoAd != null) {
            rewardedVideoAd.resume(context);
        }
    }

    public void pauseRewarded(Context context) {
        if (rewardedVideoAd != null) {
            rewardedVideoAd.pause(context);
        }
    }

    public void destroyRewarded(Context context) {
        if (rewardedVideoAd != null) {
            rewardedVideoAd.destroy(context);
        }
    }

    private void load(final AdView adView) {
        if (adView.getAdListener() == null) {
            adView.setAdListener(bannerListener);
        }
        adView.postDelayed(() -> {
            adView.loadAd(new AdRequest.Builder().build());
        }, defaultAdDelay);
    }

    private void load(final InterstitialAd interstitialAd) {
        if (interstitialAd.getAdListener() == null) {
            interstitialAd.setAdListener(interstitialListener);
        }
        AndroidUtil.post(() -> interstitialAd.loadAd(new AdRequest.Builder().build()), defaultAdDelay);
    }

    private void load(final RewardedVideoAd rewardedVideoAd, String unitId) {
        if (rewardedVideoAd.getRewardedVideoAdListener() == null) {
            rewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        }
        AndroidUtil.post(() -> rewardedVideoAd.loadAd(unitId, new AdRequest.Builder().build()), defaultAdDelay);
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

    private void earnPoints(String id, AdType type, PointSubtype subtype, int points, String comment) {
        FrameManager.onInstance(context).trackPoints(id, type.value(), subtype.value(), points, comment);

        AdEvent event = new AdEvent();
        event.points = points;
        event.setType(type);
        event.setSubtype(subtype);

        EventManager.post(event);
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

            earnPoints(
                    String.valueOf(DataUtil.getSha256()),
                    AdType.BANNER,
                    PointSubtype.ADD,
                    points * BANNER_MULTIPLIER,
                    "Banner points"
            );
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            bannerState = State.OPENED;
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            bannerState = State.LOADED;
            View view = (View) bannerAdView.getParent();
            view.setVisibility(View.VISIBLE);
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
            //lastInterstitialAdTime = TimeUtil.currentTime();
/*            earnPoints(
                    String.valueOf(DataUtil.getSha256()),
                    AdType.INTERSTITIAL,
                    PointSubtype.ADD,
                    points * INTERSTITIAL_MULTIPLIER,
                    "Interstitial points"
            );*/
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            interstitialState = State.OPENED;
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            interstitialState = State.LOADED;
            interstitialAd.show();
            pref.setInterstitialTime(TimeUtil.currentTime());
        }
    };

    private final RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLoaded() {
            rewardedState = State.LOADED;
            rewardedVideoAd.show();
            pref.setRewardedTime(TimeUtil.currentTime());
        }

        @Override
        public void onRewardedVideoAdOpened() {
            rewardedState = State.OPENED;
        }

        @Override
        public void onRewardedVideoStarted() {
            rewardedState = State.STARTED;
        }

        @Override
        public void onRewardedVideoAdClosed() {
            rewardedState = State.CLOSED;

        }

        @Override
        public void onRewarded(RewardItem rewardItem) {
/*            earnPoints(
                    String.valueOf(DataUtil.getSha256()),
                    AdType.REWARDED,
                    PointSubtype.ADD,
                    points * REWARDED_MULTIPLIER,
                    "Rewarded points"
            );*/
        }

        @Override
        public void onRewardedVideoAdLeftApplication() {
            rewardedState = State.LEFT;

        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int errorCode) {
            rewardedState = State.FAILED;

        }
    };
}
