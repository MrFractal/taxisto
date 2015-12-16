package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.PartnerAccount;

/**
 * Created by petr on 24.03.2015.
 */
@Repository
public interface PartnerAccountRepository extends CrudRepository<PartnerAccount, Long>{
    PartnerAccount findByLoginAndPasswordAndActive(String login, String password, boolean active);
    PartnerAccount findByToken(String token);
}
