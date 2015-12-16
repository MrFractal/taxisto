package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.SendSmsLock;

import java.util.List;

/**
 * Created by petr on 10.02.2015.
 */
@Repository
public interface SendSmsLockRepository extends CrudRepository<SendSmsLock, Long>{
    SendSmsLock findByTimeOfUnlockIsNullAndClient(Client client);
    List<SendSmsLock> findByTimeOfUnlockIsNull();
}
