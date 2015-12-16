package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverPeriodWork;

import java.util.List;

/**
 * Created by petr on 30.01.2015.
 */
@Repository
public interface DriverPeriodWorkRepository extends CrudRepository<DriverPeriodWork, Long>{
    DriverPeriodWork findByStartWorkBeforeAndEndWorkAfterAndDriver(DateTime n1, DateTime n2, Driver driver);

    //DriverPeriodWork findByStartWorkBetweenAndDriverAndActive(DateTime start, DateTime end, Driver driver, boolean active, Pageable pageable);
    Page<DriverPeriodWork> findByStartWorkBeforeAndEndWorkAfterAndDriverAndActive(DateTime start, DateTime end, Driver driver, boolean active, Pageable pageable);

    Page<DriverPeriodWork> findByEndWorkAfterAndDriverAndActive(DateTime end, Driver driver, boolean active, Pageable pageable);

    List<DriverPeriodWork> findByDriver(Driver driver);
}
