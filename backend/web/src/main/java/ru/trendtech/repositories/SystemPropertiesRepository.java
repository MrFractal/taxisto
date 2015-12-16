package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.SystemProperties;

/**
 * Created by petr on 05.11.2014.
 */
@Repository
public interface SystemPropertiesRepository extends CrudRepository<SystemProperties, Long> {
    SystemProperties findByPropName(String propName);
}
