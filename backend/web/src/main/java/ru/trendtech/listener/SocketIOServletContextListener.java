package ru.trendtech.listener;

/**
 * Created by petr on 13.08.14.
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.notifications.node.NodeJsService;
import ru.trendtech.services.search.ServiceAutoSearch;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class SocketIOServletContextListener implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketIOServletContextListener.class);

//    @Autowired
//    ThreadPoolTaskExecutor executor;


    @Autowired
    private CommonService commonService;

    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        LOGGER.info("CONTEXT DESTROYED");
        NodeJsService.destroy();
        ServiceAutoSearch.destroyScheduler();
        //TurboMissionService.destroyScheduler();

        scheduler.shutdown();
        //executor.shutdown();
    }



    //Run this before web application is started
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        LOGGER.info("ServletContextListener started");
    }
}
