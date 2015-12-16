package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Region;

import java.util.List;

/**
 * Created by petr on 29.03.2015.
 */
@Repository
public interface RegionRepository extends CrudRepository<Region, Long>{
    List<Region> findByIsActive(boolean isActive);
}
