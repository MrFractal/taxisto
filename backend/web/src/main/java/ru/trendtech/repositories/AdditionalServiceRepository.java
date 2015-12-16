package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.AdditionalService;

import java.util.List;

/**
 * Created by petr on 07.07.2015.
 */
@Repository
public interface AdditionalServiceRepository extends CrudRepository<AdditionalService, Long> {
     List<AdditionalService> findByIdIn(List<Long> ids);
}
