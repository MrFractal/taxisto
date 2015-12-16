package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderAddress;
import ru.trendtech.domain.courier.TargetAddressState;

import java.util.List;

/**
 * Created by petr on 26.08.2015.
 */
@Repository
public interface OrderAddressRepository extends CrudRepository<OrderAddress, Long> {
    List<OrderAddress> findByTargetAddressStateAndOrder(TargetAddressState targetAddressState, Order order);
}
