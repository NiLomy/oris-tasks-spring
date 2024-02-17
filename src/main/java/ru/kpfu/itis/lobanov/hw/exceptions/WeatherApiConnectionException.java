package ru.kpfu.itis.lobanov.hw.exceptions;

public class WeatherApiConnectionException extends RuntimeException {
    public WeatherApiConnectionException() {
    }

    public WeatherApiConnectionException(String message) {
        super(message);
    }

    public WeatherApiConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherApiConnectionException(Throwable cause) {
        super(cause);
    }

    public WeatherApiConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
