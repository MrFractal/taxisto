package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.Item;

import java.util.List;

/**
 * Created by petr on 25.08.2015.
 */
@Repository
public interface ItemRepository extends CrudRepository<Item, Long>{
}
