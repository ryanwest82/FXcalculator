package com.rwest.fxcalculator.service.currency;

import com.rwest.fxcalculator.domain.Currency;

import java.util.List;
import java.util.Optional;

/**
 * Currency Lookup Interface
 */
public interface CurrencyLookup {

    Optional<Currency> findCurrency(String currencyName);

    List<Currency> getCurrencyList();

}
