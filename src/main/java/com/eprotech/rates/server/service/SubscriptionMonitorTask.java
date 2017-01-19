package com.eprotech.rates.server.service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by mercury on 24-Aug-16.
 */
public abstract class SubscriptionMonitorTask implements Runnable, Cloneable {

    private static final long HEARTBEAT_TIMEOUT = 30000;//30 seconds

    // Atomic variables to track heartbeat
    protected AtomicLong heartBeatTracker = new AtomicLong(System.currentTimeMillis());
    protected AtomicBoolean heartBeatStarted = new AtomicBoolean(false);

    // Is the subscription currently active - marked false when complete
    protected AtomicBoolean isSubscriptionActive = new AtomicBoolean(true);
    // Not required - Checking isInterrupted was sufficient to exit loop
    protected AtomicBoolean isForceStopped = new AtomicBoolean(false);

    public void setIsForceStopped(boolean forceStoped) {
        this.isForceStopped.set(forceStoped);
    }

    public boolean getIsForceStopped() {
        return isForceStopped.get();
    }

    /**
     * subscription should not have been marked inactive
     * heartbeat should have started
     * then check time out as well
     *
     * @return
     */
    public boolean isServiceActive() {
        return isSubscriptionActive.get();
    }

    public boolean isHeartBeatStarted() {
        return heartBeatStarted.get();
    }

    public boolean isTimedOut() {
        long elapsed = System.currentTimeMillis() - heartBeatTracker.get();
        System.out.println("isTimedOut "+elapsed);
        return elapsed >= HEARTBEAT_TIMEOUT;
    }

    @Override
    public SubscriptionMonitorTask clone() throws CloneNotSupportedException {
        return null;
    }
}
