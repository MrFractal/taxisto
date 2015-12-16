package ru.trendtech.repositories.billing;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.billing.MissionRate;

/**
 * File created by max on 08/05/2014 15:36.
 */

@Repository
public interface MissionRatesRepository extends CrudRepository<MissionRate, Long> {

}