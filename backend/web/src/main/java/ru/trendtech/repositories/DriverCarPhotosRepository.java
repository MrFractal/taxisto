package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverCarPhotos;

import java.util.List;

/**
 * Created by petr on 17.06.2015.
 */
@Repository
public interface DriverCarPhotosRepository extends CrudRepository<DriverCarPhotos, Long>{
    List<DriverCarPhotos> findByDriver(Driver driver);
}
