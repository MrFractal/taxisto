package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.ClientActivatedPromoCodes;
import ru.trendtech.domain.ClientAvailableActivatePromoCode;
import ru.trendtech.domain.PromoCodes;

import java.util.List;

/**
 * Created by petr on 12.10.2014.
 */
@Repository
public interface ClientActivatedPromoCodesRepository extends CrudRepository<ClientActivatedPromoCodes, Long> {
    List<ClientActivatedPromoCodes> findByClientId(long clientId);
    ClientActivatedPromoCodes findByPromoCodeIdAndClientId(long promocodeId, long clientId);
    List<ClientActivatedPromoCodes> findByPromoCodeId(long promocodeId);
    List<ClientActivatedPromoCodes> findByClientIdAndDateOfUsedBetween(long clientId, long dateStart, long dateEnd);
    List<ClientActivatedPromoCodes> findByDateOfUsedBetweenAndPromoCodeIdNot(long dateStart, long dateEnd, long promoCodeId);
    @Query("select count(c) from ClientActivatedPromoCodes c where c.promoCodeId <> -1")
    int findCountClientActivatedPromoCodes();
}
