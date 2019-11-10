package com.rwest.fxcalculator.domain;

import java.math.BigDecimal;

/***
 * Represents a console request to convert from a base currency into a terms currency
 */
public final class ConversionRequest {

    private final Currency base;

    private final Currency terms;

    private final BigDecimal amount;

    public ConversionRequest(Currency base, Currency terms, BigDecimal amount) {
        this.base = base;
        this.terms = terms;
        this.amount = amount;
    }

    public Currency getBase() {
        return base;
    }

    public Currency getTerms() {
        return terms;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
