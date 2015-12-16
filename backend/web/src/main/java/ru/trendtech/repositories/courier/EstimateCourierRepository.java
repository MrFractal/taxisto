package ru.trendtech.repositories.courier;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.EstimateCourier;
import ru.trendtech.domain.courier.Order;

/**
 * Created by petr on 15.09.2015.
 */
@Repository
public interface EstimateCourierRepository extends PagingAndSortingRepository<EstimateCourier, Long>{
     EstimateCourier findByOrder(Order order);
}
