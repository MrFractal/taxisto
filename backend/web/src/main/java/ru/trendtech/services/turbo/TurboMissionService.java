package ru.trendtech.services.turbo;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.trendtech.domain.Mission;
import ru.trendtech.repositories.MissionRepository;
import ru.trendtech.services.notifications.node.NodeJsService;
import ru.trendtech.services.administration.AdministrationService;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by petr on 16.12.2014.
 */

@Service
public class TurboMissionService {
    @Autowired
    AdministrationService administrationService;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    private NodeJsService nodeJsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TurboMissionService.class);
    private static ScheduledExecutorService schedulerTurbo = Executors.newScheduledThreadPool(1);
    private HashMap mapMission = new HashMap();




    public void addThreadTurboOrder(ScheduledFuture scheduledFuture, long missionId){
        ScheduledFuture scheduled = (ScheduledFuture)mapMission.get(missionId);
        if(scheduled==null){
            LOGGER.info("Нет потока связанной с данной миссией, стартуем его");
            mapMission.put(missionId, scheduledFuture);
        }else{
            // уже крутится поток с данной миссией, значит убиваем его и стартуем заново
            LOGGER.info("Есть поток связанный с данной миссией, начинаем поиск заново");
            //stopTurboOrder(missionId);
        }
    }



    public void removeThreadTurboOrder(long missionId){
        mapMission.remove(missionId);
    }



    // когда поменялся статус миссии, просто тушим поток
    public void stopTurboOrder(long missionId){
        ScheduledFuture<TurboMission> cancelHandle = (ScheduledFuture)mapMission.get(missionId);
        cancelHandle.cancel(true);
    }


    // когда поменялся статус миссии, просто тушим поток
    public void cancelTurboOrder(long missionId){
        LOGGER.info("mapMission size = "+mapMission.size());
        ScheduledFuture<TurboMission> cancelHandle = (ScheduledFuture)mapMission.get(missionId);
        cancelHandle.cancel(true);
        removeThreadTurboOrder(missionId);
    }


    // когда пользователь отменяет поиск руками
    public void stopTurboAndCancelMission(long missionId){
        ScheduledFuture<TurboMission> cancelHandle = (ScheduledFuture)mapMission.get(missionId);
        cancelHandle.cancel(true);
        Mission mission = missionRepository.findOne(missionId);
        mission.setState(Mission.State.CANCELED);
        missionRepository.save(mission);
        removeThreadTurboOrder(missionId);

        JSONObject json = new JSONObject();
        try {
            json.put("missionId", missionId);
            nodeJsService.notified("turbo_canceled",json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void  destroyScheduler(){
        LOGGER.info("SHUT DOWN TurboMissionService!!!");
        if(!schedulerTurbo.isShutdown()){
            schedulerTurbo.shutdownNow();
        }
    }


    public void stopThreadIfExist(long missionId){
        ScheduledFuture scheduled = (ScheduledFuture)mapMission.get(missionId);
           if(scheduled!=null){
               scheduled.cancel(true);
           }
    }







    public void startTurbo(final TurboMission turboMission) {
        try {
             /*
             перед тем как добавлять в мапу для поиска в течении 10 минут, проверить, есть ли уже в мапе заказ с текущим id
             если есть - потушить поток, начать отсчет заново в новом потоке
             если нет - просто стартуем поиск водил
             */

            ScheduledFuture scheduled = (ScheduledFuture)mapMission.get(turboMission.getMission().getId());
            if(scheduled!=null){
                scheduled.cancel(true);
                removeThreadTurboOrder(turboMission.getMission().getId());
            }

            final ScheduledFuture<?> autoSerachHandle = schedulerTurbo.scheduleAtFixedRate(turboMission, 3, 1, SECONDS);
            turboMission.setScheduledFuture(autoSerachHandle);

            addThreadTurboOrder(autoSerachHandle, turboMission.getMission().getId());

            schedulerTurbo.schedule(new Runnable() {
                public void run() {
                    // прошло 10 минут на миссию так никто и не откликнулся, тушим поток, отменяем миссию
                    autoSerachHandle.cancel(true);
                    administrationService.cancelMission(false, turboMission.getMission());
                    removeThreadTurboOrder(turboMission.getMission().getId());

                    JSONObject json = new JSONObject();
                    try {
                        json.put("missionId", turboMission.getMission().getId());
                        nodeJsService.notified("turbo_canceled",json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, 600, SECONDS);
        } catch (Exception h) {
            h.printStackTrace();
        }
    }
}


