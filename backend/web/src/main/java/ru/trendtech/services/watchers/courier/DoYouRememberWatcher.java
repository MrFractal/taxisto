package ru.trendtech.services.watchers.courier;

import org.apache.commons.lang3.Range;
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
import ru.trendtech.domain.courier.RemindOrder;
import ru.trendtech.repositories.courier.OrderRepository;
import ru.trendtech.repositories.courier.RemindOrderRepository;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.utils.DateTimeUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by petr on 16.09.2015.
 */
@Service("doYouRemember")
public class DoYouRememberWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoYouRememberWatcher.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RemindOrderRepository remindOrderRepository;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;
    private Range<Integer> firstRemindRange = Range.between(55, 60);
    private Range<Integer> secondRemindRange = Range.between(25, 30);



    private RemindOrder buildRemindOrder(Order order, RemindOrder.RemindType remindType){
        RemindOrder remindOrder = new RemindOrder();
        remindOrder.setOrder(order);
        remindOrder.setRemindType(remindType);
        remindOrder.setTimeOfRemind(DateTimeUtils.nowNovosib_GMT6());
        return remindOrder;
    }


    private void saveOrNotified(Order order, RemindOrder.RemindType remindType){
        RemindOrder remindOrder = remindOrderRepository.findByOrderAndRemindType(order, remindType);
        if(remindOrder == null){
            remindOrder = buildRemindOrder(order, remindType);
            remindOrderRepository.save(remindOrder);
            notifidDriver(order, remindType);
        } else {
            if(remindOrder.getTimeOfConfirm() == null){
                /* курьер не подтвердил, шлем еще раз */
                notifidDriver(order, remindType);
            }
        }
    }


    @Scheduled(fixedRate = 30000)
    @Transactional
    public void doYouRemember() {
          List<OrderType> orderTypeList = Arrays.asList(OrderType.TAKE_AND_DELIVER, OrderType.BUY_AND_DELIVER);
          List<Order> orderList = orderRepository.findByStateAndOrderTypeIn(Order.State.TAKEN_BY_COURIER, orderTypeList);
          if(!CollectionUtils.isEmpty(orderList)){
              DateTime timeOfFinishing; Minutes minutes; int result;
              DateTime timeNow = DateTimeUtils.nowNovosib_GMT6();
              for(Order order: orderList){
                  timeOfFinishing = order.getTimeOfFinishing();
                  if(timeNow.isBefore(timeOfFinishing)){
                      minutes = Minutes.minutesBetween(timeOfFinishing, timeNow);
                      result = Math.abs(minutes.getMinutes());
                      if(firstRemindRange.contains(result)){
                          saveOrNotified(order, RemindOrder.RemindType.FIRST);
                      } else if(secondRemindRange.contains(result)){
                          saveOrNotified(order, RemindOrder.RemindType.SECOND);
                      }
                  }
              }
          }
    }


    /* todo: связать first second с интервалами */

    private void notifidDriver(Order order, RemindOrder.RemindType remindType){
        int countNotified = 1;
        if(remindType.equals(RemindOrder.RemindType.SECOND)){
            countNotified = 2;
        }
        nodeJsNotificationsService.courierOrderFired(order, countNotified);
    }

}
