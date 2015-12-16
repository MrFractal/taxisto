package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.TypeWindow;
import ru.trendtech.domain.courier.CustomWindow;

/**
 * Created by petr on 03.09.2015.
 */
@Repository
public interface CustomWindowRepository extends CrudRepository<CustomWindow, Long> {
      CustomWindow findByTypeWindow(TypeWindow typeWindow);
}
