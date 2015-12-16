package ru.trendtech.services.turbo;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoaderListener;
import ru.trendtech.domain.Mission;
import ru.trendtech.repositories.MissionRepository;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.search.ServiceAutoSearch;

import java.util.concurrent.ScheduledFuture;

/**
 * Created by petr on 16.12.2014.
 */

@Service
public class TurboMission implements Runnable{

    ApplicationContext ctx = ContextLoaderListener.getCurrentWebApplicationContext();

    @Autowired
    AdministrationService administrationService;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    TurboMissionService turboMissionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TurboMission.class);
    private double radius = 0;
    private int priority;
    private int timeSec = 0;
    private Mission mission;
    private ScheduledFuture scheduledFuture;
    private int turboElapsedTime=0;

    public int getTurboElapsedTime() {
        return turboElapsedTime;
    }

    public void setTurboElapsedTime(int turboElapsedTime) {
        this.turboElapsedTime = turboElapsedTime;
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

    private TurboMissionService getTurboMissionService() {
        if (turboMissionService==null) {
            //ApplicationContext ctx = ApplicationContextUtils.getInstance();
            turboMissionService = ctx.getBean(TurboMissionService.class);
        }
        return turboMissionService;
    }


    public boolean isStateMissionChange(long missionId){
        boolean result = false;
        try {
            missionRepository = getMissionRepository();
            Mission mission = missionRepository.findOne(missionId);
            if (!mission.getState().equals(Mission.State.TURBO)) {
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
            LOGGER.info("Ищу водителя для миссии: " + getMission().getId() + " state = " + getMission().getState() + " timeSec =" + timeSec+" Thread.currentThread().getName() = "+Thread.currentThread().getName());

            boolean isStateMissionChange = isStateMissionChange(getMission().getId());
            //LOGGER.info("mission id=" + mission.getId() + " state=" + mission.getState() + " isStateMissionChange=" + isStateMissionChange);

            if (!isStateMissionChange) {
                administrationService = getAdministrationService();
                JSONObject json = new JSONObject();
                json.put("missionId", getMission().getId());
                JSONObject loc = new JSONObject();
                loc.put("latitude", getMission().getLocationFrom().getLatitude());
                loc.put("longitude", getMission().getLocationFrom().getLongitude());
                json.put("location", loc);

                timeSec++;

                // приоритет - 2
                priority = 2;
                if (timeSec > 0 && timeSec <= 5) {
                } else if (timeSec > 5 && timeSec <= 10) {
                    radius = 7;
                } else if (timeSec > 10 && timeSec <= 15) {
                    radius = 9;
                } else if (timeSec > 15 && timeSec <= 20) {
                    radius = 12;
                } else if (timeSec > 20 && timeSec <= 23) {
                    radius = 15;
                } else if (timeSec > 23 && timeSec <= 26) {
                    radius = 15;
                } else if (timeSec > 26 && timeSec <= 30) {
                    radius = 15;
                }
                json.put("count", timeSec);
                json.put("radius", radius);

                //administrationService.notified("find_drivers", json);

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
                    radius = 10;
                } else if (timeSec > 26 && timeSec <= 30) {
                    radius = 15;
                }
                json.put("count", timeSec);
                json.put("radius", radius);

                LOGGER.info("count = "+timeSec+" radius = "+radius);

                //administrationService.notified("find_drivers", json);

            } else {
                LOGGER.info("МИССИЮ ВЗЯЛ ВОДИТЕЛЬ. ЗНАЧИТ ОТМЕЯЕМ ТУРБО ПОИСК");
                turboMissionService = getTurboMissionService();
                //turboMissionService.cancelTurboOrder(getMission().getId());
            }
        }catch(Exception h){
            LOGGER.warn("AutoSearch exception in run methos: "+h.getMessage());
            h.printStackTrace();
        }
    }
}
