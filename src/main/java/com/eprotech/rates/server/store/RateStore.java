package com.eprotech.rates.server.store;

import com.eprotech.rates.server.domain.Rate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by mercury on 21-Aug-16.
 */
public class RateStore {
    private ConcurrentMap<String, Rate> store = new ConcurrentHashMap<>();

    @Deprecated
    public void put(String ccyPair, BigDecimal bidPrice, BigDecimal askPrice) {
        store.put(ccyPair, new Rate(ccyPair, bidPrice, askPrice));
    }

    public void put(String ccyPair, BigDecimal bidPrice, BigDecimal askPrice, LocalDateTime updatedTime) {
        store.put(ccyPair, new Rate(ccyPair, bidPrice, askPrice, updatedTime));
    }

    public Rate get(String ccyPair) {
        return store.get(ccyPair);
    }

    public void markStale() {
        //TODO
    }
}
