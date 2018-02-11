package com.dreampany.framework.data.event;

import android.location.Location;

/**
 * Created by air on 1/1/18.
 */

public class LocationEvent extends Event<Location> {
    public LocationEvent(Location location) {
        super(location);
    }
}
