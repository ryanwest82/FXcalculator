package com.rwest.fxcalculator.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *  This class reads configurable properties from a predefined local file and makes those properties available
 *  for application usage
 */
public final class ApplicationConfig {

    private static final String REFRESH_PROPERTY_KEY = "rate.refresh.interval";

    private static final String CURRENCY_FILE_PATH_KEY = "paths.currency.file";

    private static final String RATE_FILE_PATH_KEY = "paths.rates.file";

    private static long refreshInterval;

    private static String currencyFilePath;

    private static String rateFilePath;

    public static void loadConfig() throws IOException {

        try (InputStream input = ApplicationConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            Properties props = new Properties();
            props.load(input);

            refreshInterval = Long.valueOf(props.getProperty(REFRESH_PROPERTY_KEY));
            currencyFilePath = props.getProperty(CURRENCY_FILE_PATH_KEY);
            rateFilePath = props.getProperty(RATE_FILE_PATH_KEY);
        }
    }

    public static long getRefreshInterval() {
        return refreshInterval;
    }

    public static String getCurrencyFilePath() {
        return currencyFilePath;
    }

    public static String getRateFilePath() {
        return rateFilePath;
    }
}
