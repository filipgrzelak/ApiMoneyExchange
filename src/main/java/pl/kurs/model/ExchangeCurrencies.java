package pl.kurs.model;

import pl.kurs.model.exceptions.HttpConnectionException;

public interface ExchangeCurrencies {
    double exchange(String currencyFrom, String currencyTo, double amount) throws HttpConnectionException;
}
