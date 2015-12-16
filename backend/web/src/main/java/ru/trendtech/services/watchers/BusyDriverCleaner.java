package ru.trendtech.services.watchers;

import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverLocation;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.repositories.LocationRepository;
import ru.trendtech.utils.DateTimeUtils;
import java.util.List;

/**
 * Created by petr on 04.03.2015.
 */
@Service("busyDriverCleaner")
public class BusyDriverCleaner {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusyDriverCleaner.class);
    @Autowired
    DriverRepository driverRepository;
    @Autowired
    LocationRepository locationRepository;

    @Scheduled(fixedDelay = 120000)
    @Transactional
    public void busyDriverCleaner() {
          List<Driver> drivers = driverRepository.findByState(Driver.State.BUSY);
              for(Driver driver: drivers){
                  DriverLocation location = locationRepository.findByDriverId(driver.getId());
                  Minutes minutes = Minutes.minutesBetween(DateTimeUtils.nowNovosib_GMT6(), location.getWhen());
                    if(Math.abs(minutes.getMinutes())>15){
                         driver.setState(Driver.State.OFFLINE);
                         driverRepository.save(driver);
                    }
              }
    }
}
