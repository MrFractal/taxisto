package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.PromoCodes;

import java.util.List;

/**
 * Created by petr on 21.08.14.
 */
@Repository
public interface PromoCodeRepository extends CrudRepository<PromoCodes, Long> {
    PromoCodes findByPromoCodeIgnoreCase(String text);

    @Query("select p from PromoCodes p where p.availableUsedCount <> p.usedCount and p.fromId is null")
    List<PromoCodes> findFreePromoCodes();

    List<PromoCodes> findByDateOfIssueBetweenAndFromIdNot(long dateStart, long dateEnd, long fromId); // все кроме юзера 10  - флаер юзер

    List<PromoCodes> findByDateOfIssueBetween(long dateStart, long dateEnd);

    PromoCodes findByPromoCode(String text);

    List<PromoCodes> findByFromIdIsNotNull();

    List<PromoCodes> findByFromIdIsNotNullAndFromIdNot(long fromId);

    List<PromoCodes> findByFromIdAndDateOfIssueBetween(long clientFromId, long dateStart, long dateEnd);
    List<PromoCodes> findByToIdAndDateOfIssueBetween(long clientToId, long dateStart, long dateEnd);
  }
