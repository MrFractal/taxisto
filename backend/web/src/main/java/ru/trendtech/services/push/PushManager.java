package ru.trendtech.services.push;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;
import ru.trendtech.domain.DeviceInfo;
import ru.trendtech.integration.push.PushNotificationException;

import java.util.Map;

/**
 * Created by ivanenok on 4/4/2014.
 */
public abstract class PushManager {
    protected static String convertMassage(Object message) throws PushNotificationException {
        String result = null;
        if (!StringUtils.isEmpty(message)) {
            if (!(message instanceof String)) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    result = objectMapper.writeValueAsString(message);
                } catch (JsonProcessingException e) {
                    throw new PushNotificationException(e);
                }
            } else {
                result = (String) message;
            }
        } else {
            throw new PushNotificationException("Empty message can't be sent to device!");
        }
        return result;
    }

    /**
     * for configure the push notification accounts instance or to register the services over the push notification services like APNS OR GCM
     *
     * @param confString
     * @param password
     * @return String
     */
    public abstract boolean configureAccount(String confString, String password, boolean production);

    /**
     * This method actually sent the message to the device and return the status of the notification to the caller resources.
     *
     * @param message
     * @return String
     */
    public abstract void sendMessage(Object message, Map<String, String> param, DeviceInfo... devices) throws PushNotificationException;

}