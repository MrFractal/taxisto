package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.TurboIncreaseDriver;

/**
 * Created by petr on 19.12.2014.
 */
@Repository
public interface TurboIncreaseDriverRepository extends CrudRepository<TurboIncreaseDriver, Long> {
    TurboIncreaseDriver findByMissionAndDriver(Mission mission, Driver driver);
}
