package ru.trendtech.services.watchers;

import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverLocation;
import ru.trendtech.domain.MDOrder;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.repositories.LocationRepository;
import ru.trendtech.repositories.MdOrderRepository;
import ru.trendtech.repositories.MissionRepository;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.SendEmailUtil;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by petr on 12.03.2015.
 */
@Service("complete_IN_TRIP_Mission")
public class CompleteMissionWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompleteMissionWatcher.class);
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private MdOrderRepository mdOrderRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private CommonService commonService;

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void completeMission() {
        List<Mission> missionIntTrip = missionRepository.findByState(Mission.State.IN_TRIP);
                for(Mission mission: missionIntTrip){
                    if(mission.getPaymentType().equals(PaymentType.CARD) || mission.getPaymentType().equals(PaymentType.CORPORATE_CARD)){
                        MDOrder mdOrder = mdOrderRepository.findByMission(mission);
                        if(mdOrder!=null && mdOrder.getPaymentStatus()!=null && mdOrder.getPaymentStatus()){
                            // проведена оплата картой, статус миссии при этом IN_TRIP, комплитим миссию и очищаем ее на водителе
                            startComplete(mission);
                        }
                    }else{
                         Hours h = Hours.hoursBetween(DateTimeUtils.nowNovosib_GMT6(), mission.getTimeOfRequesting());
                         if(Math.abs(h.getHours())>24){
                             startComplete(mission);
                         }
                    }
                }
    }



    private void startComplete(Mission mission){
        Driver driver = mission.getDriverInfo();
        if(driver!=null){
            DriverLocation location = locationRepository.findByDriverAndMission(driver, mission);
            if(location!=null){
                location.setMission(null);
                locationRepository.save(location);
            }
            mission.setState(Mission.State.COMPLETED);
            missionRepository.save(mission);
            SendEmailUtil.sendEmail(commonService.getPropertyValue("int_trip_watcher_recipient_email"), String.format("COMPLETE mission id=%s, payment_state=%s", mission.getId(), mission.getPaymentType().name()), "Alert");
        }
    }


}
