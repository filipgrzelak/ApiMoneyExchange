package pl.kurs.model.exceptions;

public class HttpConnectionException extends Exception{
    public HttpConnectionException() {
    }

    public HttpConnectionException(String message) {
        super(message);
    }

    public HttpConnectionException(Throwable cause) {
        super(cause);
    }
}
