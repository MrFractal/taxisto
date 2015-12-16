package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Rerouting;

import java.util.List;

/**
 * Created by petr on 19.08.14.
 */


@Repository
public interface ReroutingRepository extends CrudRepository<Rerouting, Long> {
    List<Rerouting> findByMissionId(Long missionId);
}
