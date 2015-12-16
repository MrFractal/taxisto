package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Reason;

import java.util.List;
//import ru.trendtech.domain.Reason;

/**
 * Created by petr on 12.05.2015.
 */
@Repository
public interface ReasonRepository extends CrudRepository<Reason, Long>{
    Reason findByIdAndToDriver(long id, boolean toDriver);
    List<Reason> findByToDriver(boolean toDriver);
}
