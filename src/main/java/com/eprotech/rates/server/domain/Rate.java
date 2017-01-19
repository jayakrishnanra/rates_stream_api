package com.eprotech.rates.server.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * Created by mercury on 21-Aug-16.
 */
public class Rate {
    private final String ccyPair;
    private final BigDecimal bidPrice;
    private final BigDecimal askPrice;
    private final LocalDateTime lastUpdatedTime;

    public Rate(String ccyPair, BigDecimal bidPrice, BigDecimal askPrice) {
        this.ccyPair = ccyPair;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.lastUpdatedTime = LocalDateTime.now();
    }

    public Rate(String ccyPair, BigDecimal bidPrice, BigDecimal askPrice, LocalDateTime lastUpdatedTime) {
        this.ccyPair = ccyPair;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getCcyPair() {
        return ccyPair;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public LocalDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "ccyPair='" + ccyPair + '\'' +
                ", bidPrice=" + bidPrice +
                ", askPrice=" + askPrice +
                ", lastUpdatedTime=" + lastUpdatedTime +
                '}';
    }
}
