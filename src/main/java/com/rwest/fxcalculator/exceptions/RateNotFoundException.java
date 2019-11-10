package com.rwest.fxcalculator.exceptions;

public class RateNotFoundException extends Exception {

    public RateNotFoundException(String from, String to) {
        super(String.format("Unable to find rate for %s/%s", from, to));
    }
}
