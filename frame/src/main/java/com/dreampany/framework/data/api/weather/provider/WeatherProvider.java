package com.dreampany.framework.data.api.weather.provider;

import com.dreampany.framework.data.api.weather.WeatherConfig;
import com.dreampany.framework.data.api.weather.exception.ApiKeyRequiredException;
import com.dreampany.framework.data.api.weather.exception.WeatherException;
import com.dreampany.framework.data.api.weather.model.City;
import com.dreampany.framework.data.api.weather.model.Weather;

import java.util.List;

/**
 * Created by air on 29/12/17.
 */

public interface WeatherProvider {
    void setConfig(WeatherConfig config);

    void setCodeProvider(CodeProvider codeProvider);

    String getWeatherUrl(String id) throws ApiKeyRequiredException;

    String getWeatherUrl(String city, String country) throws ApiKeyRequiredException;

    String getWeatherUrl(double latitude, double longitude) throws ApiKeyRequiredException;

    Weather getWeather(String data) throws WeatherException;

    List<City> getCities(String data) throws WeatherException;

}
