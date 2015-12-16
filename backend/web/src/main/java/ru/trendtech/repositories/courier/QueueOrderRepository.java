package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.QueueMission;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.QueueOrder;

import java.util.List;

/**
 * Created by petr on 16.09.2015.
 */
@Repository
public interface QueueOrderRepository extends CrudRepository<QueueOrder, Long>{
    QueueOrder findByOrder(Order order);
    //List<QueueOrder> findByDriverAndOrderStateNotIn(Driver driver, List<Order.State> states);
}
