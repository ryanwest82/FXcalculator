package com.rwest.fxcalculator.domain;

import java.math.BigDecimal;

/***
 * Represents the result of a conversion between a base currency and terms currency
 */
public final class ConversionResult {

    private final ConversionRequest request;

    private final BigDecimal result;

    public ConversionResult(ConversionRequest request, BigDecimal result) {
        this.request = request;
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format("%s %s = %s %s", request.getAmount(), request.getBase().getName(),
                result, request.getTerms().getName());
    }

    public BigDecimal getResult() {
        return result;
    }
}
