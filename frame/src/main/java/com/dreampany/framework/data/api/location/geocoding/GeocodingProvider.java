package com.dreampany.framework.data.api.location.geocoding;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Pair;

import com.dreampany.framework.data.util.LogKit;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by air on 10/16/17.
 */

public class GeocodingProvider {

    public interface Callback {
        void onAddress(long locationId, Location location, Address address);
    }

    private Context context;
    private Locale locale;
    private Callback callback;
    private List<Pair<Long, Location>> locations;
    private Map<Location, Address> addresses;
    private Executor executor;
    private Geocoder geocoder;
    private volatile boolean processing;

    public GeocodingProvider(Context context, Callback callback) {
        this(context, Locale.getDefault(), callback);
    }

    public GeocodingProvider(Context context, Locale locale, Callback callback) {
        if (context == null) {
            throw new RuntimeException("Context is null");
        }
        if (!Geocoder.isPresent()) {
            throw new RuntimeException("Android Geocoder not present. Please check if Geocoder.isPresent() before invoking the search");
        }
        this.context = context.getApplicationContext();
        this.locale = locale;
        this.callback = callback;
        this.locations = Collections.synchronizedList(new ArrayList<Pair<Long, Location>>());
        this.addresses = Maps.newConcurrentMap();
        executor = Executors.newCachedThreadPool();
        if (locale == null) {
            geocoder = new Geocoder(this.context);
        } else {
            geocoder = new Geocoder(this.context, this.locale);
        }
    }

    public void reverse(long locationId, Location location) {

        if (addresses.containsKey(location)) {
            Address address = addresses.get(location);
            if (callback != null) {
                callback.onAddress(locationId, location, address);
            }
            return;
        }
        locations.add(Pair.create(locationId, location));
        if (processing) {
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                processing = true;
                processReverse();
                processing = false;
            }
        });
    }

    private void processReverse() {

        while (!locations.isEmpty()) {

            Pair<Long, Location> entry = locations.remove(0);

            long locationId = entry.first;
            Location location = entry.second;

            if (!addresses.containsKey(location)) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    LogKit.verbose("Reverse address " + addresses);
                    if (addresses != null) {
                        for (Address address : addresses) {
                            this.addresses.put(location, address);
                            break; // need working for more than 1 results
                        }
                    }

                } catch (IOException e) {
                    LogKit.verbose("error in geocoding: " + e.toString());
                }
            }

            if (addresses.containsKey(location)) {
                Address address = addresses.get(location);
                if (callback != null) {
                    callback.onAddress(locationId, location, address);
                }
            }
        }
    }

}
