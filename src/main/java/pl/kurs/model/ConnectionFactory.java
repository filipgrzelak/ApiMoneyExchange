package pl.kurs.model;

import java.io.IOException;

public class ConnectionFactory {
    public HttpConnection build(String url) throws IOException {
        return new HttpConnection(url);
    }
}
