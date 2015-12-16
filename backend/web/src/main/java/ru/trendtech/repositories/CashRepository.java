package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverCashFlow;
import ru.trendtech.domain.DriverPeriodWork;
import ru.trendtech.domain.Mission;

import java.util.List;

@Repository
public interface CashRepository extends CrudRepository<DriverCashFlow, Long> {
    List<DriverCashFlow> findByDriverOrderByIdDesc(DriverCashFlow driverCash);
    DriverCashFlow findByDriverAndOperation(Driver driver, int operation);
    DriverCashFlow findByMissionAndOperationAndDriver(Mission mission, int operation, Driver driver);
    DriverCashFlow findByDriverAndDriverPeriodWorkAndOperation(Driver driver, DriverPeriodWork driverPeriodWork, int operation);
    List<DriverCashFlow> findByMission(Mission mission);
}
