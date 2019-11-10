package com.rwest.fxcalculator.util;

import com.rwest.fxcalculator.config.ApplicationConfig;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Utility Class - Includes method to determine if a data refresh is due
 */
public final class RefreshCalculator {

    public static boolean isRefreshDue(LocalDateTime lastRefresh) {
        return ChronoUnit.MILLIS.between(lastRefresh, LocalDateTime.now()) > ApplicationConfig.getRefreshInterval();
    }
}
