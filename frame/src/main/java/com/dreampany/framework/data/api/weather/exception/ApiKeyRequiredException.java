package com.dreampany.framework.data.api.weather.exception;

/**
 * Created by air on 29/12/17.
 */

public class ApiKeyRequiredException extends RuntimeException {

    public ApiKeyRequiredException() {
    }

    public ApiKeyRequiredException(String message) {
        super(message);
    }

    public ApiKeyRequiredException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ApiKeyRequiredException(Throwable throwable) {
        super(throwable);
    }
}
