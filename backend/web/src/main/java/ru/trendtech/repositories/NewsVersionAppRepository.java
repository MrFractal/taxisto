package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.NewsVersionApp;
import ru.trendtech.domain.VersionsApp;

import java.util.List;

/**
 * Created by petr on 22.01.2015.
 */
@Repository
public interface NewsVersionAppRepository extends CrudRepository<NewsVersionApp, Long>{
    List<NewsVersionApp> findByVersionsAppAndActive(VersionsApp versionApp, boolean active);
}
