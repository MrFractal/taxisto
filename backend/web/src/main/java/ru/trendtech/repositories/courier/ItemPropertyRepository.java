package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.ItemProperty;

/**
 * Created by petr on 07.10.2015.
 */
@Repository
public interface ItemPropertyRepository extends CrudRepository<ItemProperty, Long>{
}
