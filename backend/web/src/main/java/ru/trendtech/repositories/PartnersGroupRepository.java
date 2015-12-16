package ru.trendtech.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.PartnersGroup;
import java.util.List;

/**
 * Created by petr on 19.09.2014.
 */
public interface PartnersGroupRepository extends PagingAndSortingRepository<PartnersGroup, Long> {
      List<PartnersGroup> findBySection(String section);
}