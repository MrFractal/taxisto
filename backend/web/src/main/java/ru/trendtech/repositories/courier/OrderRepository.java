package ru.trendtech.repositories.courier;


import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderType;
import java.util.List;

/**
 * Created by petr on 25.08.2015.
 */
@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    List<Order> findByClientAndStateIn(Client client, List<Order.State> states);
    List<Order> findByState(Order.State state);
    List<Order> findByClientAndStateAndIsBookedAndDriverIsNull(Client client, Order.State state, boolean booked);
    List<Order> findByClientAndStateInAndOrderTypeNotOrderByTimeOfRequestingDesc(Client client, List<Order.State> states, OrderType orderType, Pageable pageable);
    List<Order> findByClientAndStateInOrderByTimeOfRequestingDesc(Client client, List<Order.State> states, Pageable pageable);
    List<Order> findByClientAndStateInAndOrderTypeNot(Client client, List<Order.State> states, OrderType orderType);
    List<Order> findByStateAndOrderType(Order.State state, OrderType orderType);
    List<Order> findByStateInAndOrderType(List<Order.State> stateList, OrderType orderType);
    List<Order> findByStateAndOrderTypeIn(Order.State state, List<OrderType> orderTypeList);
    List<Order> findByStateInAndDriverIsNullOrderByTimeOfRequestingDesc(List<Order.State> states);
    List<Order> findByStateInAndDriverOrderByTimeOfRequestingDesc(List<Order.State> states, Driver driver);
    List<Order> findByWebUserAndStateIsNotIn(WebUser webUser, List<Order.State> states);
    Order findByIdAndClientAndStateIsNot(long orderId, Client client, Order.State state);
}
