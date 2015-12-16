package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.TaxoparkPartners;

import java.util.List;

/**
 * Created by petr on 13.10.2014.
 */
@Repository
public interface TaxoparkPartnersRepository extends CrudRepository<TaxoparkPartners, Long> {
}