package com.eprotech.rates.server.service;

import com.eprotech.rates.server.domain.Rate;
import com.eprotech.rates.server.store.RateStore;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by mercury on 21-Aug-16.
 */
public class RateStoreTest {

    @Test
    public void put() {
        RateStore rateStore = new RateStore();
        String ccyPair = "EUR_GBP";

        BigDecimal ask1 = new BigDecimal(18.125);
        BigDecimal bid1 = new BigDecimal(18.125);

        rateStore.put(ccyPair, bid1, ask1);
        Rate rate = rateStore.get(ccyPair);
        assertEquals(ask1, rate.getBidPrice());

        BigDecimal bid2 = new BigDecimal(123.124);
        BigDecimal ask2 = new BigDecimal(123.124);
        rateStore.put(ccyPair, bid2, ask2);
        Rate rate2 = rateStore.get(ccyPair);
        assertEquals(bid2, rate2.getBidPrice());

    }

}