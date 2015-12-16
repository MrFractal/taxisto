package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.StoreAddress;

/**
 * Created by petr on 26.08.2015.
 */
@Repository
public interface StoreAddressRepository extends CrudRepository<StoreAddress, Long>{
}
