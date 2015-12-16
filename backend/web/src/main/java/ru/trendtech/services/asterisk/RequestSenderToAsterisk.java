package ru.trendtech.services.asterisk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.trendtech.models.asterisk.AsteriskRequest;
import ru.trendtech.services.http.AbstractHttpService;
import ru.trendtech.services.http.HttpServiceAPIException;

/**
 * Created by ivanenok on 4/13/2014.
 */

@Component
public class RequestSenderToAsterisk extends AbstractHttpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestSenderToAsterisk.class);

    public void initiateBillingDetails(String phone) throws CommunicationException {
        AsteriskRequest request = buildRequest(phone, AsteriskRequest.ActionType.DETAILED_BILLING_INIT);
        sendRequest(request);
    }

    public void registeredDriver(String phone) throws CommunicationException {
        AsteriskRequest request = buildRequest(phone, AsteriskRequest.ActionType.DRIVER_REGISTERED);
        sendRequest(request);
    }

    public void registeredClient(String phone) throws CommunicationException {
        AsteriskRequest request = buildRequest(phone, AsteriskRequest.ActionType.CLIENT_REGISTERED);
        sendRequest(request);
    }

    private void sendRequest(AsteriskRequest request) throws CommunicationException {
        try {
            doPost(request.getActionType().getRequestUrl(), request);
        } catch (HttpServiceAPIException e) {
            throw new CommunicationException("Problem on initiating sending SMS with billing info to client with phone: " + request.getPhone(), e);
        }
    }

    private AsteriskRequest buildRequest(String phone, AsteriskRequest.ActionType actionType) {
        AsteriskRequest request = new AsteriskRequest();
        request.setAction(actionType.getAction());
        request.setPhone(phone);
        return request;
    }
}
