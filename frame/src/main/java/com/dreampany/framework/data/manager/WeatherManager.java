package com.dreampany.framework.data.manager;

import android.content.Context;

import com.dreampany.framework.data.api.network.data.manager.NetworkManager;
import com.dreampany.framework.data.api.weather.WeatherClient;
import com.dreampany.framework.data.api.weather.WeatherConfig;
import com.dreampany.framework.data.api.weather.exception.WeatherException;
import com.dreampany.framework.data.api.weather.model.Weather;
import com.dreampany.framework.data.api.weather.owm.OWMWeatherProvider;
import com.dreampany.framework.data.provider.room.WeatherDatabase;
import com.dreampany.framework.data.util.TimeUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by air on 30/12/17.
 */

public class WeatherManager {

    //Calls per minute: 60

    public static final long idleDelay = TimeUtil.secondToMilli(20);
    private static final long delay = TimeUtil.minuteToMilli(5);

    private static final String OWM_API_KEY = "4fbc9ccf3ef7f41a3ba49dfc4d1c8c8d";

    private static final String WEATHERS = "weathers";
    private static final String TIME = "time";
    private static final String ID = "id";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private static WeatherManager instance;
    private final Context context;
    private final WeatherDatabase database;
    private final WeatherClient weatherClient;
    private final Executor executor;

    private WeatherManager(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.context = context.getApplicationContext();
        database = WeatherDatabase.onInstance(context);
        weatherClient = new WeatherClient(context);
        weatherClient.setProvider(new OWMWeatherProvider());
        WeatherConfig config = new WeatherConfig();
        config.apiKey = OWM_API_KEY;
        weatherClient.updateConfig(config);
        executor = Executors.newSingleThreadExecutor();
    }

    synchronized public static WeatherManager onInstance(Context context) {
        if (instance == null) {
            instance = new WeatherManager(context);
        }
        return instance;
    }

    public Weather getWeather(String id, NetworkManager network) {
        long adjustTime = TimeUtil.currentTime() - delay;
        Weather weather = database.weatherDao().getWeather(id, adjustTime);

        if (weather == null && network.hasInternet()) {
            String[] keys = {ID};
            Object[] values = {id};
            weather = getFromFirestore(keys, values, adjustTime);
            if (weather != null) {
                storeInDatabase(weather);
            }
        }

        if (weather == null && network.hasInternet()) {
            weather = getFromApi(id);
            if (weather != null) {
                weather.setTime(TimeUtil.currentTime());
                storeInDatabase(weather);
                storeInFirestore(weather);
            }
        }

        return weather;
    }

    public Weather getWeather(String city, String country, NetworkManager network) {
        long adjustTime = TimeUtil.currentTime() - delay;
        Weather weather = database.weatherDao().getWeather(city, country, adjustTime);

        if (weather == null && network.hasInternet()) {
            String[] keys = {CITY, COUNTRY};
            Object[] values = {city, country};
            weather = getFromFirestore(keys, values, adjustTime);
            if (weather != null) {
                storeInDatabase(weather);
            }
        }

        if (weather == null && network.hasInternet()) {
            weather = getFromApi(city, country);
            if (weather != null) {
                weather.setTime(TimeUtil.currentTime());
                storeInDatabase(weather);
                storeInFirestore(weather);
            }
        }

        return weather;
    }

    public Weather getCacheWeather(double latitude, double longitude) {
        return WeatherDatabase.onInstance(context).weatherDao().getWeather(latitude, longitude);
    }

    public Weather getWeather(double latitude, double longitude, NetworkManager network) {
        long adjustTime = TimeUtil.currentTime() - delay;
        Weather weather = database.weatherDao().getWeather(latitude, longitude, adjustTime);
        if (weather == null && network.hasInternet()) {
            String[] keys = {LATITUDE, LONGITUDE};
            Object[] values = {latitude, longitude};
            weather = getFromFirestore(keys, values, adjustTime);
            if (weather != null) {
                storeInDatabase(weather);
            }
        }

        if (weather == null && network.hasInternet()) {
            weather = getFromApi(latitude, longitude);
            if (weather != null) {
                storeInDatabase(weather);
                storeInFirestore(weather);
            }
        }

        return weather;
    }

    private Weather getFromFirestore(String[] keys, Object[] values, long time) {
        if (!FirebaseManager.onManager().resolveAuth()) {
            return null;
        }
        return FirestoreManager.onInstance().getSingle(WEATHERS, keys, values, TIME, time, Weather.class);
    }

    private Weather getFromApi(String id) {
        try {
            Weather weather = weatherClient.getWeather(id);
            return weather;
        } catch (WeatherException e) {
            return null;
        }
    }

    private Weather getFromApi(String city, String country) {
        try {
            Weather weather = weatherClient.getWeather(city, country);
            return weather;
        } catch (WeatherException e) {
            return null;
        }
    }

    private Weather getFromApi(double latitude, double longitude) {
        try {
            Weather weather = weatherClient.getWeather(latitude, longitude);
            return weather;
        } catch (WeatherException e) {
            return null;
        }
    }

    private void storeInDatabase(Weather weather) {
        database.weatherDao().insert(weather);
    }

    private void storeInFirestore(Weather weather) {
        if (!FirebaseManager.onManager().resolveAuth()) {
            return;
        }
        FirestoreManager.onInstance().addSingle(WEATHERS, weather.getId(), weather);
    }
}
