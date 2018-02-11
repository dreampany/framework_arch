package com.dreampany.framework.data.api.weather.provider;

import com.dreampany.framework.data.api.weather.WeatherConfig;
import com.dreampany.framework.data.api.weather.exception.WeatherProviderInstantiationException;

/**
 * Created by air on 29/12/17.
 */

public class ProviderFactory {

    public static WeatherProvider createProvider(ProviderType providerType) throws WeatherProviderInstantiationException {
        return createProvider(providerType, null);
    }

    public static WeatherProvider createProvider(ProviderType providerType, WeatherConfig config) throws WeatherProviderInstantiationException {
        Class clazz = providerType.getWeatherProviderClass();
        try {
            WeatherProvider provider = (WeatherProvider) clazz.newInstance();
            if (config != null) {
                provider.setConfig(config);
            }

            if (providerType.getCodeProviderClass() != null) {
                Class codeClazz = providerType.getCodeProviderClass();
                CodeProvider codeProvider = (CodeProvider) codeClazz.newInstance();
                provider.setCodeProvider(codeProvider);
            }

            return provider;
        } catch (InstantiationException e) {
            throw new WeatherProviderInstantiationException();
        } catch (IllegalAccessException e) {
            throw new WeatherProviderInstantiationException();
        }
    }
}
