package ru.trendtech.repositories.courier;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.PromoCodeUses;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by petr on 06.11.2015.
 */
@Repository
public interface PromoCodeUsesRepository extends PagingAndSortingRepository<PromoCodeUses, Long> {
      List<PromoCodeUses> findByClientIdOrderByDateOfUseDesc(long clientId, Pageable pageable);
}
