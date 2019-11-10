package com.rwest.fxcalculator.exceptions;

public class CurrencyNotFoundException extends Exception {

    public CurrencyNotFoundException(String currencyName) {
        super(currencyName + " is not a recognised currency");
    }
}
