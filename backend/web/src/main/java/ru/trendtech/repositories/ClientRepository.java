package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.billing.Account;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    @Query("select count(c) from Client c where c.mainClient = ?1")
    int countUserInCorporateProfile(Client client);

    @Query("select c from Client c where c.mainClient.id = c.id")
    List<Client> mainClientList();

    List<Client> findByIdInAndEmailUnsubscribe(List<Long> clientIds, boolean unsubscribe);

    Client findByEmailAndPassword(String email, String password);

    Client findBySmsCodeAndRegistrationState(String code, Client.RegistrationState state);

    Client findByPhoneAndPasswordAndRegistrationState(String code,String password, Client.RegistrationState state);

    Client findByPhoneAndPasswordAndRegistrationStateAndMainClientNotNull(String code,String password, Client.RegistrationState state);

    Client findByCorporateLoginAndCorporatePasswordAndRegistrationStateAndMainClientNotNull(String login, String password, Client.RegistrationState state);

    Client findByPhoneAndRegistrationState(String phone, Client.RegistrationState state);

    Client findByPhoneAndRegistrationStateAndSmsCode(String phone, Client.RegistrationState state, String sms);

    Client findByPhone(String phone);

    Client findByCorporateLogin(String corporateLogin);

    Client findByPhoneAndPassword(String phone, String password);

    Client findBySmsCode(String code);

    Client findByAccount(Account account);

    Client findByToken(String token);

    List<Client> findByMainClient(Client mainClient);

    List<Client> findByMainClientAndIdNotIn(Client mainClient, List<Long> mainClientId);

    List<Client> findByMainClientIsNotNull();

    List<Client> findByRegistrationTimeBetween(DateTime start, DateTime end);

    List<Client>  findByFirstNameContainingAndPhoneContainingAndEmailContaining(String nameMask, String phoneMask,String emailMask, Pageable pageable);

    @Query("select count(c) from Client c")
    public int findCountClient();


}
