package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverSetting;

/**
 * Created by petr on 03.04.2015.
 */
@Repository
public interface DriverSettingRepository extends CrudRepository<DriverSetting, Long>{
    DriverSetting findByDriver(Driver driver);
}
