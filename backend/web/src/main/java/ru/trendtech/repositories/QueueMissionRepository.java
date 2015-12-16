package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.QueueMission;
/**
 * Created by petr on 01.04.2015.
 */
@Repository
public interface QueueMissionRepository extends CrudRepository<QueueMission, Long>{
    QueueMission findByMissionAndDriver(Mission mission, Driver driver);
    QueueMission findByMission(Mission mission);
}
