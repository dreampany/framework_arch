package com.dreampany.framework.data.api.weather.provider;

/**
 * Created by air on 29/12/17.
 */

public interface ProviderType {
    Class getWeatherProviderClass();

    Class getCodeProviderClass();
}
