package ru.trendtech.controllers.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.trendtech.models.asterisk.*;
import ru.trendtech.services.asterisk.AsteriskService;
import ru.trendtech.services.asterisk.CommunicationException;
import ru.trendtech.services.asterisk.RequestSenderToAsterisk;

import java.util.Arrays;

@Controller
@RequestMapping
class AsteriskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsteriskController.class);

    @Autowired
    private AsteriskService asteriskService;

    @Autowired
    private RequestSenderToAsterisk requestSenderToAsterisk;

    @RequestMapping(value = "/asterisk", method = RequestMethod.POST)
    public
    @ResponseBody
    AsteriskResponse handleAction(@RequestBody AsteriskRequest request) {
        AsteriskResponse response;
        response = execute(request);
        return response;
    }

    private AsteriskResponse execute(AsteriskRequest request) {
        AsteriskResponse result = null;
        switch (request.getActionType()) {
            case CLIENT_NAME:
                ClientNameResponse clientNameResponse = new ClientNameResponse(request);
                String name = asteriskService.getName(request.getPhone());
                clientNameResponse.setName(name);
                result = clientNameResponse;
                break;
            case DETAILED_BILLING:
                DetailedBillingResponse detailedBillingResponse = new DetailedBillingResponse(request);
                detailedBillingResponse.setDetails("detailed\nmultiline\nbilling report");
                result = detailedBillingResponse;
                break;
            case DETAILED_BILLING_INIT:
                break;
            case DRIVER_ARRIVING_TIME:
                DriverArrivingTimeResponse arrivingTimeResponse = new DriverArrivingTimeResponse(request);
                arrivingTimeResponse.setTimes(Arrays.asList(15, 10));
                result = arrivingTimeResponse;
                break;
            case SEARCH_DRIVER_ID:
                DriverIdResponse driverIdResponse = new DriverIdResponse(request);
                driverIdResponse.setDriversIds(Arrays.asList("123456", "456123"));
                result = driverIdResponse;
                break;
            case TRIP_COST:
                TripCostResponse tripCostResponse = new TripCostResponse(request);
                tripCostResponse.setCost(750);
                tripCostResponse.setFrom("ул. Красный проспект, 1");
                tripCostResponse.setTo("ул. Ленина, 17");
                result = tripCostResponse;
                break;
            case DRIVER_REGISTERED:
                break;
            case CLIENT_REGISTERED:
                break;
            case TEST_SERVER_CALLS:
                String clientPhone = "+79833158988";
                String driverPhone = "123456";
                try {
                    requestSenderToAsterisk.initiateBillingDetails(clientPhone);
                } catch (CommunicationException e) {
                    LOGGER.error("Problem on call to Asterisk", e);
                }
                try {
                    requestSenderToAsterisk.registeredClient(clientPhone);
                } catch (CommunicationException e) {
                    LOGGER.error("Problem on call to Asterisk", e);
                }
                try {
                    requestSenderToAsterisk.registeredDriver(driverPhone);
                } catch (CommunicationException e) {
                    LOGGER.error("Problem on call to Asterisk", e);
                }
                result = new AsteriskResponse(request);
                break;
            default:
        }
        return result;
    }

}
