package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.ClientWifiConnection;
import ru.trendtech.domain.Mission;

/**
 * Created by petr on 06.07.2015.
 */
@Repository
public interface ClientWifiConnectionRepository extends CrudRepository<ClientWifiConnection , Long>{
    ClientWifiConnection findByMission(Mission mission);
    ClientWifiConnection findByMissionAndTimeOfClosingIsNull(Mission mission);
}
