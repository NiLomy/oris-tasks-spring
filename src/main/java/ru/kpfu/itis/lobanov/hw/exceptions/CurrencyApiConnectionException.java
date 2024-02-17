package ru.kpfu.itis.lobanov.hw.exceptions;

public class CurrencyApiConnectionException extends RuntimeException {
    public CurrencyApiConnectionException() {
    }

    public CurrencyApiConnectionException(String message) {
        super(message);
    }

    public CurrencyApiConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyApiConnectionException(Throwable cause) {
        super(cause);
    }

    public CurrencyApiConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
