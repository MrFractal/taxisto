package ru.trendtech.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.MissionService;
import ru.trendtech.domain.ServicePrice;

import java.util.List;

@Repository
public interface ServicesRepository extends PagingAndSortingRepository<ServicePrice, Long> {
    ServicePrice findByService(MissionService service);
    Iterable<ServicePrice> findByServiceIn(List<MissionService> missionServices);
}
