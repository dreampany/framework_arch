package com.dreampany.framework.data.api.weather.owm;

import com.dreampany.framework.data.api.weather.provider.ProviderType;

/**
 * Created by air on 29/12/17.
 */

public class OWMProviderType implements ProviderType {

    @Override
    public Class getWeatherProviderClass() {
        return OWMWeatherProvider.class;
    }

    @Override
    public Class getCodeProviderClass() {
        return OWMCodeProvider.class;
    }
}
