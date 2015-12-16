package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Router;
import ru.trendtech.domain.RouterUsed;

/**
 * Created by petr on 30.06.2015.
 */
@Repository
public interface RouterUsedRepository extends CrudRepository<RouterUsed, Long> {
    RouterUsed findByDriverAndEndUsedIsNull(Driver driver);
    RouterUsed findByRouterAndEndUsedIsNull(Router router);
    RouterUsed findByRouterAndDriverNotAndEndUsedIsNull(Router router, Driver driver);
}
