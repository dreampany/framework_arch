package com.dreampany.framework.data.api.weather.exception;

/**
 * Created by air on 29/12/17.
 */

public class WeatherProviderInstantiationException extends Exception {
    public WeatherProviderInstantiationException() {
    }

    public WeatherProviderInstantiationException(String message) {
        super(message);
    }

    public WeatherProviderInstantiationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public WeatherProviderInstantiationException(Throwable throwable) {
        super(throwable);
    }
}
