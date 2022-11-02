package pl.kurs.model;

import pl.kurs.model.exceptions.HttpConnectionException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class HttpConnection implements Closeable {

    private HttpURLConnection connection;

    public HttpConnection(HttpURLConnection connection) {
        this.connection = connection;
    }

    public HttpConnection(String url) throws IOException {
        this((HttpURLConnection) new URL(url).openConnection());
    }

    public void setRequestMethodToGet() throws ProtocolException {
        connection.setRequestMethod("GET");
    }

    public void setRequestProperty(String key, String value) {
        connection.setRequestProperty(key, value);
    }

    public String response() throws HttpConnectionException {
        checkConnectionCode();
        String inputLine;
        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (IOException e) {
            throw new HttpConnectionException(e);
        }

        return content.toString();
    }

    private void checkConnectionCode() throws HttpConnectionException {
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new HttpConnectionException("Connection error! Code: " + responseCode + " " + connection.getResponseMessage());
            }
        } catch (IOException | HttpConnectionException e) {
            throw new HttpConnectionException(e);
        }
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    @Override
    public void close() throws IOException {
        connection.disconnect();
    }
}
