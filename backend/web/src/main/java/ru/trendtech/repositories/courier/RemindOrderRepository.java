package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.RemindOrder;

import java.util.List;

/**
 * Created by petr on 16.09.2015.
 */
@Repository
public interface RemindOrderRepository  extends CrudRepository<RemindOrder,Long>{
    RemindOrder findByOrderAndRemindType(Order order, RemindOrder.RemindType remindType);
    RemindOrder findByOrderAndTimeOfConfirmIsNull(Order order);
}
