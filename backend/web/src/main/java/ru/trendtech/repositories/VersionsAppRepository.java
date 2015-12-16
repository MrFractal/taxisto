package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.VersionsApp;

import java.util.List;

/**
 * Created by petr on 07.11.2014.
 */
@Repository
public interface VersionsAppRepository extends CrudRepository<VersionsApp, Long> {
    VersionsApp findByVersionAndClientType(String version, String clientType);
    List<VersionsApp> findByClientType(String clientType);
}
