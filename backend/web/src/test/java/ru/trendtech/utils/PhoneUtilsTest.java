package ru.trendtech.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Created by petr on 10.02.14.
 */
public class PhoneUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneUtilsTest.class);
    @Test
    public void testNormalizeNumber() throws Exception {
//        LOGGER.info(PhoneUtils.normalizeNumber("9833158988"));
//        LOGGER.info(PhoneUtils.normalizeNumber("79833158988"));
//        LOGGER.info(PhoneUtils.normalizeNumber("+79833158988"));
//        LOGGER.info(PhoneUtils.normalizeNumber("89833158988"));
        LOGGER.info(PhoneUtils.normalizeNumber("+73838759656"));
    }

    @Test
    public void testValidNumber() throws Exception {
        LOGGER.info(PhoneUtils.normalizeNumber("123456"));
    }

}
