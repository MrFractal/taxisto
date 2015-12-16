package ru.trendtech.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;
import ru.trendtech.models.asterisk.*;

import static ru.trendtech.controllers.ProfilesUtils.asteriskUrl;

/**
 * Created by ivanenok on 4/14/2014.
 */

@Test
public class AsteriskControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsteriskControllerTest.class);

    private static final RestTemplate TEMPLATE = Utils.template();

    @Test
    public void testActionClientName() throws Exception {
        AsteriskRequest request = buildRequest(AsteriskRequest.ActionType.CLIENT_NAME);
        printRequest(request);
        ClientNameResponse response = TEMPLATE.postForObject(asteriskUrl(), request, ClientNameResponse.class);
        printResponse(response);
//        String asString = ((MappingJackson2HttpMessageConverter) TEMPLATE.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(response);
    }

    @Test
    public void testActionDetailedBilling() throws Exception {
        AsteriskRequest request = buildRequest(AsteriskRequest.ActionType.DETAILED_BILLING);
        printRequest(request);
        DetailedBillingResponse response = TEMPLATE.postForObject(asteriskUrl(), request, DetailedBillingResponse.class);
        printResponse(response);
//        String asString = ((MappingJackson2HttpMessageConverter) TEMPLATE.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(response);
    }

    @Test
    public void testActionDriverArrivedTime() throws Exception {
        AsteriskRequest request = buildRequest(AsteriskRequest.ActionType.DRIVER_ARRIVING_TIME);
        printRequest(request);
        DriverArrivingTimeResponse response = TEMPLATE.postForObject(asteriskUrl(), request, DriverArrivingTimeResponse.class);
        printResponse(response);
//        String asString = ((MappingJackson2HttpMessageConverter) TEMPLATE.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(response);
    }

    @Test
    public void testActionSearchDriver() throws Exception {
        AsteriskRequest request = buildRequest(AsteriskRequest.ActionType.SEARCH_DRIVER_ID);
        printRequest(request);
        DriverIdResponse response = TEMPLATE.postForObject(asteriskUrl(), request, DriverIdResponse.class);
        printResponse(response);
//        String asString = ((MappingJackson2HttpMessageConverter) TEMPLATE.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(response);
    }

    @Test
    public void testActionTripCost() throws Exception {
        AsteriskRequest request = buildRequest(AsteriskRequest.ActionType.TRIP_COST);
        printRequest(request);
        TripCostResponse response = TEMPLATE.postForObject(asteriskUrl(), request, TripCostResponse.class);
        printResponse(response);
//        String asString = ((MappingJackson2HttpMessageConverter) TEMPLATE.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(response);
    }

    @Test
    public void testCallsFromServer() throws Exception {
        AsteriskRequest request = buildRequest(AsteriskRequest.ActionType.TEST_SERVER_CALLS);
        printRequest(request);
        AsteriskResponse response = TEMPLATE.postForObject(asteriskUrl(), request, AsteriskResponse.class);
        printResponse(response);
    }

    private AsteriskRequest buildRequest(AsteriskRequest.ActionType actionType) {
        AsteriskRequest result = new AsteriskRequest();
        result.setAction(actionType.getAction());
        result.setPhone("+79833158988");
        result.setDate("20140512");
        return result;
    }

    private void printRequest(AsteriskRequest request) throws JsonProcessingException {
        String asString = ((MappingJackson2HttpMessageConverter) TEMPLATE.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        LOGGER.debug("request JSON: \n {}", asString);
    }

    private void printResponse(AsteriskResponse response) throws JsonProcessingException {
        String asString = ((MappingJackson2HttpMessageConverter) TEMPLATE.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(response);
        LOGGER.debug("response JSON: \n {}", asString);
    }
}
