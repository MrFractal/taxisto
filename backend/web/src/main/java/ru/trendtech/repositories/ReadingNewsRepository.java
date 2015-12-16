package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.News;
import ru.trendtech.domain.ReadingNews;

/**
 * Created by petr on 01.12.2014.
 */

@Repository
public interface ReadingNewsRepository extends CrudRepository<ReadingNews, Long>{
    ReadingNews findByNewsAndDriver(News news, Driver driver);
}
