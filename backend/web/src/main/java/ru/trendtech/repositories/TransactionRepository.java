package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.billing.Transactions;

@Repository
public interface TransactionRepository extends CrudRepository<Transactions, Long> {
}
