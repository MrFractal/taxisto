package ru.trendtech.repositories.courier;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.courier.ActivationQueue;

import java.util.List;

/**
 * Created by petr on 02.09.2015.
 */
@Repository
public interface ActivationQueueRepository extends PagingAndSortingRepository<ActivationQueue, Long>{
    ActivationQueue findByClient(Client client);
    ActivationQueue findByClientAndTimeOfActivationIsNull(Client client);
    List<ActivationQueue> findByTimeOfActivationIsNullOrderByTimeOfRequestAsc();
}
