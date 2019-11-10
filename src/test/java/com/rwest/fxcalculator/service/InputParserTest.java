package com.rwest.fxcalculator.service;

import com.rwest.fxcalculator.domain.ConversionRequest;
import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;
import com.rwest.fxcalculator.exceptions.InvalidInputException;
import com.rwest.fxcalculator.service.currency.CurrencyLookup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
public class InputParserTest {

    private static InputParser underTest;

    private static Currency aud;

    private static Currency gbp;

    @BeforeAll
    static void setup() {
        CurrencyLookup currencyLookup = Mockito.mock(CurrencyLookup.class);
        aud = new Currency("AUD", 2);
        gbp = new Currency("GBP", 2);
        when(currencyLookup.findCurrency(aud.getName())).thenReturn(Optional.of(aud));
        when(currencyLookup.findCurrency(gbp.getName())).thenReturn(Optional.of(gbp));
        underTest = new InputParserImpl(currencyLookup);
    }

    @Test
    void theOneWhereAnInputStringIsSuccessfullyParsed() {
        try {
            int amount = 100;
            String input = aud.getName() + " " + amount + " in " + gbp.getName();

            ConversionRequest request = underTest.parse(input);

            assertThat(request.getAmount(), is(BigDecimal.valueOf(amount)));
            assertThat(request.getBase(), is(aud));
            assertThat(request.getTerms(), is(gbp));
        } catch (CurrencyNotFoundException cnf) {
            fail("CurrencyNotFoundException should not have been thrown");
        } catch (InvalidInputException iie) {
            fail("InvalidInputException should not have been thrown");
        }
    }

    @Test
    void theOneWhereAnInputStringFailsValidation() {
        try {
            underTest.parse("What is 500 euros in yen?");
        } catch (Exception e) {
            assertThat(e.getClass().getName(), is(InvalidInputException.class.getName()));
        }
    }
}
