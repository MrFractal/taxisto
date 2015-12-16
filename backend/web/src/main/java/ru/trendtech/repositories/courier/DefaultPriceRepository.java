package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.DefaultPrice;
import ru.trendtech.domain.courier.OrderType;

/**
 * Created by petr on 28.08.2015.
 */
@Repository
public interface DefaultPriceRepository extends CrudRepository<DefaultPrice, Long>{
    DefaultPrice findByActiveAndOrderType(boolean active, OrderType orderType);
}
