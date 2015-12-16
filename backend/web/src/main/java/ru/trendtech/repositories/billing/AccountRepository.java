package ru.trendtech.repositories.billing;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.billing.Account;

/**
 * Created by petr on 06.02.14.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
}
