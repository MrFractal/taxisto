package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.ClientSumPromoCode;

import java.util.List;

/**
 * Created by petr on 12.10.2014.
 */
@Repository
public interface ClientSumPromoCodeRepository extends CrudRepository<ClientSumPromoCode, Long> {
    ClientSumPromoCode findByClientIdAndAvailableAmount(long clientId, int amount);
    List<ClientSumPromoCode> findByClientId(long clientId);

}
