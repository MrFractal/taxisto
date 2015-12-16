package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.ClientAvailableActivatePromoCode;

/**
 * Created by petr on 12.10.2014.
 */
@Repository
public interface ClientAvailableActivatePromoCodeRepository extends CrudRepository<ClientAvailableActivatePromoCode, Long> {
    ClientAvailableActivatePromoCode findByClientId(long clientId);
}

