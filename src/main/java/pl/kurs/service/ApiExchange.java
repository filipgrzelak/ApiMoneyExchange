package pl.kurs.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kurs.model.ExchangeCurrencies;
import pl.kurs.model.HttpConnection;
import pl.kurs.model.ConnectionFactory;
import pl.kurs.model.exceptions.HttpConnectionException;

public class ApiExchange implements ExchangeCurrencies {

    private static final String URL_API_TEMPLATE = "https://api.apilayer.com/exchangerates_data/";
    private final ConnectionFactory factory;

    public ApiExchange(ConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public double exchange(String currencyFrom, String currencyTo, double amount) throws HttpConnectionException {
        double result;
        String properies = "convert?to=" + currencyTo + "&from=" + currencyFrom + "&amount=" + amount;
        try (HttpConnection connection = factory.build(URL_API_TEMPLATE + properies)) {
            connection.setRequestMethodToGet();
            connection.setRequestProperty("apikey", "fGPsmd9t1GgIuOJngU3e4KWo2IU0xZUG");
            ObjectMapper objectMapper = new ObjectMapper();
            String response = connection.response();
            JsonNode jsonNode = objectMapper.readTree(response);
            result = jsonNode.get("result").asDouble();
        } catch (Exception e) {
            throw new HttpConnectionException(e);
        }
        return result;
    }
}
