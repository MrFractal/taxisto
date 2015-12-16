package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.trendtech.domain.MissionCanceled;

import java.util.List;

/**
 * Created by petr on 11.11.2014.
 */
public interface MissionCanceledRepository extends CrudRepository<MissionCanceled, Long> {
    //List<MissionCanceled> findByMissionId(long missionId);
    MissionCanceled findByMissionId(long missionId);
    MissionCanceled findByMissionIdAndCancelBy(long missionId, String cancelBy);
}
