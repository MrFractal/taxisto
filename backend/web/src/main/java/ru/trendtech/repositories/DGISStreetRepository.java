package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.dgis.DGISStreet;
import java.util.List;

/**
 * Created by petr on 06.10.2014.
 */
@Repository
public interface DGISStreetRepository extends CrudRepository<DGISStreet, Long> {
    List<DGISStreet> findByNameStreetLike(String nameStreet); // IgnoreCase
    List<DGISStreet> findByNameStreetContainingIgnoreCase(String nameStreet); // IgnoreCase
}
