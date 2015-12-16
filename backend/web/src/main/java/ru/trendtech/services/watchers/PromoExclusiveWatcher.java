package ru.trendtech.services.watchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.domain.PrivateTariff;
import ru.trendtech.repositories.PrivateTariffRepository;
import ru.trendtech.utils.DateTimeUtils;
import java.util.List;

/**
 * Created by petr on 14.05.2015.
 */
@Service("PromoExclusive")
public class PromoExclusiveWatcher {
    @Autowired
    PrivateTariffRepository privateTariffRepository;

    @Scheduled(fixedRate = 60000) // start watcher every 60 sec
    @Transactional
    public void promoExclusive() {
        List<PrivateTariff> privateTariffList = privateTariffRepository.findByTariffNameAndActiveAndExpirationDateLessThan(AutoClass.BONUS.name(), true, DateTimeUtils.nowNovosib_GMT6());
         for(PrivateTariff privateTariff: privateTariffList){
             privateTariff.setActive(false);
         }
         privateTariffRepository.save(privateTariffList);
    }
}
