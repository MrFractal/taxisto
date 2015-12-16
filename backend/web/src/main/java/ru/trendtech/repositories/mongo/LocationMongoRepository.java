package ru.trendtech.repositories.mongo;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.mongo.Location;

/**
 * Created by petr on 20.11.2014.
 */
@Repository
public interface LocationMongoRepository extends PagingAndSortingRepository<Location, String> {
}
