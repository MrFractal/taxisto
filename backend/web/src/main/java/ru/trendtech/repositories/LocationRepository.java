package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverLocation;
import ru.trendtech.domain.Mission;

import java.util.List;

@Repository
public interface LocationRepository extends CrudRepository<DriverLocation, Long> {
    //@Query("select d from DriverLocation d where d.driver.state = 'AVAILABLE'")
    //public List<DriverLocation> findAvailableDriver();
    List<DriverLocation> findByDriverIn(List<Driver> drivers);
    List<DriverLocation> findByDriverOrderByIdDesc(Driver driver);
    DriverLocation findByDriverId(long driverId);
    DriverLocation findByDriver(Driver driver);
    List<DriverLocation> findByMissionAndDriverTypeX(Mission mission, boolean typeX);
    DriverLocation findByDriverAndMission(Driver driver, Mission mission);
    List<DriverLocation> findByMissionIsNotNull();
}