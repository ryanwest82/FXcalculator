package com.rwest.fxcalculator.service.currency;

import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyRefreshException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
public class CurrencyLookupTest {

    private static CurrencyLookup underTest;

    private static Currency aud;

    @BeforeAll
    static void setup() throws CurrencyRefreshException {
        CurrencyService currencyService = Mockito.mock(FileBasedCurrencyService.class);
        aud = new Currency("AUD", 2);
        Currency usd = new Currency("USD", 2);
        Set<Currency> currencies = new HashSet<>();
        currencies.add(aud);
        currencies.add(usd);
        when(currencyService.getCurrencies()).thenReturn(currencies);
        underTest = new CurrencyLookupImpl(currencyService);
    }

    @Test
    void theOneWhereACurrencyLookupSucceeds() {
        Optional<Currency> currencyOptional = underTest.findCurrency(aud.getName());
        assertThat(currencyOptional.get(), is(aud));
    }

    @Test
    void theOneWhereACurrencyLookupFails() {
        Optional<Currency> currencyOptional = underTest.findCurrency("ZZZ");
        assertFalse(currencyOptional.isPresent());
    }

    @Test
    void theOneWhereWeSuccessfullyRetrieveAListOfAllAvailableCurrencies() {
        List<Currency> result = underTest.getCurrencyList();
        assertThat(result.size(), is(2));
    }
}
