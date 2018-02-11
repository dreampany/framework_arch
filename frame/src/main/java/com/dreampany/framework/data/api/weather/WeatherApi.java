package com.dreampany.framework.data.api.weather;

import android.content.Context;

import com.dreampany.framework.data.api.weather.exception.ApiKeyRequiredException;
import com.dreampany.framework.data.api.weather.exception.WeatherException;
import com.dreampany.framework.data.api.weather.model.Weather;
import com.dreampany.framework.data.api.weather.provider.WeatherProvider;

/**
 * Created by air on 29/12/17.
 */

public abstract class WeatherApi {

    protected final Context context;
    protected WeatherProvider provider;

    protected WeatherApi(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.context = context.getApplicationContext();
    }

    public void setProvider(WeatherProvider provider) {
        this.provider = provider;
    }

    public void updateConfig(WeatherConfig config) {
        provider.setConfig(config);
    }

    public abstract Weather getWeather(String id) throws ApiKeyRequiredException, WeatherException;

    public abstract Weather getWeather(String city, String country) throws ApiKeyRequiredException, WeatherException;

    public abstract Weather getWeather(double latitude, double longitude) throws ApiKeyRequiredException, WeatherException;

}
