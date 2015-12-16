package ru.trendtech.services.search;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.trendtech.domain.Mission;
import ru.trendtech.repositories.MissionRepository;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.services.notifications.node.NodeJsService;
import ru.trendtech.services.administration.AdministrationService;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by petr on 03.12.2014.
 */

@Service
public class ServiceAutoSearch {
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAutoSearch.class);
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private HashMap mapMission = new HashMap();

    private void addThreadAutoSearch(ScheduledFuture scheduledFuture, long missionId){
         mapMission.put(missionId, scheduledFuture);
    }



    private void removeThreadAutoSearch(long missionId){
        mapMission.remove(missionId);
    }



    // когда поменялся статус миссии, просто тушим поток
    public void stopAutosearch(long missionId){
            ScheduledFuture<AutoSearchMission> cancelHandle = (ScheduledFuture)mapMission.get(missionId);
            if(cancelHandle != null) {
                cancelHandle.cancel(true);
                removeThreadAutoSearch(missionId);
            }
    }


    // когда пользователь отменяет поиск руками
    public void stopAutosearchAndCancelMission(long missionId){
        ScheduledFuture<AutoSearchMission> cancelHandle = (ScheduledFuture)mapMission.get(missionId);
        if(cancelHandle!=null){
            cancelHandle.cancel(true);
            removeThreadAutoSearch(missionId);
        }
        Mission mission = missionRepository.findOne(missionId);
        mission.setState(Mission.State.CANCELED);
        missionRepository.save(mission);

        nodeJsNotificationsService.autosearchCanceled(missionId);
    }

    /*
        JSONObject json = new JSONObject();
            try {
                json.put("missionId", missionId);
                nodeJsService.notified("autosearch_canceled", json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
     */





    public static void  destroyScheduler(){
        LOGGER.info("SHUT DOWN ServiceAutoSearch!!!");
        if(!scheduler.isShutdown()){
            scheduler.shutdownNow();
        }
    }




    public void startAutosearch(final AutoSearchMission autoSearchMission) {
        try {
             /*
             перед тем как добавлять в мапу для поиска в течении 10 минут, проверить, есть ли уже в мапе заказ с текущим id
             если есть - потушить поток, начать отсчет заново в новом потоке
             если нет - просто стартуем поиск водил
             */

            final ScheduledFuture<?> autoSerachHandle = scheduler.scheduleAtFixedRate(autoSearchMission, 3, 1, SECONDS);
            autoSearchMission.setScheduledFuture(autoSerachHandle);

            addThreadAutoSearch(autoSerachHandle, autoSearchMission.getMission().getId());

            scheduler.schedule(new Runnable() {
                public void run() {
                    // прошло 10 минут на миссию так никто и не откликнулся, тушим поток, отменяем миссию
                    autoSerachHandle.cancel(true);
                    Mission miss =  missionRepository.findOne(autoSearchMission.getMission().getId()); //  autoSearchMission.getMission()
                        if(miss.getState().equals(Mission.State.AUTO_SEARCH)){
                            administrationService.cancelMission(false, miss);
                            removeThreadAutoSearch(autoSearchMission.getMission().getId());

                            nodeJsNotificationsService.autosearchCanceled(autoSearchMission.getMission().getId());

                            /*
                            JSONObject json = new JSONObject();
                            try {
                                json.put("missionId", autoSearchMission.getMission().getId());
                                nodeJsService.notified("autosearch_canceled",json);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            */
                        }
                    /*
                       autosearch_canceled
                       data: {“missionId”: 23}
                    */

                }
            }, 600, SECONDS); // 600
        } catch (Exception h) {
            h.printStackTrace();
        }
    }
}

