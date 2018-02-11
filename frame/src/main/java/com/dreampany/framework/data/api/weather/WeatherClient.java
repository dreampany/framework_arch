package com.dreampany.framework.data.api.weather;

import android.content.Context;

import com.dreampany.framework.data.api.weather.exception.ApiKeyRequiredException;
import com.dreampany.framework.data.api.weather.exception.WeatherException;
import com.dreampany.framework.data.api.weather.model.Weather;
import com.dreampany.framework.data.http.HttpManager;

/**
 * Created by air on 29/12/17.
 */

public class WeatherClient extends WeatherApi {

    private HttpManager http;

    public WeatherClient(Context context) {
        super(context);
        http = new HttpManager();
    }

    @Override
    public Weather getWeather(String id) throws ApiKeyRequiredException, WeatherException {
        String url = provider.getWeatherUrl(id);
        return getWeatherByUrl(url);
    }

    @Override
    public Weather getWeather(String city, String country) throws ApiKeyRequiredException, WeatherException {
        String url = provider.getWeatherUrl(city, country);
        return getWeatherByUrl(url);    }

    @Override
    public Weather getWeather(double latitude, double longitude) throws ApiKeyRequiredException, WeatherException {
        String url = provider.getWeatherUrl(latitude, longitude);
        return getWeatherByUrl(url);    }


    private Weather getWeatherByUrl(String url) {
        String data = null;

        try {
            data = http.get(url);
        } catch (Throwable t) {
            return null;
        }

        try {
            Weather weather = provider.getWeather(data);
            return weather;
        } catch (WeatherException t) {
            return null;
        }
    }
}
