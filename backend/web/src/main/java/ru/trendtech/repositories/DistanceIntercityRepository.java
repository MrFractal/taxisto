package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.DistanceIntercity;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Mission;

/**
 * Created by petr on 29.06.2015.
 */
@Repository
public interface DistanceIntercityRepository extends CrudRepository<DistanceIntercity, Long> {
    DistanceIntercity findByDriverAndMission(Driver driver, Mission mission);
}
