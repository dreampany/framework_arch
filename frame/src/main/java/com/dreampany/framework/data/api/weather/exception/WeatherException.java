package com.dreampany.framework.data.api.weather.exception;

/**
 * Created by air on 29/12/17.
 */

public class WeatherException extends Exception {
    public WeatherException() {
    }

    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public WeatherException(Throwable throwable) {
        super(throwable);
    }
}
