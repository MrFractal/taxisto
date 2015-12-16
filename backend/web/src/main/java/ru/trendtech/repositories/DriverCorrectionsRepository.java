package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.DriverCorrections;

/**
 * Created by petr on 16.01.2015.
 */
@Repository
public interface DriverCorrectionsRepository  extends CrudRepository<DriverCorrections, Long> {
}
