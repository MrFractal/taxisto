package ru.trendtech.services.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;


/**
 * Created by ivanenok on 5/9/2014.
 */
@Service
public class ServiceEmailNotification {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEmailNotification.class);

    @Value("${email.defaultFrom}")
    private String defaultFromAddress = "";


    //@Autowired
    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }



    public boolean sendSimpleEmail(String emailTo, String subject, String text ){

        boolean result = false;

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            //add email address validation here

            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom(defaultFromAddress);
            mailSender.send(message);

            result = true;
            //add log
        } catch (Exception e){
            //add log
            e.printStackTrace();
        }
        return result;
    }

    public boolean passwordRecovery(String email, String code) {
        return sendSimpleEmail(email, "Таксисто: смена пароля", String.format("Код для подтверждения смены пароля в Таксисто: %s", code));
    }

    public boolean inviteFriend(String email) {
        return sendSimpleEmail(email, "Отличный способ заказа такси", "Привет! Я использую приложение Таксисто, отличный способ заказа такси. Для установки приложения нажми на ссылку http://taxisto.ru");
    }

}
