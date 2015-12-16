package ru.trendtech.repository;

//import com.trend4web.backend.accounting.domain.Account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;
import ru.trendtech.domain.Driver;
import ru.trendtech.repositories.DriverRepository;

/**
 * User: Petr Rudenko
 * Date: 4/4/11
 * Time: 6:48 PM
 */
@ContextConfiguration("classpath:/META-INF/application-context.xml")
@TestExecutionListeners({TransactionalTestExecutionListener.class})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class DriverRepoTest extends AbstractTestNGSpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverRepoTest.class);

    @Autowired
    private DriverRepository service;

    @Test
    public void testCreateDriver() throws Exception {
        for (int i = 0; i < 11; i++) {
            Driver driver = new Driver();
            driver.setFirstName("First Name " + i);
            driver.setLastName("Last Name " + i);
//            driver.set("First Name " + i);
            driver.setFirstName("First Name " + i);
            driver.setFirstName("First Name " + i);
            driver.setFirstName("First Name " + i);
            driver.setFirstName("First Name " + i);
            driver.setFirstName("First Name " + i);
            service.save(driver);
//            LOGGER.debug("service.findAllAccounts() = " + allAccounts.size());
        }
//        LOGGER.debug("service.findAllAccounts() = " + service.findAllAccounts().size());
    }

    @Test
    public void testAccountManagement() throws Exception {
//    Account account = new Account("login 1", "password");
//    account = service.createAccount(account);
//
//    CustomerProfile profile = new CustomerProfile();
//    profile = service.createProfile(profile);
//
//    LOGGER.debug("profiles count in account " + account + " is " + account.getProfiles());
    }

    @Test
    public void testfindAccountByName() throws Exception {
//    Account account = service.findAccountByName("login 1");
//    LOGGER.info("Account loaded: " + account);
    }
}
