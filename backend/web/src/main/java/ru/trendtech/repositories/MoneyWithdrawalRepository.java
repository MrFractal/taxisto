package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.MoneyWithdrawal;

/**
 * Created by petr on 07.10.2014.
 */

@Repository
public interface MoneyWithdrawalRepository extends CrudRepository<MoneyWithdrawal, Long> {
    MoneyWithdrawal findBySmsCode(String code);
    MoneyWithdrawal findByDriver(Driver driver);
}
