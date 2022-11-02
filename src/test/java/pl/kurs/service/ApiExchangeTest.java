package pl.kurs.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.kurs.model.ConnectionFactory;
import pl.kurs.model.HttpConnection;
import pl.kurs.model.exceptions.HttpConnectionException;

import static org.junit.Assert.*;

import java.io.IOException;

public class ApiExchangeTest {

    private ApiExchange api;

    private String SAMPLE_RESPONSE = "{\n" +
            "  \"success\" : true,\n" +
            "  \"query\" : {\n" +
            "    \"from\" : \"USD\",\n" +
            "    \"to\" : \"PLN\",\n" +
            "    \"amount\" : 100\n" +
            "  },\n" +
            "  \"info\" : {\n" +
            "    \"timestamp\" : 1663661371,\n" +
            "    \"rate\" : 4.713388\n" +
            "  },\n" +
            "  \"date\" : \"2022-09-20\",\n" +
            "  \"result\" : 471.3388\n" +
            "}";

    @Mock
    private ConnectionFactory connectionFactory;
    @Mock
    private HttpConnection httpConnection;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        api = new ApiExchange(connectionFactory);
    }

    @Test
    public void shouldReturn471Point3388WhenWeHaveSampleReponse() throws HttpConnectionException, IOException {
        Mockito.when(connectionFactory.build(Mockito.anyString())).thenReturn(httpConnection);
        Mockito.when(httpConnection.response()).thenReturn(SAMPLE_RESPONSE);
        assertEquals(471.3388, api.exchange("USD", "PLN", 100.0), 0.00001);
    }

    @Test
    public void shouldThrowHttpConnectionExceptionWhenTheResponseIsIncorrect() throws HttpConnectionException, IOException {
        Mockito.when(connectionFactory.build(Mockito.anyString())).thenReturn(httpConnection);
        Mockito.when(httpConnection.response()).thenThrow(HttpConnectionException.class);
        Throwable throwable = new HttpConnectionException();
        assertThrows(HttpConnectionException.class, () -> api.exchange("USD", "PLN", 100.0));
        assertEquals(HttpConnectionException.class, throwable.getClass());
    }

}