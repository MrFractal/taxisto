package ru.trendtech.utils.test;

/**
 * Created by petr on 21.11.2014.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.*;

class BeeperControl {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeeperControl.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
         beepForAnHour();
    }



    public static void beepForAnHour() throws ExecutionException, InterruptedException {
//        final Runnable beeper = new Runnable() {
//            public void run() {
//                LOGGER.info("beep name thread: " + Thread.currentThread().getName());
//            }
//        };


    for(int i=0;i<3;i++){
        //System.out.println("ddddd");
        //FindDrivers findDrivers = new FindDrivers("Найти mission_id = "+i);
        //scheduler.schedule(findDrivers, 10, TimeUnit.SECONDS);
        //scheduler.scheduleAtFixedRate(findDrivers, 1, 1, SECONDS);

        //Future future = scheduler.submit(findDrivers);

        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(null, 1, 1, SECONDS); // findDrivers
        System.out.println("get: " + beeperHandle.get());

        scheduler.schedule(new Runnable() {
            public void run() {
                beeperHandle.cancel(true);
                LOGGER.info("cancel");
            }
        }, 1 * 10, SECONDS);
    }

       // stp.scheduleAtFixedRate(new HandlerA(), 0, 10, TimeUnit.SECONDS);

//     final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 1, 1, SECONDS);

//        scheduler.schedule(new Runnable() {
//            public void run() {
//                beeperHandle.cancel(true);
//                LOGGER.info("cancel");
//            }
//        }, 1 * 10, SECONDS);


        scheduler.shutdown();
        while(!scheduler.isTerminated()){
            //wait for all tasks to finish
            //System.out.println("wait for all tasks to finish");
        }
        System.out.println("Finished all threads");

    }
}