package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Router;

/**
 * Created by petr on 30.06.2015.
 */
@Repository
public interface RouterRepository extends PagingAndSortingRepository<Router,Long>{
    @Query("select count(r) from Router r")
    int countRouters();
}
