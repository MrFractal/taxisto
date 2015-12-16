package ru.trendtech.controllers.partners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.trendtech.common.mobileexchange.model.partners.*;
import ru.trendtech.services.partners.PartnersService;

/*
  Created by petr on 24.03.2015.
*/

@Controller
@RequestMapping("/partner")
@Transactional
public class PartnersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PartnersController.class);
    @Autowired
    private PartnersService partnersService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public
    @ResponseBody
    PartnerLoginResponse partnerLogin(@RequestBody PartnerLoginRequest request) {
        return partnersService.partnerLogin(request.getLogin(), request.getPassword());
    }


    @RequestMapping(value = "/update/eventsPartners", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateEventPartnerSiteResponse updateEventPartners(@RequestBody UpdateEventPartnerSiteRequest request) {
        return partnersService.updateEventPartners(request.getSecurity_token(), request.getEventPartnerInfos());
    }


    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public
    @ResponseBody
    EventPartnerSiteResponse eventsPartners(@RequestBody EventPartnerSiteRequest request) {
        return partnersService.eventsPartners(request.getSecurity_token(), request.getNumberPage(), request.getSizePage(), request.getFuture(), request.getPublished(), request.getNameMask(), request.getEventId());
    }

}
