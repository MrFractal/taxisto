package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.ClientCountPromoCode;

/**
 * Created by petr on 12.10.2014.
 */
@Repository
public interface ClientCountPromoCodeRepository extends CrudRepository<ClientCountPromoCode, Long> {
    ClientCountPromoCode findByClientId(long clientId);
}
