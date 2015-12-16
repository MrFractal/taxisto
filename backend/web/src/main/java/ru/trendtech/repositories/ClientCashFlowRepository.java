package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.ClientCashFlow;
import ru.trendtech.domain.Mission;

import java.util.List;

/**
 * Created by petr on 13.01.2015.
 */
@Repository
public interface ClientCashFlowRepository extends CrudRepository<ClientCashFlow, Long> {
    List<ClientCashFlow> findByMission(Mission mission);
    List<ClientCashFlow> findByMissionAndClientAndOperation(Mission mission, Client client, int operation);

    /* сколько за текущий месяц накатал клиент по корпоративной карте или балансу*/
    @Query("select sum(c.sum) from ClientCashFlow c where c.client =?1 and  c.corporateAccount=?2 and c.dateOperation between ?3 and ?4 and c.operation in(7)")
    Integer findSumMoneyAmountByDateTime(Client client, Client corporateAccount, DateTime start, DateTime end);
}


