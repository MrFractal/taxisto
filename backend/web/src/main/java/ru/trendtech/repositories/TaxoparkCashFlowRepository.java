package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.TaxoparkCashFlow;

/**
 * Created by petr on 20.07.2015.
 */
@Repository
public interface TaxoparkCashFlowRepository extends CrudRepository<TaxoparkCashFlow, Long>{
}
