package com.rwest.fxcalculator.service.rate;

import com.rwest.fxcalculator.config.ApplicationConfig;
import com.rwest.fxcalculator.domain.ConversionRate;
import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;
import com.rwest.fxcalculator.exceptions.RateRefreshException;
import com.rwest.fxcalculator.service.currency.CurrencyLookup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Simple implementation of the RateService, which gathers rate information from a local file
 */
public class FileBasedRateService extends RateService {

    private static final String DELIMITER = ",";

    public FileBasedRateService(CurrencyLookup currencyLookup) {
        super(currencyLookup);
    }

    @Override
    public Set<ConversionRate> getRates() throws RateRefreshException, CurrencyNotFoundException {

        Set<ConversionRate> conversionRates = new HashSet<>();

        try (BufferedReader csvReader = new BufferedReader(
                new FileReader(getClass().getResource(ApplicationConfig.getRateFilePath()).getFile()))) {
            String row;

            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(DELIMITER);
                String fromName = data[0];
                String toName = data[1];

                Optional<Currency> from = currencyLookup.findCurrency(fromName);
                Optional<Currency> to = currencyLookup.findCurrency(toName);

                Currency fromCurrency = from.orElseThrow(() -> new CurrencyNotFoundException(fromName));
                Currency toCurrency = to.orElseThrow(() -> new CurrencyNotFoundException(toName));

                try {
                    BigDecimal rate = new BigDecimal(data[2]);
                    conversionRates.add(new ConversionRate(fromCurrency, toCurrency, rate));
                } catch (NumberFormatException nfe) {
                    String viaName = data[2];
                    Optional<Currency> via = currencyLookup.findCurrency(viaName);
                    conversionRates.add(new ConversionRate(fromCurrency, toCurrency,
                            via.orElseThrow(() -> new CurrencyNotFoundException(viaName))));
                }

            }
        } catch (IOException e) {
            throw new RateRefreshException("Failed to read rates from file", e);
        }

        return conversionRates;
    }
}
