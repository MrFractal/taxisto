package ru.trendtech.services.watchers;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.domain.Mission;
import ru.trendtech.repositories.MissionRepository;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.notifications.NotificationsService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.services.notifications.node.NodeJsService;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.sms.SMSC;
import ru.trendtech.utils.DateTimeUtils;

import java.util.*;

/**
 * File created by max on 07/05/2014 19:39.
 */


@Service("bookingWatcher")
public class BookingWatcher implements BookingWatcherIface {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingWatcher.class);
    @Value("${cancel_booked_after}")
    private int cancelBookedAfter;
    @Autowired
    private NodeJsService nodeJsService;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private AdministrationService administrationService;


    @Scheduled(fixedRate = 30000)
    @Transactional
    public void checkBooking() {
        nodeJsService.connectToSocket();
        List<Mission> missions = new ArrayList<Mission>();
        missions.addAll(missionRepository.findByStateAndBookingStateAndIsBookedOrderByTimeOfStartingAsc(Mission.State.BOOKED, Mission.BookingState.WAITING, true)); //not assigned missions
        missions.addAll(missionRepository.findByStateAndBookingStateAndIsBookedOrderByTimeOfStartingAsc(Mission.State.BOOKED, Mission.BookingState.DRIVER_ASSIGNED, true)); //assigned missions

    /*
        Если в течении 15 минут, после создании брони, никто из водителей не заасанился, то необходимо:
        1. Отправить на клиента информацию о том что бронь не подтвердилась.
        2. Отменить бронь.
        3. Отправить смс.
    */
    LOGGER.info("cancelBookedAfter = " + cancelBookedAfter);
        for (Mission mission : missions) {
            DateTime timeOfStarting = mission.getTimeOfStarting();
            DateTime timeOfRequesting = mission.getTimeOfRequesting();
            DateTime nowDateTime = DateTimeUtils.nowNovosib_GMT6();

            if (timeOfStarting != null) {
                Minutes minutes = Minutes.minutesBetween(nowDateTime, timeOfStarting);

                if (minutes.getMinutes() <= 60 && minutes.getMinutes() >= 35 && mission.getCountNotified() != 1) {
                    // first notified
                    if (Mission.BookingState.DRIVER_ASSIGNED.equals(mission.getBookingState()) && mission.getDriverInfo() != null) {
                        //LOGGER.debug("1.minutes.getMinutes() <= 60 && minutes.getMinutes()>=35 && mission.getCountNotified()!=1");
                        if (mission != null) {
                               /* notified nod*/
                            nodeJsNotificationsService.bookedMissionFired(mission.getId(), DateTimeUtils.stringDateTimeByPattern(mission.getTimeOfStarting(), "dd.MM 'в' HH:mm"));
                            /*
                            JSONObject json = new JSONObject();
                            json.put("missionId", mission.getId());
                            nodeJsService.notified("booked_mission_fired", json);
                            */
                        }
                    }
                } else if (minutes.getMinutes() < 35 && minutes.getMinutes() >= 30 && mission.getCountNotified() != 2) {
                    // second notified
                    if (Mission.BookingState.DRIVER_ASSIGNED.equals(mission.getBookingState()) && mission.getDriverInfo() != null) {
                        //LOGGER.debug("2.minutes.getMinutes() < 35 && minutes.getMinutes()>=30 && mission.getCountNotified()!=2");
                        if (mission != null) {
                            /* notified nod*/
                            /*
                            JSONObject json = new JSONObject();
                            json.put("missionId", mission.getId());
                            nodeJsService.notified("booked_mission_fired", json);
                            */
                            nodeJsNotificationsService.bookedMissionFired(mission.getId(), DateTimeUtils.stringDateTimeByPattern(mission.getTimeOfStarting(), "dd.MM 'в' HH:mm"));
                        }
                    }

                        /* оповещаем диспетчера АРМ-а о том, что водитель не подтвердил первый раз бронь */
                        nodeJsNotificationsService.openMissionCard(mission.getId());
                        /*
                        JSONObject json = new JSONObject();
                        json.put("missionId", mission.getId());
                        nodeJsService.notified("open_mission_card", json);
                        */
                } else if (minutes.getMinutes() < 28 && mission.getCountNotified() == 0) { // != 2

                        // on off 25.09.2015
                        //administrationService.cancelMission(true, mission);

                        /* notified nod
                        on off 25.09.2015
                        JSONObject json = new JSONObject();
                        json.put("missionId", mission.getId());
                        nodeJsService.notified("mission_booked_canceled_by_server", json);
                        */

                        // списание штрафа / пока отключено
                        if (mission.getDriverInfo() != null) {
                            //administrationService.updateDriverBalance(mission.getDriverInfo().getId(), mission.getId(), -1000, 1, null); // Money.of(CurrencyUnit.of("RUB"), -1000)
                        }
                }
            }
            if (timeOfRequesting != null) {
                Minutes minutes2 = Minutes.minutesBetween(timeOfRequesting, nowDateTime);
                LOGGER.info("MinutesBetween(nowDateTime, timeOfRequesting) = " + minutes2.getMinutes());
                if (minutes2.getMinutes() >= cancelBookedAfter) { // <= -15
                    if (!Mission.BookingState.DRIVER_ASSIGNED.equals(mission.getBookingState()) && !Mission.BookingState.DRIVER_NOTIFIED.equals(mission.getBookingState())) {

                        // todo добавить сюда причину
                        administrationService.cancelMission(true, mission); // true - не заасаниная миссия

                        /* notified nod*/
                        nodeJsNotificationsService.bookedMissionFailed(mission.getId());
                        /*
                        JSONObject json = new JSONObject();
                        json.put("missionId", mission.getId());
                        nodeJsService.notified("booked_mission_failed", json);
                        */
                    }
                }
            }
        }


    }

}






