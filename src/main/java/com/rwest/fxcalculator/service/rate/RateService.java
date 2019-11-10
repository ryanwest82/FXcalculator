package com.rwest.fxcalculator.service.rate;

import com.rwest.fxcalculator.domain.ConversionRate;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;
import com.rwest.fxcalculator.exceptions.RateRefreshException;
import com.rwest.fxcalculator.service.currency.CurrencyLookup;

import java.util.Set;

/**
 * Abstract class for a Rate providing service. The concrete implementation could gather it's information from any
 * source, i.e file, database, api etc
 */
public abstract class RateService {

    final CurrencyLookup currencyLookup;

    RateService(CurrencyLookup currencyLookup) {
        this.currencyLookup = currencyLookup;
    }

    abstract Set<ConversionRate> getRates() throws RateRefreshException, CurrencyNotFoundException;

    CurrencyLookup getCurrencyLookup() {
        return currencyLookup;
    }
}
