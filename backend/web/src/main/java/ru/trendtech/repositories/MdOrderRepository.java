package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.*;

import java.util.List;

/**
 * Created by petr on 17.09.2014.
 */

@Repository
public interface MdOrderRepository extends CrudRepository<MDOrder, Long> {
    MDOrder findByMission(Mission mission);
    List<MDOrder> findByClient(Client client);
    List<MDOrder> findByRefundStatusIsNullAndPaymentStatusIsNull();
}
