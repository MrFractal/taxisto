package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.PromoCodeExclusive;

import java.util.List;

/**
 * Created by petr on 10.03.2015.
 */
@Repository
public interface PromoCodeExclusiveRepository extends CrudRepository<PromoCodeExclusive, Long>{
    @Query("select p from PromoCodeExclusive p where p.availableUsedCount <> p.usedCount and lower(p.promoCode) = ?1")
    PromoCodeExclusive findPromoCodeExclusive(String promo);

    PromoCodeExclusive findByPromoCodeIgnoreCase(String promo);

    @Query("select p from PromoCodeExclusive p where p.availableUsedCount <> p.usedCount")
    List<PromoCodeExclusive> findListPromoCodeExclusive();

    List<PromoCodeExclusive> findByDateOfIssueIsNull();
}
