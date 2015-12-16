package ru.trendtech.repositories.courier;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderStateHistory;

import java.util.List;

/**
 * Created by petr on 15.10.2015.
 */
@Repository
public interface OrderStateHistoryRepository extends PagingAndSortingRepository<OrderStateHistory, Long> {
      List<OrderStateHistory> findByOrderOrderByTimeOfChangeDesc(Order order, Pageable pageable);
      List<OrderStateHistory> findByOrderAndStateInOrderByTimeOfChangeDesc(Order order, List<Order.State> courierStates, Pageable pageable);
      List<OrderStateHistory> findByOrderAndStateInOrderByTimeOfChangeDesc(Order order, List<Order.State> courierStates);
}
