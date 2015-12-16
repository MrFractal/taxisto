package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.dgis.DGISAddress;
import ru.trendtech.domain.dgis.DGISStreet;

import java.util.List;

/**
 * Created by petr on 07.10.2014.
 */

@Repository
public interface DGISAddressRepository extends CrudRepository<DGISAddress, Long> {
}
