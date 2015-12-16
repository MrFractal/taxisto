package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Assistant;
import ru.trendtech.domain.News;

import java.util.List;

/**
 * Created by petr on 01.12.2014.
 */
@Repository
public interface NewsRepository extends CrudRepository<News, Long> {
    List<News> findByTimeOfFinishingGreaterThan(DateTime startDate, org.springframework.data.domain.Pageable pageable);
    Page findAll(org.springframework.data.domain.Pageable pageable);
    //SELECT n FROM News n where n.timeOfStarting = (select max(n2.timeOfStarting) from News n2) and n.timeOfStarting between ?1 and ?2
    @Query("SELECT n FROM News n where n.timeOfStarting between ?1 and ?2 order by n.timeOfStarting desc")
    Page<News> freshNews(DateTime start, DateTime end, Pageable pageable);
}
