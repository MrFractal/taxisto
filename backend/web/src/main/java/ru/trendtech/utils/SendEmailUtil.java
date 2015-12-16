package ru.trendtech.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.InternetAddress;
import java.util.Collection;

/**
 * Created by petr on 20.10.2014.
 */


public class SendEmailUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailUtil.class);



    public static void sendEmail(String to, String htmlBody, String subject) {
           try {
               HtmlEmail email = new HtmlEmail();
               email.setStartTLSEnabled(true);
               email.setSSLCheckServerIdentity(true);
               email.setHostName("smtp.yandex.ru");
               email.setAuthentication("no-reply@taxisto.ru", "SyU52hJ");
               email.setSmtpPort(25);
               email.setFrom("no-reply@taxisto.ru", "Taxisto");
               email.addTo(to);
               email.setSubject(subject);
               email.setCharset(org.apache.commons.mail.EmailConstants.UTF_8);
               email.setHtmlMsg(htmlBody);
               email.send();
           }catch(EmailException email){
               LOGGER.info("email exception = " + email.getMessage());
           }
    }




}
