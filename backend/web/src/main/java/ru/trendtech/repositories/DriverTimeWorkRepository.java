package ru.trendtech.repositories;

import org.joda.time.LocalDate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverTimeWork;

/**
 * Created by petr on 12.05.2015.
 */
@Repository
public interface  DriverTimeWorkRepository extends CrudRepository<DriverTimeWork, Long>{
    DriverTimeWork findByDriverAndWorkDate(Driver driver, LocalDate date);
}
