package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.TestEntityWithCustomGenerateId;

/**
 * Created by petr on 24.09.2015.
 */
@Repository
public interface TestEntityWithCustomGenerateIdRepository extends CrudRepository<TestEntityWithCustomGenerateId, Long>{
}
