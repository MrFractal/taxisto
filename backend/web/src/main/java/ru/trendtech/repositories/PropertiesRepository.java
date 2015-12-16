package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.PromoCodes;
import ru.trendtech.domain.Properties;

/**
 * Created by petr on 16.09.2014.
 */

@Repository
public interface PropertiesRepository extends CrudRepository<Properties, Long> {
     Properties findByPropName(String text);
}