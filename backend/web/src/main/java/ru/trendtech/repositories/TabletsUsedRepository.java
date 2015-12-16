package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Tablet;
import ru.trendtech.domain.TabletsUsed;

/**
 * Created by petr on 22.06.2015.
 */
@Repository
public interface TabletsUsedRepository extends CrudRepository<TabletsUsed, Long>{
    TabletsUsed findByDriverAndEndUsedIsNull(Driver driver);
    TabletsUsed findByTabletAndEndUsedIsNull(Tablet tablet);
    TabletsUsed findByTabletAndDriverNotAndEndUsedIsNull(Tablet tablet, Driver driver);
}
