package com.dreampany.framework.data.api.weather.listener;

import com.dreampany.framework.data.api.weather.model.Weather;

/**
 * Created by air on 29/12/17.
 */

public interface WeatherEventListener extends ClientListener {
    void onWeatherRetrieved(Weather weather);
}
