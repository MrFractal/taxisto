package ru.trendtech.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.mongo.DriverActivity;

/**
 * Created by petr on 31.03.2015.
 */
@Repository
public interface DriverActivityRepository extends MongoRepository<DriverActivity, String> {
}
