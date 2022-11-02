package pl.kurs.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.kurs.model.exceptions.HttpConnectionException;

import java.io.*;
import java.net.HttpURLConnection;

import static org.junit.Assert.*;

public class HttpConnectionTest {

    @Mock
    private HttpConnection httpConnection;
    @Mock
    private HttpURLConnection connection;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldThrowHttpConnectionExceptionWhenOpenConnectionNotSucceed() {
        Throwable throwable = new HttpConnectionException();
        assertThrows(IOException.class, () -> httpConnection = new HttpConnection("111"));
        assertEquals(HttpConnectionException.class, throwable.getClass());
    }

    @Test
    public void shouldCreateHttpUrlConnection() {
        httpConnection = new HttpConnection(connection);
    }

    @Test
    public void shouldSetRequestMethodToGET() throws IOException {
        httpConnection = new HttpConnection("https://google.com/");
        httpConnection.setRequestMethodToGet();
        assertEquals("GET", httpConnection.getConnection().getRequestMethod());
    }

    @Test
    public void shouldSetRequestPropertyKeyTo2137() throws IOException {
        httpConnection = new HttpConnection("https://google.com/");
        assertNull(httpConnection.getConnection().getRequestProperty("apikey"));
        httpConnection.setRequestProperty("apikey", "2137");
        assertEquals("2137", httpConnection.getConnection().getRequestProperty("apikey"));
    }

    @Test
    public void shouldThrowHttpConnectionExceptionWhenCodeResponseIs199() throws IOException {
        httpConnection = new HttpConnection(connection);
        Mockito.when(connection.getResponseCode()).thenReturn(199);
        Throwable throwable = new HttpConnectionException();
        assertThrows(HttpConnectionException.class, () -> httpConnection.response());
        assertEquals(HttpConnectionException.class, throwable.getClass());
    }

    @Test
    public void shouldThrowHttpConnectionExceptionWhenCodeResponseIs201() throws IOException {
        httpConnection = new HttpConnection(connection);
        Mockito.when(connection.getResponseCode()).thenReturn(201);
        Throwable throwable = new HttpConnectionException();
        assertThrows(HttpConnectionException.class, () -> httpConnection.response());
        assertEquals(HttpConnectionException.class, throwable.getClass());
    }

    @Test
    public void shouldNotThrowHttpConnectionExceptionWhenCodeResponseIs200() throws IOException, HttpConnectionException {
        httpConnection = new HttpConnection(connection);
        Mockito.when(connection.getResponseCode()).thenReturn(200);
        InputStream is = new ByteArrayInputStream("TEMPLATE".getBytes());
        Mockito.when(connection.getInputStream()).thenReturn(is);
        assertEquals("TEMPLATE", httpConnection.response());
    }

    @Test
    public void shouldThrowHttpConnectionExceptionWhenCodeResponseIs200AndTheResponseFromAPIIsIncorrect() throws IOException {
        httpConnection = new HttpConnection(connection);
        Mockito.when(connection.getResponseCode()).thenReturn(200);
        InputStream is = new ByteArrayInputStream("XD".getBytes());
        Mockito.when(connection.getInputStream()).thenReturn(is);
        Mockito.when(new BufferedReader(
                new InputStreamReader(connection.getInputStream()))).thenThrow(IOException.class);
        Throwable throwable = new HttpConnectionException();
        assertThrows(HttpConnectionException.class, () -> httpConnection.response());
        assertEquals(HttpConnectionException.class, throwable.getClass());
    }

    @Test
    public void shouldNotThrowHttpConnectionExceptionWhenCodeResponseIs200XDD() throws IOException {
        httpConnection = new HttpConnection(connection);
        httpConnection.close();
    }
}