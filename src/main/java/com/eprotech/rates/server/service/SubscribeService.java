package com.eprotech.rates.server.service;

import java.io.IOException;
import java.util.List;

/**
 * Created by mercury on 21-Aug-16.
 */
public interface SubscribeService {
    void subscribe(List<String> ccyPairs) throws IOException, Exception;
}
