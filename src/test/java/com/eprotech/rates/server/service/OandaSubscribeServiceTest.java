package com.eprotech.rates.server.service;

import com.eprotech.rates.server.store.RateStore;
import org.junit.Test;

import java.util.Arrays;

/**
 *
 * Created by mercury on 21-Aug-16.
 */
public class OandaSubscribeServiceTest {

    @Test
    public void testSubscribe() throws Exception {
        RateStore rateStore = new RateStore();
        OandaSubscribeService service = new OandaSubscribeService(rateStore);
        service.subscribe(Arrays.asList("GBP_USD,EUR_USD"));
    }
}