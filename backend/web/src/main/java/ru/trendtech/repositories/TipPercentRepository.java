package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.TipPercent;

/**
 * Created by petr on 06.02.2015.
 */
@Repository
public interface TipPercentRepository extends CrudRepository<TipPercent, Long>{
}
