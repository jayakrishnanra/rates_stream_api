package com.eprotech.rates.server.service.rates;

import com.eprotech.rates.server.properties.PropertyLoader;
import com.eprotech.rates.server.service.SubscriptionMonitorTask;
import com.eprotech.rates.server.store.RateStore;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RateSubscriptionTask extends SubscriptionMonitorTask {
    private static final Logger logger = LoggerFactory.getLogger(RateSubscriptionTask.class);
    private static final DateTimeFormatter ISO_DATE_TIME = DateTimeFormatter.ISO_DATE_TIME;
    private final List<String> ccyPairs;
    private volatile RateStore rateStore;

    public RateSubscriptionTask(List<String> ccyPairs, RateStore rateStore) {
        this.ccyPairs = ccyPairs;
        this.rateStore = rateStore;
    }

    @Override
    public RateSubscriptionTask clone() throws CloneNotSupportedException {
        return new RateSubscriptionTask(ccyPairs, rateStore);
    }

    @Override
    public void run() {
        System.out.println("run: RateSubscriptionTask " + Thread.currentThread().getName());
        CloseableHttpResponse response = null;
        try {
            int connectTimeout = 30000;
            final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(connectTimeout).build();
            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

            String domain = PropertyLoader.getInstance().getDomainUrl();
            String access_token = PropertyLoader.getInstance().getAccess_token();
            String account_id = PropertyLoader.getInstance().getAccount_id();
            String instruments = ccyPairs.stream().collect(Collectors.joining(","));

            HttpUriRequest httpGet = new HttpGet(domain + "/v1/prices?accountId=" + account_id + "&instruments=" + instruments);
            httpGet.setHeader(new BasicHeader("Authorization", "Bearer " + access_token));

            logger.info("Subscription request:- " + httpGet.getRequestLine());

            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
                InputStream stream = entity.getContent();

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                //while ((line = br.readLine()) != null && isForceStopped.get()) {
                while ((line = br.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                    //Keep reading data from the stream!
                    Object obj = JSONValue.parse(line);
                    JSONObject data = (JSONObject) obj;

                    logger.debug(data.toJSONString());

                    // unwrap if necessary
                    if (data.containsKey("tick")) {
                        JSONObject tick = (JSONObject) data.get("tick");

                        // ignore heartbeats
                        if (tick.containsKey("instrument")) {

                            String instrument = tick.get("instrument").toString();
                            //TODO parse time and set on rate!!
                            String time = tick.get("time").toString();
                            double bid = Double.parseDouble(tick.get("bid").toString());
                            double ask = Double.parseDouble(tick.get("ask").toString());

                            rateStore.put(instrument, BigDecimal.valueOf(bid), BigDecimal.valueOf(ask), LocalDateTime.parse(time, ISO_DATE_TIME));
                        }
                        if (tick.containsKey("heartbeat")) {
                            throw new RuntimeException("Failure to handle one scenario!!!");
                        }
                    }

                    if (data.containsKey("heartbeat")) {
                        JSONObject heartbeat = (JSONObject) data.get("heartbeat");
                        String time = heartbeat.get("time").toString();
                        LocalDateTime dateTime = LocalDateTime.parse(time, ISO_DATE_TIME);
                        //Keep track of heartbeat to allow to reset connection
                        if (!heartBeatStarted.get()) {
                            heartBeatStarted.set(true);
                        }

                        // Not sure if the heart beat time is accurate, relying on System time
                        heartBeatTracker.set(System.currentTimeMillis());
                        //heartBeatTracker.updateAndGet(operand -> dateTime.toInstant(ZoneOffset.UTC).toEpochMilli() - operand);
                    }
                }
            } else {
                // print error message
                if (entity != null) {
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    logger.error("Unexpected response recieved {}", responseString);
                }
            }
        } catch (Exception e) {
            logger.error("Failure to continue subscription ", e);
        } finally {
            isSubscriptionActive.set(false);
            try {
                if (response != null) {
                    logger.info("Closing http connection, interrupted status - {}", Thread.currentThread().isInterrupted());
                    response.close();
                }
            } catch (IOException ex) {
                logger.error("Failed to close the connection.", ex);
            }
        }
    }


}