package com.eprotech.rates.server.managers;

import com.eprotech.rates.server.service.SubscriptionMonitorTask;
import com.eprotech.rates.server.service.enums.ConnectionType;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Monitors and maintains the connection as needed
 * Created by mercury on 22-Aug-16.
 */
public class ConnectionManagerThread extends Thread {

    private ConcurrentMap<ConnectionType, ServiceThreadHolder> map = new ConcurrentHashMap<>();

    public ConnectionManagerThread() {
        System.out.println("ConnectionManagerThread: started " + Thread.currentThread().getName());
        this.start();
    }

    public void register(ConnectionType type, ServiceThreadHolder holder) {
        System.out.println("register: " + type);
        map.put(type, holder);
        interrupt();
    }

    @Override
    public void run() {
        System.out.println("run: ConnectionManagerThread " + Thread.currentThread().getName());

        while (true) {
            try {
                Collection<ServiceThreadHolder> serviceThreadHolders = map.values();
                for (ServiceThreadHolder serviceHolder : serviceThreadHolders) {

                    // for each running thread check heartBeat monitor
                    SubscriptionMonitorTask task = serviceHolder.getSubscriptionMonitorTask();
                    if (serviceHolder.getServiceProcessorThread() != null
                            && ((task.isHeartBeatStarted() && task.isTimedOut()) || !task.isServiceActive())) {
                        serviceHolder.getServiceProcessorThread().interrupt();//Interrupt the thread and also stop manually in case interrupt fails
                        task.setIsForceStopped(true);

                        //Prepare for next run and in the process make it eligible for garbage collection
                        serviceHolder.setSubscriptionMonitorTask(task.clone());
                        serviceHolder.setServiceProcessorThread(null);
                    }

                    //check thread
                    if (serviceHolder.getServiceProcessorThread() == null) {
                        serviceHolder.setServiceProcessorThread(new Thread(serviceHolder.getSubscriptionMonitorTask()));
                        serviceHolder.getServiceProcessorThread().start();
                    }
                }

                //check every one minute
                int pause = 10;
                System.out.println("Sleeping connection manager for " + pause);
                Thread.sleep(TimeUnit.SECONDS.toMillis(pause));
            } catch (InterruptedException e) {
                System.out.println("Thread sleep interrupted(don't care, could be a trigger to restart - " + e);
                //clear the interrupt status as the job hasn't completed
                interrupted();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }
}
