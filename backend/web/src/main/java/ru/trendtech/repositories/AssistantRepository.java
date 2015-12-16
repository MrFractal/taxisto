package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Assistant;

/**
 * Created by petr on 14.11.2014.
 */
@Repository
public interface AssistantRepository  extends CrudRepository<Assistant, Long>{
}
