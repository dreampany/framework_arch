package com.dreampany.framework.data.api.weather.provider;

import com.dreampany.framework.data.api.weather.WeatherCode;

/**
 * Created by air on 29/12/17.
 */

public interface CodeProvider {
    WeatherCode getWeatherCode(String code);
}
