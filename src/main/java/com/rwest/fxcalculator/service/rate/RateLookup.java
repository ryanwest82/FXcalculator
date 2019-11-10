package com.rwest.fxcalculator.service.rate;

import com.rwest.fxcalculator.domain.ConversionRate;
import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Interface for Rate Lookup
 */
public interface RateLookup {

    Optional<ConversionRate> findRate(Currency from, Currency to) throws CurrencyNotFoundException;

    List<ConversionRate> getRatesList();

}
