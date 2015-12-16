package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.PriceChanges;

import java.util.List;

/**
 * Created by petr on 18.09.2015.
 */
@Repository
public interface PriceChangesRepository extends CrudRepository<PriceChanges, Long>{
         List<PriceChanges> findByOrder(Order order);
}
