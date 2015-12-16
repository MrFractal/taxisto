package ru.trendtech.services.watchers.courier;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderType;
import ru.trendtech.repositories.courier.OrderRepository;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.courier.OrderService;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.utils.DateTimeUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by petr on 29.10.2015.
 */
@Service
public class CancelOrderWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderWatcher.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ServiceSMSNotification serviceSMSNotification;
    @Autowired
    private OrderService orderService;


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void start() {
        List<Order.State> stateList = Arrays.asList(Order.State.WAIT_TO_CONFIRM, Order.State.NEW);
        List<Order> orderList = orderRepository.findByStateInAndOrderType(stateList, OrderType.BUY_AND_DELIVER);
        if(!CollectionUtils.isEmpty(orderList)){
            DateTime timeRequesting;
            Minutes minutes;
            DateTime timeNow = DateTimeUtils.nowNovosib_GMT6();
            for(Order order: orderList){
                timeRequesting = order.getTimeOfRequesting();
                if(timeNow.isAfter(timeRequesting)) {
                    minutes = Minutes.minutesBetween(timeRequesting, timeNow);
                    LOGGER.info("timeNow: " + timeNow + " | timeRequesting: " + timeRequesting + " | DIFF: " + minutes.getMinutes());
                    if(Math.abs(minutes.getMinutes()) >= 60) {
                        //todo: cancel order
                        orderService.cancelOrderByServer(order);
                        serviceSMSNotification.sendCustomSMS(order.getClient().getPhone(), "Вы не подтвердили заказ. Ваш заказ отменен.", "");
                    }
                }
            }
        }
    }

}
