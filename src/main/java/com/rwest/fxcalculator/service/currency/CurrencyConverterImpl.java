package com.rwest.fxcalculator.service.currency;

import com.rwest.fxcalculator.domain.ConversionRate;
import com.rwest.fxcalculator.domain.ConversionRequest;
import com.rwest.fxcalculator.domain.ConversionResult;
import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;
import com.rwest.fxcalculator.exceptions.RateNotFoundException;
import com.rwest.fxcalculator.service.rate.RateLookup;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * Currency Converter implementation which utilises a Rate Lookup to calculate a conversion from base to terms currency
 * (which potentially gets routed via multiple 'CROSS_VIA' currencies along the way)
 */
public final class CurrencyConverterImpl implements CurrencyConverter {

    private final RateLookup rateLookup;

    public CurrencyConverterImpl(RateLookup rateLookup) {
        this.rateLookup = rateLookup;
    }

    @Override
    public ConversionResult convert(ConversionRequest request) throws RateNotFoundException, CurrencyNotFoundException {
        BigDecimal result = calculateResult(request.getBase(), request.getTerms(), request.getAmount())
                .setScale(request.getTerms().getDecimalPrecision(), RoundingMode.HALF_DOWN);
        return new ConversionResult(request, result);
    }

    /**
     * This method may be called recursively when the path from a base to a terms currency is required to
     * pass through one or more 'CROSS_VIA' currencies.
     */
    private BigDecimal calculateResult(Currency from, Currency to, BigDecimal amount)
            throws RateNotFoundException, CurrencyNotFoundException {
        Optional<ConversionRate> rateFound = rateLookup.findRate(from, to);

        ConversionRate conversionRate = rateFound
                .orElseThrow(() -> new RateNotFoundException(from.getName(), to.getName()));

        BigDecimal result = amount.multiply(conversionRate.getRate());

        // If the result is already in the requested terms currency then return it
        if (conversionRate.getTerms() == to) {
            return result;
        } else {
            return calculateResult(conversionRate.getTerms(), to, result);
        }
    }
}
