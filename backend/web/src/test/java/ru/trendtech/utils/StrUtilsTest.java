package ru.trendtech.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Created by petr on 10.02.14.
 */
public class StrUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(StrUtilsTest.class);
    @Test
    public void testGenerateSMSCode() throws Exception {
        for (int i = 0; i < 5; i++) {
            String code = StrUtils.generateSMSCode();
            LOGGER.info("sms code = " + code);
        }
    }

    @Test
    public void testGenerateDriverLogin() throws Exception {
        for (int i = 0; i < 5; i++) {
            String login = StrUtils.generateDriverLogin();
            LOGGER.info("driver login = " + login);
        }
    }
}
