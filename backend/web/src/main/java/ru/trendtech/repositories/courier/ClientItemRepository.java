package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.ClientItem;

/**
 * Created by petr on 03.09.2015.
 */
@Repository
public interface ClientItemRepository extends CrudRepository<ClientItem, Long>{
}
