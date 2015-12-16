package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.domain.AutoClassCostExample;

import java.util.List;

/**
 * Created by petr on 19.06.2015.
 */
@Repository
public interface AutoClassCostExampleRepository extends CrudRepository<AutoClassCostExample, Long>{
      List<AutoClassCostExample> findByAutoClass(AutoClass autoClass);
}
