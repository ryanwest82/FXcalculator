package com.rwest.fxcalculator.service.currency;

import com.rwest.fxcalculator.config.ApplicationConfig;
import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyRefreshException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple implementation of the CurrencyService, which gathers currency information from a local file
 */
public class FileBasedCurrencyService implements CurrencyService {

    private static final String DELIMITER = ",";

    @Override
    public Set<Currency> getCurrencies() throws CurrencyRefreshException {

        Set<Currency> currencySet = new HashSet<>();

        try (BufferedReader csvReader = new BufferedReader(
                new FileReader(getClass().getResource(ApplicationConfig.getCurrencyFilePath()).getFile()))) {

            String row;

            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(DELIMITER);
                currencySet.add(new Currency(data[0], Integer.valueOf(data[1])));
            }
        } catch (IOException e) {
            throw new CurrencyRefreshException("Failed to read currencies from file", e);
        }

        return currencySet;
    }
}
