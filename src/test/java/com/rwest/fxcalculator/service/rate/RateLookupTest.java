package com.rwest.fxcalculator.service.rate;

import com.rwest.fxcalculator.domain.ConversionRate;
import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;
import com.rwest.fxcalculator.exceptions.RateRefreshException;
import com.rwest.fxcalculator.service.currency.CurrencyLookup;
import com.rwest.fxcalculator.service.currency.CurrencyLookupImpl;
import com.rwest.fxcalculator.util.RateInverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
public class RateLookupTest {

    private static RateLookup underTest;

    private static Currency aud;

    private static Currency gbp;

    private static Currency usd;

    private static Currency jpy;

    private static ConversionRate audGbpRate;

    @BeforeAll
    static void setup() throws RateRefreshException, CurrencyNotFoundException {
        RateService rateService = Mockito.mock(FileBasedRateService.class);
        aud = new Currency("AUD", 2);
        gbp = new Currency("GBP", 2);
        usd = new Currency("USD", 2);
        jpy = new Currency("JPY", 0);

        audGbpRate = new ConversionRate(aud, gbp, BigDecimal.valueOf(1.2314));
        ConversionRate gbpUsdRate = new ConversionRate(gbp, usd, BigDecimal.valueOf(0.983));
        ConversionRate audUsdRate = new ConversionRate(aud, usd, gbp);
        Set<ConversionRate> rates = new HashSet<>();
        rates.add(audGbpRate);
        rates.add(gbpUsdRate);
        rates.add(audUsdRate);
        when(rateService.getRates()).thenReturn(rates);

        CurrencyLookup currencyLookup = Mockito.mock(CurrencyLookupImpl.class);
        List<Currency> currencies = new ArrayList<>();
        currencies.add(aud);
        currencies.add(gbp);
        currencies.add(usd);
        currencies.add(jpy);
        when(currencyLookup.getCurrencyList()).thenReturn(currencies);

        when(rateService.getCurrencyLookup()).thenReturn(currencyLookup);
        underTest = new RateLookupImpl(rateService);
    }

    @Test
    void theOneWhereAConversionRateIsSuccessfullyFound() {
        try {
            Optional<ConversionRate> rate = underTest.findRate(aud, gbp);
            assertThat(rate.get(), is(audGbpRate));
        } catch (CurrencyNotFoundException exc) {
            fail("Failed to validate input currencies");
        }
    }

    @Test
    void theOneWhereAnInvertedRateIsSuccessfullyFound() throws CurrencyNotFoundException {
        Optional<ConversionRate> invertedRate = underTest.findRate(gbp, aud);
        assertTrue(invertedRate.isPresent());
        assertThat(invertedRate.get().getRate(),
                is(RateInverter.invert(audGbpRate.getRate(), audGbpRate.getRate().scale())));
    }

    @Test
    void theOneWhereAUnityRateIsSucessfullyFound() throws CurrencyNotFoundException {
        Optional<ConversionRate> unityRate = underTest.findRate(aud, aud);
        assertTrue(unityRate.isPresent());
        assertThat(unityRate.get().getRate(), is(BigDecimal.ONE));
    }

    @Test
    void theOneWhereACrossViaRateIsSuccessfullyFound() throws CurrencyNotFoundException {
        Optional<ConversionRate> crossViaRate = underTest.findRate(aud, usd);
        assertTrue(crossViaRate.isPresent());
        assertThat(crossViaRate.get(), is(audGbpRate));
    }

    @Test
    void theOneWhereTheInputCurrenciesAreValidButAConversionRateCannotBeFound() {
        try {
            Optional<ConversionRate> result = underTest.findRate(aud, jpy);
            assertFalse(result.isPresent());
        } catch (CurrencyNotFoundException exc) {
            fail("Both Base and Terms currencies should have been validated");
        }
    }
}
