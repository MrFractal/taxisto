package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Estimate;
import ru.trendtech.domain.Mission;

import java.util.List;

/**
 * Created by petr on 19.01.2015.
 */
@Repository
public interface EstimateRepository extends CrudRepository<Estimate, Long> {
    public Estimate findByMission(Mission mission);
    public List<Estimate> findByDriverAndMissionIsNotNullOrderByEstimateDateDesc(Driver driver); // не только по водителю, но еще и комплитед
    public List<Estimate> findByDriverOrderByEstimateDateDesc(Driver driver);
    public List<Estimate> findByClientOrderByEstimateDateDesc(Client client);
}
