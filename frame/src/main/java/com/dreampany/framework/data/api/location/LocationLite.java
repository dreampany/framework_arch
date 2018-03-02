package com.dreampany.framework.data.api.location;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;

import com.dreampany.framework.data.enums.MissingType;
import com.dreampany.framework.data.enums.PermissionType;
import com.dreampany.framework.data.enums.Result;
import com.dreampany.framework.data.event.LocationEvent;
import com.dreampany.framework.data.event.MissingEvent;
import com.dreampany.framework.data.event.PermissionEvent;
import com.dreampany.framework.data.manager.EventManager;
import com.dreampany.framework.data.util.TimeUtil;
import com.github.florent37.rxgps.RxGps;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by air on 10/13/17.
 */

public class LocationLite /*implements LocationListener, GeocodingProvider.Callback*/ {

    public interface Callback {
        void onLocation(long locationId, Location location);

        void onAddress(long locationId, Location location, Address address);
    }

    public static final int REQUEST_CHECK_SETTINGS = 20001;
    private static final long INTERVAL_DEFAULT = TimeUtil.minuteToMilli(1);
    private static final long DISTANCE_DEFAULT = 2;

    private static LocationLite instance;
    private final Context context;
    private static Location location;
    private long lastTime;

    private RxGps gps;
    private CompositeDisposable compositeDisposable;


    private LocationLite(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.context = context.getApplicationContext();
        compositeDisposable = new CompositeDisposable();
    }

    synchronized public static LocationLite onInstance(Context context) {
        if (instance == null) {
            instance = new LocationLite(context);
        }
        return instance;
    }

    public void closeAll() {
        compositeDisposable.clear();
    }

    public void addDisposable(Disposable disposable) {
        this.compositeDisposable.add(disposable);
    }

    public void onLocation(Location location) {
        if (location == null) {
            return;
        }
        if (LocationLite.location != null && !TimeUtil.isExpired(lastTime, INTERVAL_DEFAULT)) {
            return;
        }
        lastTime = TimeUtil.currentTime();
        LocationLite.location = location;
        LocationEvent event = new LocationEvent(location);
        EventManager.post(event);
    }

    public void onPermission(RxGps.PermissionException exception) {
        PermissionEvent event = new PermissionEvent();
        event.setType(PermissionType.LOCATION);
        event.setResult(Result.ERROR);
        EventManager.post(event);
    }

    public void onMissing(RxGps.PlayServicesNotAvailableException exception) {
        MissingEvent event = new MissingEvent();
        event.setType(MissingType.PLAY_SERVICE);
        event.setResult(Result.ERROR);
        EventManager.post(event);
    }

    public Location getLastLocation() {
        return location;
    }

    public void checkLastLocation(Activity activity) {
        if (gps == null) {
            gps = new RxGps(activity);
        }
        gps.locationLowPower()
                .doOnSubscribe(this::addDisposable)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLocation, throwable -> {
                    if (throwable instanceof RxGps.PermissionException) {
                        onPermission((RxGps.PermissionException) throwable);
                    } else if (throwable instanceof RxGps.PlayServicesNotAvailableException) {
                        onMissing((RxGps.PlayServicesNotAvailableException) throwable);
                    }
                });
    }


}
