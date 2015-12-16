package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.MissionFantomDriver;

import java.util.List;

/**
 * Created by petr on 08.06.2015.
 */
@Repository
public interface MissionFantomDriverRepository extends CrudRepository<MissionFantomDriver, Long>{
      MissionFantomDriver findByMission(Mission mission);
      List<MissionFantomDriver> findByMissionClientInfoAndTimeOfAssigningBetween(Client client, DateTime start, DateTime end);
}
