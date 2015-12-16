package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.repository.CrudRepository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.SMSMessage;
import ru.trendtech.domain.ServicePrice;

import java.util.List;

/**
 * Created by petr on 09.10.2014.
 */
public interface SMSMessageRepository extends CrudRepository<SMSMessage, Long> {
    //SMSMessage findByClient(Client client);
    //List<SMSMessage> findByTimeOfDeliveryIsNullAndTimeOfSendIsNotNull();
    List<SMSMessage> findByTimeOfDeliveryNot(long timeDelivery);
    List<SMSMessage> findByTimeOfDeliveryIn(long timeDelivery);

    List<SMSMessage> findByTimeOfCreateBetween(DateTime start, DateTime end);
    List<SMSMessage> findByTimeOfCreateBetweenAndClient(DateTime start, DateTime end, Client client);

    //List<SMSMessage> findByTimeOfDeliveryIsNullAndCountTryNot(int countTry);
    //List<SMSMessage> findBySent(boolean sentStatus);
}
