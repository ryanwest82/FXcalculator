package com.rwest.fxcalculator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility Class - Functionality to invert direct rates
 */
public final class RateInverter {

    private static final int DEFAULT_SCALE = 3;

    public static BigDecimal invert(BigDecimal inputRate, int scale) {
        return BigDecimal.ONE.divide(inputRate, scale != 0 ? scale : DEFAULT_SCALE, RoundingMode.HALF_DOWN);
    }
}
