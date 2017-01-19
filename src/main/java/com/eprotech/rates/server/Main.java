package com.eprotech.rates.server;

import com.eprotech.rates.server.domain.Rate;
import com.eprotech.rates.server.properties.CcyPairs;
import com.eprotech.rates.server.service.OandaSubscribeService;
import com.eprotech.rates.server.service.SubscribeService;
import com.eprotech.rates.server.store.RateStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Expecting CCY pair formatting as CCY1_CCY2
 * Created by mercury on 18-Aug-16.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private RateStore rateStore = new RateStore();

    public static void main(String[] args) throws Exception {
        Main m = new Main();
        // m.check();
        m.start();
    }

    public void start() throws Exception {
        List<String> ccyPairs = getCcyPairs();
        System.out.println(ccyPairs);

        SubscribeService subscriber = new OandaSubscribeService(rateStore);
        subscriber.subscribe(ccyPairs);

        Thread t = new Thread(() -> {
            while (true) {
                logger.info("Reading from store" + rateStore.get(ccyPairs.get(0)));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }

    public List<String> getCcyPairs() throws Exception {
        return CcyPairs.getInstance().getCcyPairs();
    }

    public Rate getRate(String ccyPair){
        return rateStore.get(ccyPair);
    }

    /*private void check() {
        CyclicBarrier barrier = new CyclicBarrier(5);
        new Thread(getSubscriptionMonitorTask(barrier)).start();
        new Thread(getSubscriptionMonitorTask(barrier)).start();
        new Thread(getSubscriptionMonitorTask(barrier)).start();
        new Thread(getSubscriptionMonitorTask(barrier)).start();
        new Thread(getSubscriptionMonitorTask(barrier)).start();
    }

    private Runnable getSubscriptionMonitorTask(final CyclicBarrier barrier) {
        return () -> {
            System.out.println("Start waiting at barrier" + barrier.getNumberWaiting());
            try {
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Barrier wait complete");
            while (true) {
                List<String> ccyPairs = null;
                try {
                    //System.out.println(ccyPairs);
                    ccyPairs = CcyPairs.getInstance().getCcyPairs();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }*/

}
