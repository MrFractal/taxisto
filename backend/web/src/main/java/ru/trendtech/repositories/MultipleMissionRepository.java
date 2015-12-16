package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.MultipleMission;

/**
 * Created by petr on 29.04.2015.
 */
@Repository
public interface MultipleMissionRepository extends CrudRepository<MultipleMission, Long> {
}
