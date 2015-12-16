package ru.trendtech.services.http;

/**
 * User: max
 * Date: 2/4/13
 * Time: 4:50 PM
 */

public class HttpServiceAPIException extends Exception {

    public HttpServiceAPIException() {
    }

    public HttpServiceAPIException(String message) {
        super(message);
    }

    public HttpServiceAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpServiceAPIException(Throwable cause) {
        super(cause);
    }
}
