package ru.trendtech.repositories.billing;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.ClientCard;
import ru.trendtech.domain.Driver;

import java.util.List;

/**
 * Created by petr on 15.09.2014.
 */

@Repository
public interface ClientCardRepository  extends CrudRepository<ClientCard, Long> {
    List<ClientCard> findByClient(Client client);
    List<ClientCard> findByClientAndStatusDelete(Client client, boolean statusDelete);
    ClientCard findByClientAndStatusDeleteAndActive(Client client, boolean statusDelete, boolean active);
    ClientCard findByClientAndActive(Client client, boolean active);
    List<ClientCard> findByClientAndPanAndStatusDelete(Client client, String pan, boolean statusDelete);
    List<ClientCard> findByRefundStatusIsNullAndPaymentStatusIsNull();
    ClientCard findByMdOrderNumber(String pan);
    //86a86e98-f444-43f8-9f40-18fefc0ceb94
}