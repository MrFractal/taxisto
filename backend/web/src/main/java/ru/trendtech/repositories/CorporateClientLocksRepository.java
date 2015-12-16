package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.CorporateClientLocks;

/**
 * Created by petr on 19.03.2015.
 */
@Repository
public interface CorporateClientLocksRepository extends CrudRepository<CorporateClientLocks, Long>{
    CorporateClientLocks findByClientId(long clientId);
    CorporateClientLocks findByClientAndTimeOfUnlockIsNull(Client client);
}
