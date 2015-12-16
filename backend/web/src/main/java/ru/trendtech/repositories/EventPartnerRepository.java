package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.EventPartner;

import java.util.List;

/**
 * Created by petr on 29.01.2015.
 */
@Repository
public interface EventPartnerRepository extends CrudRepository<EventPartner, Long>{
    List<EventPartner> findByTimeOfEventAfterOrderByTimeOfEventAsc(DateTime now);
}
