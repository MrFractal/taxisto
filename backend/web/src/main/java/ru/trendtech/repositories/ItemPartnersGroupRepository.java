package ru.trendtech.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.ItemPartnersGroup;
import ru.trendtech.domain.PartnersGroup;

/**
 * Created by petr on 19.09.2014.
 */
@Repository
public interface ItemPartnersGroupRepository extends PagingAndSortingRepository<ItemPartnersGroup, Long> {
}
