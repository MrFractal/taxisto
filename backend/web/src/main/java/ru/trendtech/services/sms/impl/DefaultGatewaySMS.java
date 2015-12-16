package ru.trendtech.services.sms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.trendtech.services.sms.GatewaySMS;

/**
 * Created by max on 15.02.14.
 */

public class DefaultGatewaySMS implements GatewaySMS {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGatewaySMS.class);

    @Override
    public boolean send(String phone, String message, String messageId) {
        LOGGER.info("NO SMS SENT!!! DEFAULT IMPLEMENTATION USED!!!");
        return true;
    }
}
