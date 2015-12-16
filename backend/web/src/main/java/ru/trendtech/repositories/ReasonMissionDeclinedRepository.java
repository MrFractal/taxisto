package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.ReasonMissionDeclined;

import java.util.List;

/**
 * Created by petr on 30.03.2015.
 */
@Repository
public interface ReasonMissionDeclinedRepository extends CrudRepository<ReasonMissionDeclined, Long>{
    ReasonMissionDeclined findByMissionAndDriver(Mission mission, Driver driver);
    List<ReasonMissionDeclined> findByMission(Mission mission);
}
