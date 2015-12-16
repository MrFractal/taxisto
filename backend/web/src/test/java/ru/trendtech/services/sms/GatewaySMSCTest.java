package ru.trendtech.services.sms;

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
import ru.trendtech.services.sms.impl.SMSCGatewaySMS;

/**
 * Created by max on 15.02.14.
 */
@ContextConfiguration("classpath:/META-INF/application-context.xml")
@TestExecutionListeners({TransactionalTestExecutionListener.class})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class GatewaySMSCTest extends AbstractTestNGSpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewaySMSCTest.class);

    @Autowired
    SMSC smsc;

    @Test
    public void testSend() throws Exception {
        String[] strings = smsc.send("+79538695889", "Ваш пароль: 123", 0, "", "", 0, "", "");
        for (String s : strings) {
            LOGGER.info(s);
        }

    }

    @Test
    public void testCost() throws Exception {

    }

    @Test
    public void testStatus() throws Exception {

    }

    @Test
    public void testBalance() throws Exception {

    }
}
