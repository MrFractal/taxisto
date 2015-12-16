package ru.trendtech.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.mongo.Events;

/**
 * Created by petr on 12.01.2015.
 */
@Repository
public interface EventsRepository extends MongoRepository<Events, String> {
}
