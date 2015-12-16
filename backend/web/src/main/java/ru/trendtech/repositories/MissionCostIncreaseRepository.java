package ru.trendtech.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.billing.MissionCostIncrease;

import java.util.List;

/**
 * Created by petr on 29.08.14.
 */

public interface MissionCostIncreaseRepository extends PagingAndSortingRepository<MissionCostIncrease, Long> {

    MissionCostIncrease findByMission(Mission mission);

}
