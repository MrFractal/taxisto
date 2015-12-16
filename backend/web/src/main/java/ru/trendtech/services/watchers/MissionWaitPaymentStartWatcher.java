package ru.trendtech.services.watchers;

import com.shephertz.app42.paas.sdk.java.util.Base64;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.Notification;
import ru.trendtech.domain.PushType;
import ru.trendtech.domain.billing.AutoClassPrice;
import ru.trendtech.domain.billing.MissionRate;
import ru.trendtech.repositories.MissionRepository;
import ru.trendtech.repositories.NotificationListRepository;
import ru.trendtech.repositories.billing.MissionRatesRepository;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.notifications.push.App42PushNotificationService;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.MissionRateUtils;

import java.util.List;

/**
 * Created by petr on 08.07.2015.
 */
@Service("missionWaitPaymentStart")
@Transactional
public class MissionWaitPaymentStartWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(MissionWaitPaymentStartWatcher.class);
    @Autowired
    private App42PushNotificationService app42PushNotificationService;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private NotificationListRepository notificationListRepository;


    @Scheduled(fixedRate = 30000)
    public void execute() {
            LOGGER.info("MissionWaitPaymentStartWatcher START!!!");
            List<Mission> missions = missionRepository.findByState(Mission.State.ARRIVED);
            if(!CollectionUtils.isEmpty(missions)){
                 String message = commonService.getPropertyValue("mission_wait_payment_start_push_mess");
                 for(Mission mission: missions){
                     CommonService.TimeWaitClientUtil freeTimeWaitClientUtil = commonService.freeTimeLeftWaitClient(mission);
                         if(freeTimeWaitClientUtil.getFreeTimeInFact() <= 0 ){ // driverTimeWait > freeWaitMinutesByAutoClass
                              // если водитель ждет клиента больше чем положено по тарифу - шлем пуш
                              Notification notification = notificationListRepository.findByMissionAndPushType(mission, PushType.WAIT_PAYMENT_START);
                              if(notification == null){
                                  try{
                                      notification = new Notification();
                                      notification.setMission(mission);
                                      notification.setTimeOfNotification(DateTimeUtils.nowNovosib_GMT6());
                                      notification.setPushType(PushType.WAIT_PAYMENT_START);
                                      notification.setDescription("ok");
                                      app42PushNotificationService.missionWaitPaymentStart(mission.getClientInfo().getId(), message);
                                  }catch(CustomException c){
                                      notification.setDescription(c.getMessage());
                                      notification.setDescription(jsonErrorMessage(c.getMessage()));
                                      c.printStackTrace();
                                  }
                                      notificationListRepository.save(notification);
                              }
                         }
                 }
            }
    }



// {"app42Fault":{"httpErrorCode":401,"appErrorCode":1401,"message":"UnAuthorized Access","details":"Client is not authorized"}}
    private String jsonErrorMessage(String jsonStr)  {
        try {
            JSONObject json = new JSONObject(jsonStr);
            if (json.has("app42Fault")) {
                JSONObject descErr = (JSONObject) json.get("app42Fault");
                if (descErr.has("details")) {
                    return (String) descErr.get("details");
                }
            }
        }catch(JSONException exc){
        }
        return jsonStr;
    }

}
