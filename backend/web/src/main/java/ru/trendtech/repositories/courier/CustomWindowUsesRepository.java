package ru.trendtech.repositories.courier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.courier.CustomWindow;
import ru.trendtech.domain.courier.CustomWindowUses;

import java.util.List;

/**
 * Created by petr on 03.09.2015.
 */
@Repository
public interface CustomWindowUsesRepository extends CrudRepository<CustomWindowUses, Long>{
     List<CustomWindowUses> findByClientAndIsShowed(Client client, boolean isShowed);
     CustomWindowUses findByCustomWindowAndClient(CustomWindow customWindow, Client client);
}
