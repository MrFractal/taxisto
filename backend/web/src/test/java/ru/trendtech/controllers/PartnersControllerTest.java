package ru.trendtech.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.common.EventPartnerSiteInfo;
import ru.trendtech.common.mobileexchange.model.partners.EventPartnerSiteRequest;
import ru.trendtech.common.mobileexchange.model.partners.EventPartnerSiteResponse;
import ru.trendtech.common.mobileexchange.model.partners.UpdateEventPartnerSiteRequest;
import ru.trendtech.common.mobileexchange.model.partners.UpdateEventPartnerSiteResponse;
import ru.trendtech.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import static ru.trendtech.controllers.ProfilesUtils.partnerUrl;

/**
 * Created by petr on 25.03.2015.
 */
@ContextConfiguration("classpath:/META-INF/application-context.xml")
@TestExecutionListeners({TransactionalTestExecutionListener.class})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class PartnersControllerTest {


    @Test
    public void updateEventsPartners() throws Exception {
        UpdateEventPartnerSiteRequest request = new UpdateEventPartnerSiteRequest();
        request.setSecurity_token("zzz");
        List<EventPartnerSiteInfo> list = new ArrayList<>();
        EventPartnerSiteInfo info = new EventPartnerSiteInfo();
        info.setPublished(true);
        info.setPartnerId(1);
        info.setEventId(2L);
        info.setEventName("xxxxx");
        info.setTimeOfEvent(DateTimeUtils.nowNovosib_GMT6().getMillis());
        //List<String> listPic = new ArrayList<>();
        //listPic.add(ProfilesUtils.getPicture("driver.jpg"));
        //info.setPhotosEventsPictures(listPic);
        list.add(info);
        request.setEventPartnerInfos(list);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        UpdateEventPartnerSiteResponse response = Utils.template().postForObject(partnerUrl("/update/eventsPartners"), request, UpdateEventPartnerSiteResponse.class);
        response.getErrorMessage();
    }


    @Test
    public void listEventsPartner() throws Exception {
        EventPartnerSiteRequest request = new EventPartnerSiteRequest();
        request.setSecurity_token("8b984173bd1407c9fe85c8603f2ea4ab");
        request.setFuture(false);
        request.setNumberPage(0);
        request.setSizePage(2);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        EventPartnerSiteResponse response = Utils.template().postForObject(partnerUrl("/events"), request, EventPartnerSiteResponse.class);
        response.getEventList();
        response.getTotalItems();
        response.getLastPageNumber();
    }
}
