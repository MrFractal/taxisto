package ru.trendtech.services.partners;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.trendtech.common.mobileexchange.model.client.corporate.CorporateLoginResponse;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.common.EventPartnerInfo;
import ru.trendtech.common.mobileexchange.model.common.EventPartnerSiteInfo;
import ru.trendtech.common.mobileexchange.model.partners.EventPartnerSiteResponse;
import ru.trendtech.common.mobileexchange.model.partners.PartnerLoginResponse;
import ru.trendtech.common.mobileexchange.model.partners.UpdateEventPartnerSiteResponse;
import ru.trendtech.common.mobileexchange.model.web.AdministratorRole;
import ru.trendtech.common.mobileexchange.model.web.EventPartnerARMResponse;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.EventPartner;
import ru.trendtech.domain.PartnerAccount;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.domain.billing.Account;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.EventPartnerRepository;
import ru.trendtech.repositories.PartnerAccountRepository;
import ru.trendtech.repositories.WebUserRepository;
import ru.trendtech.services.resources.ProfilesResourcesService;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.PhoneUtils;
import ru.trendtech.utils.StrUtils;
import ru.trendtech.utils.TokenUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by petr on 24.03.2015.
 */
@Service
@Transactional
public class PartnersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PartnersService.class);
    @Autowired
    PartnerAccountRepository partnerAccountRepository;
    @Autowired
    WebUserRepository webUserRepository;
    @Autowired
    EventPartnerRepository eventPartnerRepository;
    @Autowired
    private ProfilesResourcesService profilesResourcesService;
    @PersistenceContext
    EntityManager entityManager;



    public PartnerLoginResponse partnerLogin(String login, String password) {
        String phoneNormalized = PhoneUtils.normalizeNumber(login);
        if(phoneNormalized == null){
            throw new CustomException(1, "Phone: "+login+" isn't normalized");
        }
        PartnerAccount partner = partnerAccountRepository.findByLoginAndPasswordAndActive(login, password, Boolean.TRUE);
        if(partner == null){
            throw new CustomException(2, "Login or password incorrect or user is not active");
        }
        PartnerLoginResponse response = new PartnerLoginResponse();
        String token = TokenUtil.getMD5("fractal" + StrUtils.generateAlphaNumString(5));
        partner.setToken(token);
        partnerAccountRepository.save(partner);
        response.setSecurity_token(token);
        response.setPartnerId(partner.getId());
        response.setFirstName(partner.getFirstName());
        return response;
    }



    private EventPartner updateEventPictures(EventPartner eventPartner, List<String> photosEventsPictures){
        if (photosEventsPictures!=null && !photosEventsPictures.isEmpty()) {
            List<String> pictureUrls = profilesResourcesService.saveEventPictures(photosEventsPictures, eventPartner.getPartner().getId());
            eventPartner.getPhotosEventsUrl().addAll(pictureUrls);
            eventPartner = eventPartnerRepository.save(eventPartner);
        }
         return eventPartner;
    }





    public UpdateEventPartnerSiteResponse updateEventPartners(String security_token, List<EventPartnerSiteInfo> eventPartnerInfos){
        PartnerAccount partner = partnerAccountRepository.findByToken(security_token);
        if(partner == null){
            throw new CustomException(1,"Партнер не найден");
        }
        UpdateEventPartnerSiteResponse response = new UpdateEventPartnerSiteResponse();
        List<EventPartner> list = new ArrayList<>();
        for(EventPartnerSiteInfo info :eventPartnerInfos){
            if(info.getEventId() == 0){
                // add
                EventPartner eventPartner = ModelsUtils.fromModel(info, partner);
                eventPartnerRepository.save(eventPartner);

                eventPartner = updateEventPictures(eventPartner, info.getPhotosEventsPictures());
                list.add(eventPartner);
            }else{
                EventPartner eventPartnerUpd = eventPartnerRepository.findOne(info.getEventId());
                if(eventPartnerUpd == null){
                    throw new CustomException(5,"Event id: "+info.getEventId()+" not found");
                }else{
                    // upd
                    eventPartnerUpd = ModelsUtils.fromModelUpd(info, eventPartnerUpd);
                    eventPartnerUpd = updateEventPictures(eventPartnerUpd, info.getPhotosEventsPictures());
                    list.add(eventPartnerUpd);
                }
            }
        }
        eventPartnerRepository.save(list);
        return response;
    }





    public EventPartnerSiteResponse eventsPartners(String security_token, int numberPage, int sizePage, Boolean future, Boolean published, String eventNameMask, Long eventId){
        PartnerAccount partner = partnerAccountRepository.findByToken(security_token);
        if(partner == null){
            throw new CustomException(1,"Партнер не найден");
        }
        EventPartnerSiteResponse response = new EventPartnerSiteResponse();

        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(EventPartner.class);
        Criteria crit = session.createCriteria(EventPartner.class);

        if(eventId!=0){
            criteria.add(Restrictions.eq("id", Long.valueOf(eventId)));
            crit.add(Restrictions.eq("id", Long.valueOf(eventId)));
        }

        if(published!=null){
            criteria.add(Restrictions.eq("published", Boolean.valueOf(published)));
            crit.add(Restrictions.eq("published", Boolean.valueOf(published)));
        }
        if(future!=null){
            if(future){
                criteria.add(Restrictions.ge("timeOfEvent", DateTimeUtils.nowNovosib_GMT6()));
                crit.add(Restrictions.ge("timeOfEvent", DateTimeUtils.nowNovosib_GMT6()));
            }else{
                criteria.add(Restrictions.lt("timeOfEvent", DateTimeUtils.nowNovosib_GMT6()));
                crit.add(Restrictions.lt("timeOfEvent", DateTimeUtils.nowNovosib_GMT6()));
            }
        }
        if(eventNameMask!=null){
            criteria.add(Restrictions.ilike("eventName", "%"+eventNameMask+"%"));
            crit.add(Restrictions.ilike("eventName", "%" + eventNameMask + "%"));
        }
        criteria.addOrder(Order.desc("timeOfEvent"));

        crit.setProjection(Projections.rowCount());
        int count = Integer.valueOf(crit.uniqueResult().toString());
        response.setTotalItems(count);

        int calc = count / sizePage;
        int lastPageNumber = (calc == 0 ? calc : calc+1);
        response.setLastPageNumber(lastPageNumber);
        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<EventPartner> listEventPartners = criteria.list();

        for(EventPartner eventPartner: listEventPartners){
            response.getEventList().add(ModelsUtils.toModelEventPartnerSiteInfo(eventPartner));
        }
        return response;
    }


}
