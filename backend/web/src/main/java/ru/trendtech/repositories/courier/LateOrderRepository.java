package ru.trendtech.repositories.courier;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.LateOrder;
import ru.trendtech.domain.courier.Order;

import java.util.List;

/**
 * Created by petr on 15.09.2015.
 */
@Repository
public interface LateOrderRepository extends CrudRepository<LateOrder, Long>{
     List<LateOrder> findByOrder(Order order);

     @Query(value = "select sum(cl.minutes_late) from c_late_order cl where cl.c_order_id = ?1", nativeQuery = true)
     Integer generalTimeLate(long orderId);
}
