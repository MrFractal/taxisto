package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.CorporateClientCashFlow;
import ru.trendtech.domain.Mission;

import java.util.List;

/**
 * Created by petr on 07.04.2015.
 */
@Repository
public interface CorporateClientCashFlowRepository extends CrudRepository<CorporateClientCashFlow, Long>{
    /* сколько накатал клиент за период */
    @Query("select sum(abs(c.sum)) from CorporateClientCashFlow c where c.client =?1 and c.dateOperation between ?2 and ?3 and c.operation in(4)")
    Integer findSumMoneyAmountByDateTime(Client client, DateTime start, DateTime end);

    List<CorporateClientCashFlow> findByMission(Mission mission);
}
