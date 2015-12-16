package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverRequisite;

import java.util.List;

/**
 * Created by petr on 26.01.2015.
 */
@Repository
public interface DriverRequisiteRepository extends CrudRepository<DriverRequisite, Long>{
   List<DriverRequisite> findByDriverAndActiveAndTypeDismissal(Driver driver, boolean active, int type);
   List<DriverRequisite> findByActiveAndTypeDismissal(boolean active, int type);
   DriverRequisite findByIdAndDriver(Long id, Driver driver);
   List<DriverRequisite> findByDriverAndActive(Driver driver, boolean active);
   List<DriverRequisite> findByDriver(Driver driver);
   DriverRequisite findByDriverAndActiveAndSalaryPriority(Driver driver, boolean active, int salaryPriority);
   DriverRequisite findByIdAndActive(long id, boolean active);
}
