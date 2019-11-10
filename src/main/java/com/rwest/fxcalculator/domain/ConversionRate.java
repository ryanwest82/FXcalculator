package com.rwest.fxcalculator.domain;

import java.math.BigDecimal;

/**
 * Represents a conversion rate between a base currency and a terms currency
 */
public final class ConversionRate {

    private final Currency base;

    private final Currency terms;

    private final BigDecimal rate;

    private final Currency crossVia;

    private final ConversionRateType type;

    private ConversionRate(Currency base, Currency terms, Currency via, BigDecimal rate, ConversionRateType type) {
        this.base = base;
        this.terms = terms;
        this.rate = rate;
        this.crossVia = via;
        this.type = type;
    }

    public ConversionRate(Currency base, Currency terms, BigDecimal rate) {
        this(base, terms, null, rate, ConversionRateType.DIRECT);
    }

    public ConversionRate(Currency base, Currency terms, Currency via) {
        this(base, terms, via, null, ConversionRateType.CROSS_VIA);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConversionRate that = (ConversionRate) o;

        if (base != null ? !base.equals(that.base) : that.base != null) return false;
        return terms != null ? terms.equals(that.terms) : that.terms == null;
    }

    @Override
    public int hashCode() {
        int result = base != null ? base.hashCode() : 0;
        result = 37 * result + (terms != null ? terms.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return base.getName() + terms.getName() + "=" + (rate==null ? crossVia.getName() : rate.toString());
    }

    public Currency getBase() {
        return base;
    }

    public Currency getTerms() {
        return terms;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public Currency getCrossVia() {
        return crossVia;
    }

    public ConversionRateType getType() {
        return type;
    }
}
