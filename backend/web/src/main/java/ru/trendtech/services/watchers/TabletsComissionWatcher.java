package ru.trendtech.services.watchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.trendtech.domain.Driver;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.email.ServiceEmailNotification;

import java.util.List;


/**
 * Created by petr on 24.06.2015.
 */

@Service("tablets_comission")
public class TabletsComissionWatcher implements Commissions {
    private static final Logger LOGGER = LoggerFactory.getLogger(TabletsComissionWatcher.class);
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private CommonService commonService;
    //private int counter_comission = 1;


    @Scheduled(cron="0 0 2 * * *") // "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day. | "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
    @Transactional
    public void driverComissionForUseTablet() {

        List<Driver> driverList = driverRepository.findByTabletIsNotNullAndTabletOwn(true);
        if(!CollectionUtils.isEmpty(driverList)){
            int comissionAmount = -(Integer.parseInt(commonService.getPropertyValue("tablets_comission")));
            for(Driver driver: driverList){
                /*
                if(counter_comission == 1){
                    comissionAmount = comissionAmount * 2;
                    counter_comission++;
                    LOGGER.info("counter_comission: " + counter_comission + " amount: " + comissionAmount);
                }
                */
                administrationService.operationWithMoney(driver.getId(), null, comissionAmount, 19, null, "", null, null);
            }
        }
    }
}
