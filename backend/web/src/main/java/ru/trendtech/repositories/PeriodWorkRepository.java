package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.PeriodWork;

/**
 * Created by petr on 27.04.2015.
 */
@Repository
public interface PeriodWorkRepository extends CrudRepository<PeriodWork, Long>{
     PeriodWork findByStartPeriodAndEndPeriod(DateTime start, DateTime end);
}
