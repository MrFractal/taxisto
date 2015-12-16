package ru.trendtech.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.AlternativeStatistics;
import java.util.List;

/**
 * Created by petr on 02.02.2015.
 */
@Repository
public interface AlternativeStatisticsRepository extends PagingAndSortingRepository<AlternativeStatistics, Long> {
     List<AlternativeStatistics> findByTypeStatValue(String missionId, Pageable pageable);
}
