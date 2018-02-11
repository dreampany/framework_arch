package com.dreampany.framework.data.api.weather.listener;

import com.dreampany.framework.data.api.weather.exception.WeatherException;

/**
 * Created by air on 29/12/17.
 */

public interface ClientListener {
    void onWeatherError(WeatherException exception);
    void onConnectionError(Throwable t);
}
