package com.dreampany.framework.data.manager;

import android.content.Context;

import com.dreampany.framework.data.api.network.data.manager.NetworkManager;
import com.dreampany.framework.data.api.weather.WeatherClient;
import com.dreampany.framework.data.api.weather.WeatherConfig;
import com.dreampany.framework.data.api.weather.exception.WeatherException;
import com.dreampany.framework.data.api.weather.model.Weather;
import com.dreampany.framework.data.api.weather.owm.OWMWeatherProvider;
import com.dreampany.framework.data.model.StoreTask;
import com.dreampany.framework.data.provider.room.WeatherDatabase;
import com.dreampany.framework.data.util.TimeUtil;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by air on 30/12/17.
 */

public class WeatherManager extends Manager<StoreTask<?>> {

    //Calls per minute: 60
    public static final long idleDelay = TimeUtil.secondToMilli(20);
    private static final long delay = TimeUtil.minuteToMilli(1);
    private static final String OWM_API_KEY = "9a9464d69f911fab47802f0a801c40ed";

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
    private final FirestoreManager firestore;
    private final WeatherClient client;
    private final NetworkManager network;

    public WeatherManager(Context context, NetworkManager network) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.context = context.getApplicationContext();
        this.network = network;
        database = WeatherDatabase.onInstance(context);
        firestore = FirestoreManager.onInstance();
        client = new WeatherClient(context);
        client.setProvider(new OWMWeatherProvider());
        WeatherConfig config = new WeatherConfig();
        config.apiKey = OWM_API_KEY;
        client.updateConfig(config);
    }

    @Override
    protected boolean looping() throws InterruptedException {
        StoreTask<?> task = takeTask();
        if (task == null) {
            waitRunner(wait);
            wait += pureWait;
            return true;
        }
        wait = defaultWait;

        long fireTime = firestore.getLastTime();
        if (!isExpired(fireTime, idleDelay)) {
            putTaskUniquely(task);
            long delay = TimeUtil.getExpireTime(fireTime);
            long wait = idleDelay - delay;
            if (wait > 0L) {
                waitRunner(wait);
            }
            return true;
        }

        if (!FirebaseManager.onManager().resolveAuth()) {
            putTaskUniquely(task);
            return true;
        }
        firestore.addMultiple(task.getCollection(), task.getData());
        waitRunner(defaultWait);
        return true;
    }


    public Weather getWeather(String id) {
        long adjustTime = TimeUtil.currentTime() - delay;
        Weather weather = database.weatherDao().getWeather(id, adjustTime);

        if (weather == null && network.hasInternet()) {
            String[] keys = {ID};
            Object[] values = {id};
            weather = getFromFirestore(keys, values, adjustTime);
            storeInDatabase(weather);
        }

        if (weather == null && network.hasInternet()) {
            weather = getFromApi(id);
            if (weather != null) {
                weather.setTime(TimeUtil.currentTime());
            }
            storeInDatabase(weather);
            storeInFirestore(weather);
        }

        return weather;
    }

    public Weather getWeather(String city, String country) {
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
        return database.weatherDao().getWeather(latitude, longitude);
    }

    public Weather getCacheWeatherTimely(double latitude, double longitude) {
        long expire = TimeUtil.getExpireTime(delay);
        return database.weatherDao().getWeather(latitude, longitude, expire);
    }

    public Weather getLastWeather() {
        return database.weatherDao().getLastWeather();
    }

    public Weather getCloudWeather(double latitude, double longitude) {
        Weather weather = null;
        if (network.hasInternet()) {
            String[] keys = {LATITUDE, LONGITUDE};
            Object[] values = {latitude, longitude};
            long expire = TimeUtil.getExpireTime(delay);
            weather = getFromFirestore(keys, values, expire);
            if (weather == null) {
                weather = getFromApi(latitude, longitude);
                storeInFirestore(weather);
            }
            storeInDatabase(weather);
        }
        return weather;
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
        if (FirebaseManager.onManager().resolveAuth()) {
            return firestore.getSingle(WEATHERS, keys, values, TIME, time, Weather.class);
        }
        return null;
    }

    private Weather getFromApi(String id) {
        try {
            Weather weather = client.getWeather(id);
            return weather;
        } catch (WeatherException e) {
            return null;
        }
    }

    private Weather getFromApi(String city, String country) {
        try {
            Weather weather = client.getWeather(city, country);
            return weather;
        } catch (WeatherException e) {
            return null;
        }
    }

    private Weather getFromApi(double latitude, double longitude) {
        try {
            Weather weather = client.getWeather(latitude, longitude);
            return weather;
        } catch (WeatherException e) {
            return null;
        }
    }

    private void storeInDatabase(Weather weather) {
        if (weather == null) {
            return;
        }
        database.weatherDao().insert(weather);
    }

    private void storeInFirestore(Weather weather) {
        if (weather == null) {
            return;
        }
        String collection = WEATHERS;
        Map<String, Object> data = Maps.newHashMap();
        data.put(weather.getId(), weather);
        StoreTask<?> task = new StoreTask<>();
        task.setCollection(collection);
        task.setData(data);
        putTaskUniquely(task);
    }
}
