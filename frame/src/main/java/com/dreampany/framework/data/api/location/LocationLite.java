package com.dreampany.framework.data.api.location;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.HandlerThread;

import com.dreampany.framework.data.api.location.geocoding.GeocodingProvider;
import com.dreampany.framework.data.enums.MissingType;
import com.dreampany.framework.data.enums.PermissionType;
import com.dreampany.framework.data.enums.Result;
import com.dreampany.framework.data.event.LocationEvent;
import com.dreampany.framework.data.event.MissingEvent;
import com.dreampany.framework.data.event.PermissionEvent;
import com.dreampany.framework.data.manager.EventManager;
import com.dreampany.framework.data.util.TimeUtil;
import com.github.florent37.rxgps.RxGps;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;

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
    private static final long INTERVAL_DEFAULT = 5 * TimeUtil.second;
    private static final long DISTANCE_DEFAULT = 2;

    private static LocationLite instance;
    private final Context context;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedClient;
    private LocationRequest locationRequest;
    private HandlerThread thread;
    private volatile boolean requesting;

    private GeocodingProvider geocodingProvider;

    private static Location location;
    private Callback callback;

    private RxGps gps;
    private CompositeDisposable compositeDisposable;


    private LocationLite(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.context = context.getApplicationContext();
        compositeDisposable = new CompositeDisposable();
/*        locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        fusedClient = LocationServices.getFusedLocationProviderClient(context.getApplicationContext());

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        requesting = false;
        thread = new HandlerThread("background");
        thread.start();
        geocodingProvider = new GeocodingProvider(this.context, this);*/
    }

    synchronized public static LocationLite onInstance(Context context) {
        if (instance == null) {
            instance = new LocationLite(context);
        }
        return instance;
    }

/*    public void register(Callback callback) {
        this.callback = callback;
    }

    public void unregister() {
        this.callback = null;
    }

    public Location getLastLocation() {
        return location;
    }

    public void checkLocationSettings(final Activity activity) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(activity);


        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse response) {
            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(activity, LocationLite.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }


    public void checkLocationSettings(final Fragment fragment) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(fragment.getActivity());


        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(fragment.getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse response) {
            }
        });

        task.addOnFailureListener(fragment.getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            fragment.startIntentSenderForResult(
                                    resolvable.getResolution().getIntentSender(),
                                    LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS,
                                    null, 0, 0, 0, null
                            );
                            //resolvable.startResolutionForResult(activity, LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    public void beginUpdates() {
        if (requesting) {
            endUpdates();
        }

        fusedClient.requestLocationUpdates(locationRequest, locationCallback, thread.getLooper());

        //  locationManager.requestLocationUpdates(getProviderName(), interval, 0, this);
        requesting = true;
    }


    public void endUpdates() {
        if (requesting) {
            // locationManager.removeUpdates(this);
            fusedClient.removeLocationUpdates(locationCallback);
            requesting = false;
        }
    }

    public Location getLocation() {
        return location;
    }

    public void geocoding(long locationId, Location location) {
        geocodingProvider.reverse(locationId, location);
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            LogKit.verbose("Location Result " + locationResult.toString());

            if (locationResult.getLastLocation() != null) {
                location = locationResult.getLastLocation();
                if (callback != null) {
                    callback.onLocation(0L, location);
                }
            }
        }
    };

    @Override
    public void onAddress(long locationId, Location location, Address address) {
        if (callback != null) {
            callback.onAddress(locationId, location, address);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LogKit.verbose("Location " + location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }*/

    public void closeAll() {
        compositeDisposable.clear();
    }

    public void addDisposable(Disposable disposable) {
        this.compositeDisposable.add(disposable);
    }

    public void onLocation(Location location) {
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
        //locationText.setText(location.getLatitude() + ", " + location.getLongitude());
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
