package ru.trendtech.services.watchers;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.domain.SendSmsLock;
import ru.trendtech.repositories.SendSmsLockRepository;
import ru.trendtech.utils.DateTimeUtils;

import java.util.List;

/**
 * Created by petr on 10.02.2015.
 */
@Service("unlockSmsSendWatcher")
public class UnlockSmsSendWatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnlockSmsSendWatcher.class);

    @Autowired
    SendSmsLockRepository sendSmsLockRepository;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void unlockWatcherStart() {
        List<SendSmsLock> sendSmsLocks = sendSmsLockRepository.findByTimeOfUnlockIsNull();
           if(sendSmsLocks!=null && !sendSmsLocks.isEmpty()) {
                for(SendSmsLock sendSmsLock :sendSmsLocks) {
                    DateTime autoUnlockTime =  sendSmsLock.getAutoUnlockTime();
                       if(autoUnlockTime!=null){
                            if(autoUnlockTime.isBefore(DateTimeUtils.nowNovosib_GMT6())){
                                sendSmsLock.setTimeOfUnlock(DateTimeUtils.nowNovosib_GMT6());
                                sendSmsLockRepository.save(sendSmsLock);
                            }
                       }
                }
           }
    }
}
