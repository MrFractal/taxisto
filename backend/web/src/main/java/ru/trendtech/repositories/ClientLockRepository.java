package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.trendtech.domain.ClientLocks;

/**
 * Created by petr on 08.10.2014.
 */
public interface ClientLockRepository extends PagingAndSortingRepository<ClientLocks, Long> {
    ClientLocks findByClientId(long clientId);
    ClientLocks findByClientIdAndTimeOfUnlockIsNull(long clientId);

    @Query("select count(d) from ClientLocks d where d.clientId = ?1")
    public long findCountByClientId(long clientId);
}
