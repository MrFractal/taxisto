package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.MissionStateStatistic;

/**
 * Created by petr on 10.02.2015.
 */
@Repository
public interface MissionStateStatisticRepository extends CrudRepository<MissionStateStatistic, Long>{
    MissionStateStatistic findByMission(Mission mission);
}
