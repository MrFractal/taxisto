package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverLocation;
import ru.trendtech.domain.PayTerminals;

import java.util.List;

/**
 * Created by petr on 25.08.14.
 */
@Repository
public interface PayTerminalRepository extends CrudRepository<PayTerminals, Long> {

}
