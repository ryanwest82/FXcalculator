package com.rwest.fxcalculator.service.rate;

import com.rwest.fxcalculator.domain.ConversionRate;
import com.rwest.fxcalculator.domain.ConversionRateType;
import com.rwest.fxcalculator.domain.Currency;
import com.rwest.fxcalculator.exceptions.CurrencyNotFoundException;
import com.rwest.fxcalculator.exceptions.RateRefreshException;
import com.rwest.fxcalculator.service.currency.CurrencyLookup;
import com.rwest.fxcalculator.util.RateInverter;
import com.rwest.fxcalculator.util.RefreshCalculator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Rate Lookup Implementation - relies upon an implementation of the RateService to gather current rate information.
 * Provides functionality to refresh the available rates data and to find an applicable rate based upon
 * input base and terms currencies.
 */
public class RateLookupImpl implements RateLookup {

    private static final Logger LOGGER = Logger.getLogger(RateLookupImpl.class.getName());

    private final RateService rateService;

    private Map<String, ConversionRate> rates;

    private LocalDateTime lastRefresh;

    public RateLookupImpl(RateService rateService) throws RateRefreshException, CurrencyNotFoundException {
        this.rateService = rateService;
        refreshRates();
    }

    @Override
    public Optional<ConversionRate> findRate(Currency from, Currency to) throws CurrencyNotFoundException {
        if (RefreshCalculator.isRefreshDue(lastRefresh)) {
            try {
                refreshRates();
            } catch (RateRefreshException e) {
                LOGGER.warning("Rate refresh failed, quoted rates may be outdated");
            }
        }

        ConversionRate conversionRate = rates.get(generateKey(from, to));

        if (conversionRate != null && conversionRate.getType() == ConversionRateType.CROSS_VIA)  {
            return findRate(from, conversionRate.getCrossVia());
        } else {
            return Optional.ofNullable(conversionRate);
        }
    }

    @Override
    public List<ConversionRate> getRatesList() {
        return new ArrayList<>(rates.values());
    }

    private void refreshRates() throws RateRefreshException, CurrencyNotFoundException {
        Set<ConversionRate> rawConversionRateSet = rateService.getRates();
        Map<String, ConversionRate> conversionRates = new HashMap<>();

        // Generate Inverse Rates
        Set<ConversionRate> invertedRates = new HashSet<>();
        for (ConversionRate rate : rawConversionRateSet) {
            if (rate.getType() == ConversionRateType.DIRECT) {
                invertedRates.add(
                        new ConversionRate(rate.getTerms(), rate.getBase(),
                                RateInverter.invert(rate.getRate(), rate.getRate().scale())));
            } else if (rate.getType() == ConversionRateType.CROSS_VIA) {
                invertedRates.add(new ConversionRate(rate.getTerms(), rate.getBase(), rate.getCrossVia()));
            }
        }
        rawConversionRateSet.addAll(invertedRates);

        // Generate Unity Rates based upon available currencies
        CurrencyLookup currencyLookup = rateService.getCurrencyLookup();
        List<Currency> currencyList = currencyLookup.getCurrencyList();
        Set<ConversionRate> unityRates = new HashSet<>();

        for  (Currency c : currencyList) {
            unityRates.add(new ConversionRate(c, c, BigDecimal.ONE));
        }
        rawConversionRateSet.addAll(unityRates);

        rawConversionRateSet.forEach(c -> conversionRates.put(generateKey(c.getBase(), c.getTerms()), c));

        /* TODO If the default rate refresh interval is reduced to provide near real-time rates, it will be worth
            reusing & updating the existing map contents rather than an unmodifiable map, for potential performance
            improvments */
        this.rates = Collections.unmodifiableMap(conversionRates);
        lastRefresh = LocalDateTime.now();

        LOGGER.info("Rates have been refreshed");
    }

    private String generateKey(Currency from, Currency to) {
        return from.getName() + "/" + to.getName();
    }
}
