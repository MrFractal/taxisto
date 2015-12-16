package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.ImageSource;

/**
 * Created by petr on 06.07.2015.
 */
@Repository
public interface ImageSourceRepository extends CrudRepository<ImageSource, Long>{
}
