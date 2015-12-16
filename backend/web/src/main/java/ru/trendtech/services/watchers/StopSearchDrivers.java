package ru.trendtech.services.watchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.trendtech.services.driver.search.StartSearchDrivers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by petr on 29.04.2015.
 */

public class StopSearchDrivers implements Runnable {
    private int activeTime = 60;
    private static final Logger LOGGER = LoggerFactory.getLogger(StopSearchDrivers.class);
    private ScheduledFuture<StartSearchDrivers> searchDriverFuture;

    StopSearchDrivers(){
    }

    public StopSearchDrivers(ScheduledFuture<StartSearchDrivers> searchDriverFuture){
         this.searchDriverFuture = searchDriverFuture;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Попытка заканселить: " + searchDriverFuture.get().getName() + " counter = " + activeTime++);
            if(activeTime==60){
                LOGGER.info("Stop search for: " + searchDriverFuture.get().getName());
                searchDriverFuture.cancel(true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
