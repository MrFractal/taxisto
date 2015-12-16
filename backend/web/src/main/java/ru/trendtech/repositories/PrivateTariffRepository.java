package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.PrivateTariff;
import ru.trendtech.domain.PromoCodeExclusive;

import java.util.List;

/**
 * Created by petr on 02.03.2015.
 */
@Repository
public interface PrivateTariffRepository extends CrudRepository<PrivateTariff, Long>{
    List<PrivateTariff> findByClient(Client client);
    List<PrivateTariff> findByClientAndActive(Client client, boolean active);
    PrivateTariff findByClientAndTariffName(Client client, String tariffName);
    PrivateTariff findByClientAndTariffNameAndActive(Client client, String tariffName, boolean active);
    PrivateTariff findByClientAndTariffNameAndIsActivated(Client client, String tariffName, boolean activated);
    PrivateTariff findByClientAndTariffNameAndActiveAndIsActivatedAndExpirationDateGreaterThan(Client client, String tariffName, boolean active, boolean activated, DateTime now);
    PrivateTariff findByClientAndTariffNameAndActiveAndExpirationDateGreaterThan(Client client, String tariffName, boolean active, DateTime now);
    List<PrivateTariff> findByTariffNameAndActiveAndExpirationDateLessThan(String tariffName, boolean active, DateTime now);
    PrivateTariff findByClientAndActiveAndPromoExclusive(Client client, boolean active, PromoCodeExclusive exclusive);
    PrivateTariff findByClientAndActiveAndIsActivated(Client client, boolean active, boolean activated);
}
