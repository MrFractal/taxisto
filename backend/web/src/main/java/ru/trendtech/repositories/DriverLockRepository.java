package ru.trendtech.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.DriverLocks;

import java.util.List;

/**
 * Created by petr on 07.08.14.
 */

@Repository
public interface DriverLockRepository extends PagingAndSortingRepository<DriverLocks, Long> {
    DriverLocks findByDriverId(long driverId);

    List<DriverLocks> findByDriverIdAndTimeOfUnlockIsNull(long driverId, Pageable pageable);

    @Query("select count(d) from DriverLocks d where d.driverId = ?1")
    public long findCountByDriverId(long driverId);
}