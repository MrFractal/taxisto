package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.trendtech.domain.MDOrder;
import ru.trendtech.domain.MissionAddresses;

/**
 * Created by petr on 19.09.2014.
 */
public interface MissionAddressesRepository extends CrudRepository<MissionAddresses, Long> {
}
