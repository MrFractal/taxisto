package ru.trendtech.repositories.courier;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.PromoCodeCourier;


/**
 * Created by petr on 30.10.2015.
 */
@Repository
public interface PromoCodeCourierRepository extends PagingAndSortingRepository<PromoCodeCourier, Long> {
       PromoCodeCourier findByValue(String value);
}
