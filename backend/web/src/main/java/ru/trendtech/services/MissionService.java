package ru.trendtech.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.common.MissionInfo;
import ru.trendtech.common.mobileexchange.model.web.LastMissionsInfo;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Mission;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.repositories.MissionRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * File created by max on 20/04/2014 23:08.
 */

@Service
//@Transactional
public class MissionService {
    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DriverRepository driverRepository;


    public MissionInfo find(long missionId) {
        MissionInfo result = null;
        Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            result = ModelsUtils.toModel(mission);
        }
        return result;
    }



     // И не забудьте что если у брони статус driver_assigned or driver_approved, то нужно возвращать еще инфу о драйвере по его id в mission.driverInfo_id
    public List findBookingMission(long missionId) {
        MissionInfo missionInfo = null;
        DriverInfo driverInfo = null;

        List result = new ArrayList();

        Mission mission = missionRepository.findByIdAndState(missionId, Mission.State.BOOKED);

        if (mission != null) {
                   result.add(0, ModelsUtils.toModel(mission));
               if((mission.getBookingState().equals(Mission.BookingState.DRIVER_ASSIGNED)) || (mission.getBookingState().equals(Mission.BookingState.DRIVER_APPROVED))){
                   result.add(1, ModelsUtils.toModel(mission.getDriverInfo()));
               }else {
                   result.add(1,null);
               }
        }
        return result;
    }



    public MissionInfo findByClient(long clientId) {
        MissionInfo result = null;
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            Mission mission = client.getMission();
            if (mission != null) {
                result = ModelsUtils.toModel(mission);
            }
        }
        return result;
    }

    public MissionInfo findByDriver(long driverId) {
        MissionInfo result = null;
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null) {
            Mission mission = driver.getCurrentMission();
            if (mission != null) {
                result = ModelsUtils.toModel(mission);
            }
        }
        return result;
    }

    public List<LastMissionsInfo> findLastMissionsClient(long clientId) {
        ArrayList<LastMissionsInfo> result = new ArrayList<>();
        Client client = clientRepository.findOne(clientId);
        if (client != null){
            List<Mission> missions = missionRepository.findByClientInfoOrderByIdDesc(client);
            for (Mission mission : missions) {
                Driver driver = mission.getDriverInfo();
                LastMissionsInfo info = new LastMissionsInfo();
                info.setId(driver.getId());
                info.setPhone(driver.getPhone());
                info.setRequestTime(mission.getTimeOfRequesting().getMillis());
                info.setFullName(driver.getFirstName() + " " + driver.getLastName());
                result.add(info);
            }
        }
        return result;
    }

    public List<LastMissionsInfo> findLastMissionsDriver(long driverId) {
        ArrayList<LastMissionsInfo> result = new ArrayList<>();
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null){
            List<Mission> missions = missionRepository.findByDriverInfoOrderByIdDesc(driver);
            for (Mission mission : missions) {
                Client clientInfo = mission.getClientInfo();
                LastMissionsInfo info = new LastMissionsInfo();
                info.setId(clientInfo.getId());
                info.setPhone(clientInfo.getPhone());
                info.setRequestTime(mission.getTimeOfRequesting().getMillis());
                info.setFullName(clientInfo.getFirstName() + " " + clientInfo.getLastName());
                result.add(info);
            }
        }
        return result;
    }
}
