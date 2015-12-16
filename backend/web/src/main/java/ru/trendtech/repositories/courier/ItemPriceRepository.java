package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.ItemPrice;

/**
 * Created by petr on 25.08.2015.
 */
@Repository
public interface ItemPriceRepository extends CrudRepository<ItemPrice, Long>{
}
