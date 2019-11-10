package com.rwest.fxcalculator.service;

import com.rwest.fxcalculator.domain.ConversionRequest;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;
import com.rwest.fxcalculator.exceptions.InvalidInputException;

/**
 * Interface for Input Parser
 */
public interface InputParser {

    ConversionRequest parse(String input) throws CurrencyNotFoundException, InvalidInputException;

}
