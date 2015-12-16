package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.CorporateClientLimit;

import java.util.List;

/**
 * Created by petr on 05.03.2015.
 */
@Repository
public interface CorporateClientLimitRepository extends CrudRepository<CorporateClientLimit, Long>{
    //List<CorporateClientLimit> findByClient(Client client);
    List<CorporateClientLimit> findByClient(Client client);
    CorporateClientLimit findByClientAndPeriod(Client client, CorporateClientLimit.Period period);
    //CorporateClientLimit findByClientAndPeriod(Client client, CorporateClientLimit.Period period);
    //List<CorporateClientLimit> findByMainClient(Client mainClient);
}
