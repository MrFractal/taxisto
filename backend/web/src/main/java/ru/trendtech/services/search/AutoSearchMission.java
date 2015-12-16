package ru.trendtech.services.search;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoaderListener;
import ru.trendtech.domain.Mission;
import ru.trendtech.repositories.MissionRepository;
import ru.trendtech.services.notifications.node.NodeJsService;
import ru.trendtech.services.administration.AdministrationService;


import java.util.concurrent.ScheduledFuture;

/**
 * Created by petr on 03.12.2014.
 */
@Service
public class AutoSearchMission implements Runnable{
    private ApplicationContext ctx = ContextLoaderListener.getCurrentWebApplicationContext();
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private ServiceAutoSearch autoSearchService;
    @Autowired
    private NodeJsService nodeJsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoSearchMission.class);
    private double radius = 1;
    private int priority;
    private int timeSec = 0;
    private Mission mission;
    private ScheduledFuture scheduledFuture;
    private int autoSearchElapsedTime=0;
    private int counter=0;

    public int getAutoSearchElapsedTime() {
        return autoSearchElapsedTime;
    }

    public void setAutoSearchElapsedTime(int autoSearchElapsedTime) {
        this.autoSearchElapsedTime = autoSearchElapsedTime;
    }

    public ScheduledFuture getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }


    private MissionRepository getMissionRepository() {
        try {
            if (missionRepository == null) {
                //ApplicationContext ctx = ApplicationContextUtils.getInstance();
                missionRepository = ctx.getBean(MissionRepository.class);
            }
        }catch(Exception r){
            r.printStackTrace();
        }
        return missionRepository;
    }



    private AdministrationService getAdministrationService() {
        if (administrationService==null) {
            //ApplicationContext ctx = ApplicationContextUtils.getInstance();
            administrationService = ctx.getBean(AdministrationService.class);
        }
        return administrationService;
    }


    private NodeJsService getNodeJsService() {
        if (nodeJsService ==null) {
            nodeJsService = ctx.getBean(NodeJsService.class);
        }
        return nodeJsService;
    }


    private ServiceAutoSearch getAutoSearchService() {
        if (autoSearchService==null) {
            //ApplicationContext ctx = ApplicationContextUtils.getInstance();
            autoSearchService = ctx.getBean(ServiceAutoSearch.class);
        }
        return autoSearchService;
    }




    public boolean isStateMissionChange(long missionId){
        boolean result = false;
        try {
            missionRepository = getMissionRepository();
            Mission mission = missionRepository.findOne(missionId);
            if (!mission.getState().equals(Mission.State.AUTO_SEARCH)) {
                result = true;
            }
        }catch(Exception h){
           h.printStackTrace();
        }
        return result;
    }



    @Override
    public void run() {
        try {
            boolean isStateMissionChange = isStateMissionChange(getMission().getId());

            if (!isStateMissionChange) {
                administrationService = getAdministrationService();
                nodeJsService = getNodeJsService();
                JSONObject json = new JSONObject();
                json.put("missionId", getMission().getId());
                JSONObject loc = new JSONObject();
                loc.put("latitude", getMission().getLocationFrom().getLatitude());
                loc.put("longitude", getMission().getLocationFrom().getLongitude());
                json.put("location", loc);

                timeSec++;
                if(timeSec>60){
                    timeSec=0;
                }

                // приоритет - 2
                priority = 2;
                if (timeSec > 0 && timeSec <= 5) {
                } else if (timeSec > 5 && timeSec <= 10) {
                    radius = 7;
                } else if (timeSec > 10 && timeSec <= 15) {
                    radius = 7; // 9
                } else if (timeSec > 15 && timeSec <= 20) {
                    radius = 7; // 12
                } else if (timeSec > 20 && timeSec <= 23) {
                    radius = 7;
                } else if (timeSec > 23 && timeSec <= 26) {
                    radius = 7;
                } else if (timeSec > 26 && timeSec <= 30) {
                    radius = 7;
                }
                json.put("count", timeSec);
                json.put("radius", radius);

                nodeJsService.notified("find_drivers", json);

                // приоритет - 1
                priority = 1;

                if (timeSec > 3 && timeSec <= 5) {
                    radius = 0.5;
                } else if (timeSec > 5 && timeSec <= 10) {
                    radius = 1;
                } else if (timeSec > 10 && timeSec <= 15) {
                    radius = 3;
                } else if (timeSec > 15 && timeSec <= 20) {
                    radius = 5;
                } else if (timeSec > 20 && timeSec <= 23) {
                    radius = 7;
                } else if (timeSec > 23 && timeSec <= 26) {
                    radius = 7; // 10
                } else if (timeSec > 26 && timeSec <= 30) {
                    radius = 7; // 15
                }
                json.put("count", timeSec);
                json.put("radius", radius);

                nodeJsService.notified("find_drivers", json);

            } else {
                LOGGER.info("МИССИЮ ВЗЯЛ ВОДИТЕЛЬ. ЗНАЧИТ ОТМЕЯЕМ АВТОПОИСК");
                autoSearchService = getAutoSearchService();
                autoSearchService.stopAutosearch(getMission().getId());
            }
        }catch(Exception h){
            LOGGER.warn("AutoSearch exception in run methos: "+h.getMessage());
            h.printStackTrace();
        }
    }
}
