package com.eprotech.rates.server.service;

import com.eprotech.rates.server.managers.ConnectionManagerThread;
import com.eprotech.rates.server.managers.ServiceThreadHolder;
import com.eprotech.rates.server.service.enums.ConnectionType;
import com.eprotech.rates.server.service.rates.RateSubscriptionTask;
import com.eprotech.rates.server.store.RateStore;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created by mercury on 21-Aug-16.
 */
public class OandaSubscribeService implements SubscribeService {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(OandaSubscribeService.class);

    private volatile RateStore rateStore;
    private volatile ConnectionManagerThread connectionManager = new ConnectionManagerThread();

    public OandaSubscribeService(RateStore rateStore) {
        this.rateStore = rateStore;
    }

    @Override
    public void subscribe(List<String> ccyPairs) throws Exception {
        logger.info("subscribe {}",ccyPairs);
        // If no heartbeat for a timeout period, then re-subscribe -
        //new RateSubscriptionTask(ccyPairs, rateStore).start();

        //Register Thread and Handler to connection manager
        connectionManager.register(ConnectionType.RATES,
                new ServiceThreadHolder(new RateSubscriptionTask(ccyPairs, rateStore), () -> rateStore.markStale()));

    }


}




