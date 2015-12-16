package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.TariffRestriction;

import java.util.List;

/**
 * Created by petr on 11.03.2015.
 */
@Repository
public interface TariffRestrictionRepository extends CrudRepository<TariffRestriction, Long>{
   List<TariffRestriction> findByTariffNameAndActiveAndHoliday(String name, boolean active, boolean holiday);

   @Query("select count(t) from TariffRestriction t")
   int countRestriction();
}
