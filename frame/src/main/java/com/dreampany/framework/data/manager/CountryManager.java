package com.dreampany.framework.data.manager;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.telephony.TelephonyManager;

import com.dreampany.framework.data.http.HttpManager;
import com.dreampany.framework.data.provider.pref.FramePref;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.JsonUtil;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.Locale;

/**
 * Created by air on 2/2/18.
 */

public final class CountryManager {

    private static final String COUNTRY_URL = "http://ip-api.com/json";
    private static final String COUNTRY = "country";

    private static CountryManager instance;
    private final Context context;

    private final BiMap<String, String> countries;

    private CountryManager(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.context = context.getApplicationContext();
        countries = HashBiMap.create();
    }

    synchronized public static CountryManager onInstance(Context context) {
        if (instance == null) {
            instance = new CountryManager(context);
        }
        return instance;
    }

    public String getCountry(FramePref pref, Location location) {
        String country = pref.getString(COUNTRY);
        if (!DataUtil.isEmpty(country)) {
            return country;
        }

        if (location != null) {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (!DataUtil.isEmpty(addresses)) {
                    country = addresses.get(0).getCountryName();
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        if (country == null) {
            country = getCountryBasedOnSimCardOrNetwork(context);
        }

        if (country == null) {
            country = getCountryFromIp();
        }

        if (country != null) {
            pref.putString(COUNTRY, country);
            return country;
        }
        return null;
    }

    private String getCountryBasedOnSimCardOrNetwork(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    private String getCountryFromIp() {
        HttpManager manager = new HttpManager();
        JsonParser parser = new JsonParser();
        String json = manager.get(COUNTRY_URL);

        if (DataUtil.isEmpty(json)) {
            return null;
        }

        try {
            JsonObject object = parser.parse(json).getAsJsonObject();
            if (object == null) {
                return null;
            }

            String country = JsonUtil.getString(object, COUNTRY, null);
            return country;
        } catch (NullPointerException | JsonParseException ignored) {
            return null;
        }
    }


}
