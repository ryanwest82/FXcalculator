package com.rwest.fxcalculator.service.currency;

import com.rwest.fxcalculator.domain.ConversionRequest;
import com.rwest.fxcalculator.domain.ConversionResult;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;
import com.rwest.fxcalculator.exceptions.RateNotFoundException;

/**
 * Currency Converter interface
 */
public interface CurrencyConverter {

    ConversionResult convert(ConversionRequest request) throws RateNotFoundException, CurrencyNotFoundException;

}
