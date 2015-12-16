package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Transact;
import ru.trendtech.utils.DateTimeUtils;

/**
 * Created by petr on 23.06.2015.
 */
@Repository
public interface TransactRepository extends JpaRepository<Transact, Long> {
       @Query(value = "insert into transact(id, time_requesting) VALUES (?1, ?2)", nativeQuery = true)
       void insertTransact(String id, DateTime dateTime);
}
