package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Comission;
import ru.trendtech.domain.TaxoparkPartners;

import java.util.List;

/**
 * Created by petr on 17.12.2014.
 */
@Repository
public interface ComissionRepository  extends CrudRepository<Comission, Long> {
    Comission findByComissionTypeAndObjectId(int comissionType, Long objectId);
    List<Comission> findByComissionType(int comissionType);
}
