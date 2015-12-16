package ru.trendtech.services.watchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.RegionInfo;
import ru.trendtech.domain.*;
import ru.trendtech.repositories.DistanceIntercityRepository;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.repositories.LocationRepository;
import ru.trendtech.repositories.RegionRepository;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.driver.search.FindDriversService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 22.04.2015.
 */

@Service("define_coast")
public class DefineCoastWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefineCoastWatcher.class);
    private final List<Driver.State> driverStates = new ArrayList<>();
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private NodeJsNotificationsService notificationsService;


    public DefineCoastWatcher(){
        driverStates.add(Driver.State.AVAILABLE);
        driverStates.add(Driver.State.BUSY);
    }


    @Scheduled(fixedRate = 60000) // start watcher every 60 sec
    @Transactional
    public void defineCoast() {
        List<Driver> drivers = driverRepository.findByStateIn(driverStates);
        for(Driver driver: drivers) {
            DriverLocation location = locationRepository.findByDriver(driver);
                if(location!=null && location.getLocation()!=null && location.getLocation().getLatitude()!=0 && location.getLocation().getLongitude()!=0){
                    RegionInfo info = administrationService.pointInsidePolygon(location.getLocation().getLatitude(), location.getLocation().getLongitude());
                    String coast = "unknown";

                    if(driver.getCurrentMission()!=null && driver.getCurrentMission().getState().equals(Mission.State.IN_TRIP)){
                        ChangeRegionHelper changeRegionHelper = changeRegion(location.getRegion(), info);
                        if(changeRegionHelper.isChangeRegion()){
                            // шлем событие на ноду
                            notificationsService.regionChange(driver.getId(), "Вы "+(changeRegionHelper.isFromRemote() ? "покинули ":" въехали в")+" зону межгорода", changeRegionHelper.isFromRemote());
                        }
                    }

                        if(info != null){
                            if(!StringUtils.isEmpty(info.getCoast())){
                              coast = info.getCoast();
                            }
                            location.setCoast(coast);
                            location.setRegion(regionRepository.findOne(info.getId()));
                            locationRepository.save(location);
                        }
                }
        }
    }



    private ChangeRegionHelper changeRegion(Region previousRegion, RegionInfo currentRegion){
        ChangeRegionHelper changeRegionHelper = new ChangeRegionHelper();
        boolean previousRegionIsRemote = previousRegion == null || previousRegion.getTypeRegion()==2;
        boolean currentRegionIsRemote = currentRegion == null || currentRegion.getTypeRegion()==2;

        if(previousRegionIsRemote && !currentRegionIsRemote){
             // из межгорода в Аэро или местную
            changeRegionHelper.setFromRemote(true);
            changeRegionHelper.setChangeRegion(true);
        } else if(!previousRegionIsRemote && currentRegionIsRemote){
             // из Аэро или местной в межгород
            changeRegionHelper.setFromRemote(true);
            changeRegionHelper.setChangeRegion(true);
        }
           return changeRegionHelper;
    }




    private class ChangeRegionHelper{
        boolean changeRegion = false;
        boolean fromRemote = false;

        public boolean isChangeRegion() {
            return changeRegion;
        }

        public void setChangeRegion(boolean changeRegion) {
            this.changeRegion = changeRegion;
        }

        public boolean isFromRemote() {
            return fromRemote;
        }

        public void setFromRemote(boolean fromRemote) {
            this.fromRemote = fromRemote;
        }
    }

    /*
    else {
                            Mission mission = driver.getCurrentMission();
                            if(mission!=null && EnumSet.of(Mission.State.IN_TRIP).contains(mission.getState())){
                                DistanceIntercity distanceIntercity = distanceIntercityRepository.findByDriverAndMission(driver, mission);
                                    if(distanceIntercity == null){
                                        distanceIntercity = new DistanceIntercity(mission, driver, 0.0);
                                    }
                            }
    }
    */
}
