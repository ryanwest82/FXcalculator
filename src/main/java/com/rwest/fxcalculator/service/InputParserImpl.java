package com.rwest.fxcalculator.service;

import com.rwest.fxcalculator.domain.ConversionRequest;
import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;
import com.rwest.fxcalculator.exceptions.InvalidInputException;
import com.rwest.fxcalculator.service.currency.CurrencyLookup;

import java.math.BigDecimal;
import java.util.Optional;

/***
 * Implementation of Input parser, responsible for taking console input and ensuring it complies with
 * the format expected by the application
 */
public final class InputParserImpl implements InputParser {

    private static final String DELIMITER = "\\s+";

    private static final String PATTERN = "[A-Z]{3}\\s++\\d+(\\.\\d+)?\\s++in\\s++[A-Z]{3}";

    private CurrencyLookup currencyLookup;

    public InputParserImpl(CurrencyLookup currencyLookup) {
        this.currencyLookup = currencyLookup;
    }

    @Override
    public ConversionRequest parse(String input)
            throws CurrencyNotFoundException, InvalidInputException {

        if (input.matches(PATTERN)) {
            String[] tokens = input.split(DELIMITER);
            String fromName = tokens[0];
            String toName = tokens[3];

            Optional<Currency> from = currencyLookup.findCurrency(fromName);
            BigDecimal amount = new BigDecimal(tokens[1]);
            Optional<Currency> to = currencyLookup.findCurrency(toName);

            return new ConversionRequest(from.orElseThrow(() -> new CurrencyNotFoundException(fromName)),
                    to.orElseThrow(() -> new CurrencyNotFoundException(toName)), amount);

        } else {
            throw new InvalidInputException();
        }
    }
}
