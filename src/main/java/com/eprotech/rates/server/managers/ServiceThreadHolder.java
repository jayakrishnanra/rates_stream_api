package com.eprotech.rates.server.managers;

import com.eprotech.rates.server.service.InterruptionHandler;
import com.eprotech.rates.server.service.SubscriptionMonitorTask;

/**
 * Created by mercury on 22-Aug-16.
 */
public class ServiceThreadHolder {
    private SubscriptionMonitorTask subscriptionMonitorTask;
    private final InterruptionHandler interruptionHandler;
    private Thread serviceProcessorThread;

    public ServiceThreadHolder(SubscriptionMonitorTask subscriptionMonitorTask, InterruptionHandler interruptionHandler) {
        this.subscriptionMonitorTask = subscriptionMonitorTask;
        this.interruptionHandler = interruptionHandler;
    }

    public SubscriptionMonitorTask getSubscriptionMonitorTask() {
        return subscriptionMonitorTask;
    }

    public InterruptionHandler getInterruptionHandler() {
        return interruptionHandler;
    }

    public Thread getServiceProcessorThread() {
        return serviceProcessorThread;
    }

    public void setServiceProcessorThread(Thread serviceProcessorThread) {
        this.serviceProcessorThread = serviceProcessorThread;
    }

    public void setSubscriptionMonitorTask(SubscriptionMonitorTask subscriptionMonitorTask) {
        this.subscriptionMonitorTask = subscriptionMonitorTask;
    }
}
