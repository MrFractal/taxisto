package ru.trendtech.repositories.courier;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.courier.CourierClientCard;
import ru.trendtech.domain.courier.PaymentState;
import java.util.List;

/**
 * Created by petr on 24.09.2015.
 */
@Repository
public interface CourierClientCardRepository extends PagingAndSortingRepository<CourierClientCard, Long>{
      List<CourierClientCard> findByClient(Client client);
      List<CourierClientCard> findByClientAndActive(Client client, boolean active, org.springframework.data.domain.Pageable pageable);
      List<CourierClientCard> findByClientAndActive(Client client, boolean active);
      List<CourierClientCard> findByClientAndIsDeleteAndCardNumberIsNotNullAndRebillAnchorIsNotNullAndPaymentState(Client client, boolean delete, PaymentState paymentState);
}
