package com.rwest.fxcalculator.domain;

/***
 * Represents a currency
 */
public final class Currency {

    private final String name;

    private final Integer decimalPrecision;

    public Currency(String name, Integer decimalPlaces) {
        this.name = name;
        this.decimalPrecision = decimalPlaces;
    }

    @Override
    public String toString() {
        return name + " (" + decimalPrecision + " dp)";
    }

    public String getName() {
        return name;
    }

    public Integer getDecimalPrecision() {
        return decimalPrecision;
    }
}
