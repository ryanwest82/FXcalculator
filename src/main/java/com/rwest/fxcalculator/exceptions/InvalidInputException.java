package com.rwest.fxcalculator.exceptions;

public class InvalidInputException extends Exception {

    public static final String FORMAT = "\nInput must be in the format of {CURRENCY} {AMOUNT} in {CURRENCY} \n" +
            "For example \"AUD 100 in GBP\"\n";

    public InvalidInputException() {
        super(FORMAT);
    }

}
