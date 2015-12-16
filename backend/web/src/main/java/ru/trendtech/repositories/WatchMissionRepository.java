package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.WatchMission;

/**
 * Created by petr on 08.12.2014.
 */
@Repository
public interface WatchMissionRepository extends JpaRepository<WatchMission, Long> {
    WatchMission findByMission(Mission mission);
    WatchMission findByToken(String token);
}
