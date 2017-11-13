package com.dreampany.framework.data.manager;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by nuc on 12/12/2016.
 */

public final class GoogleManager {

    private GoogleApiClient googleApiClient;

    private GoogleManager() {
    }

    private static GoogleManager googleManager;

    public static GoogleManager onManager() {
        if (googleManager == null) {
            googleManager = new GoogleManager();
        }
        return googleManager;
    }

    public Location getLastKnownLocation(Context context) {
        if (PermissionManager.hasLocationPermissionGranted(context))
            return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        return null;
    }
}
