package com.rwest.fxcalculator.service.currency;

import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyRefreshException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * CurrencyLookup implementation - relies up a CurrencyService implementation to gather current currency info.
 * Provides functionality to refresh the available currencies data, and to validate an input currency
 * inputted by the user
 */
public class CurrencyLookupImpl implements CurrencyLookup {

    private static final Logger LOGGER = Logger.getLogger(CurrencyLookupImpl.class.getName());

    private final CurrencyService currencyService;

    private Map<String, Currency> currencies;

    public CurrencyLookupImpl(CurrencyService currencyService) throws CurrencyRefreshException {
        this.currencyService = currencyService;
        refreshCurrencies();
    }

    @Override
    public Optional<Currency> findCurrency(String currencyName) {
        return Optional.ofNullable(currencies.get(currencyName));
    }

    @Override
    public List<Currency> getCurrencyList() {
        return new ArrayList<>(currencies.values());
    }

    private void refreshCurrencies() throws CurrencyRefreshException {
        Set<Currency> rawCurrencySet = currencyService.getCurrencies();
        Map<String, Currency> currencies = new HashMap<>();

        rawCurrencySet.forEach(c -> currencies.put(c.getName(), c));

        this.currencies = Collections.unmodifiableMap(currencies);

        LOGGER.info("Currencies have been refreshed");
    }
}
