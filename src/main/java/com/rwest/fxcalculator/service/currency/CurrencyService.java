package com.rwest.fxcalculator.service.currency;

import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyRefreshException;

import java.util.Set;

/**
 * Interface for a Currency providing service. The implementation could gather it's information from any
 * source, i.e file, database, api etc
 */
public interface CurrencyService {

    Set<Currency> getCurrencies() throws CurrencyRefreshException;

}