/* ВРЕМЕННО ОТКЛЮЧАЕМ ЭТОТ ФУНКЦИОНАЛ
        missions.clear();
        missions.addAll(missionRepository.findByState(Mission.State.NEW)); //new missions
        for (Mission mission : missions) {
            DateTime timeOfRequesting = mission.getTimeOfRequesting();
            //  DateTime nowDateTime = timeService.nowDateTime();

            TimeZone tz = TimeZone.getTimeZone("GMT+7");
            Calendar calendar2 = Calendar.getInstance();
            calendar2.add(Calendar.MILLISECOND, tz.getOffset(timeService.now().getTime()));
            DateTime nowDateTime = new DateTime(calendar2.getTime());

            if (timeOfRequesting != null) {
                Minutes minBetweenTimeNowAndTimeOfReq = Minutes.minutesBetween(nowDateTime, timeOfRequesting);
                LOGGER.info("minBetweenTimeNowAndTimeOfReq = "+minBetweenTimeNowAndTimeOfReq.getMinutes());
                if(minBetweenTimeNowAndTimeOfReq.getMinutes()<=-10){
                    cancelMission(false, mission);
                    notified("mission_canceled_by_server", mission);
                    LOGGER.debug("5.Mission state = New time<10");
                }
            }
        }
        */

           /* проверка статусов смс сообщений */
//LOGGER.debug("SMS checker stopped");
//smsSentCheck();

//LOGGER.debug("Booking checker stopped");





/*
   public void notified2(String event, Mission mission){
              //  ---------- send to socket [mission fired]-----------------
              try {
                  if(nodeJsService.isConnected()){
                      JSONObject json = new JSONObject();
                      json.put("missionId", mission.getId());
                      nodeJsService.sendMessageSocket(event,json);
                  }else{
                      LOGGER.info("is response faild");
                  }
              } catch (JSONException e) {
                  e.printStackTrace();
              }
              //    --------------------------------------------
          }
*/
