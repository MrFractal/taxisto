package ru.trendtech.repositories.mongo;

//import ru.trendtech.domain.mongo.Location;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.mongo.Location;
import ru.trendtech.domain.mongo.LoggingEventMongo;


import java.util.List;

/**
 * Created by petr on 20.08.14.
 */
@Repository
public interface LoggingEventMongoRepository extends PagingAndSortingRepository<LoggingEventMongo, String> {
    List<LoggingEventMongo> findByDriverId(String driverId);
}
