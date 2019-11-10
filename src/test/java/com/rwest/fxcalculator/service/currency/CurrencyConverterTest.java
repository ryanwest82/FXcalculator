package com.rwest.fxcalculator.service.currency;

import com.rwest.fxcalculator.domain.ConversionRate;
import com.rwest.fxcalculator.domain.ConversionRequest;
import com.rwest.fxcalculator.domain.ConversionResult;
import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;
import com.rwest.fxcalculator.exceptions.RateNotFoundException;
import com.rwest.fxcalculator.service.rate.RateLookup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
public class CurrencyConverterTest {

    private static CurrencyConverter underTest;

    private static RateLookup rateLookup;

    private static Currency base;

    private static Currency terms;

    private static BigDecimal conversionRate;

    private static ConversionRequest request;

    @BeforeAll
    static void setup() {
        rateLookup = Mockito.mock(RateLookup.class);
        underTest = new CurrencyConverterImpl(rateLookup);
        base = new Currency("GBP", 2);
        terms = new Currency("USD", 2);
        conversionRate = BigDecimal.valueOf(25);
        request = new ConversionRequest(base, terms, BigDecimal.TEN);
    }

    @Test
    void theOneWhereACurrencyConversionSucceeds() {
        try {
            when(rateLookup.findRate(base, terms)).thenReturn(Optional.of(
                    new ConversionRate(base, terms, conversionRate)));

            ConversionResult result = underTest.convert(request);

            assertThat(result.getResult(), is(request.getAmount().multiply(conversionRate)
                    .setScale(terms.getDecimalPrecision())));
        } catch (RateNotFoundException | CurrencyNotFoundException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void theOneWhereARateIsNotFoundAndConversionFails() {
        try {
            when(rateLookup.findRate(base, terms)).thenReturn(Optional.empty());

            underTest.convert(request);

            fail("Conversion should have failed");
        } catch (Exception e) {
            assertThat(e.getClass().getName(), is(RateNotFoundException.class.getName()));
        }
    }
}
