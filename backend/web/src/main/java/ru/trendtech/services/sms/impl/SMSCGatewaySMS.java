package ru.trendtech.services.sms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.trendtech.services.sms.GatewaySMS;
import ru.trendtech.services.sms.SMSC;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by max on 15.02.14.
 */
@Component
public class SMSCGatewaySMS implements GatewaySMS {
    private static final Logger LOGGER = LoggerFactory.getLogger(SMSCGatewaySMS.class);

//    @Value("${smsc.username}")
//    private String login;
//
//    @Value("${smsc.password}")
//    private String password;
//
//    @Value("${smsc.debug}")
//    private boolean debug = false;

    @Autowired
    SMSC smsc;

    @Override
    public boolean send(String phone, String message, String messageId) {
        boolean result = false;
        //GatewaySMSC gatewaySMSC = new GatewaySMSC(login, password);
        String[] strings = smsc.send(phone, message, 0, "", messageId, 0, "", ""); // send(String phones, String message, int translit, String time, String id, int format, String sender, String query) {

        if (strings != null) {
            if (strings.length == 4) {
                result = true;
            }
        }
        return result;
    }


}
