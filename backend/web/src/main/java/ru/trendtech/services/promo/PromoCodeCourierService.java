package ru.trendtech.services.promo;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.courier.PromoCodeCourier;
import ru.trendtech.domain.courier.PromoCodeUses;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.repositories.courier.PromoCodeCourierRepository;
import ru.trendtech.repositories.courier.PromoCodeUsesRepository;
import ru.trendtech.services.client.ClientCourierService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.utils.DateTimeUtils;

import java.util.List;

/**
 * Created by petr on 30.10.2015.
 */
@Service(value = "promoCourier")
@Transactional
public class PromoCodeCourierService implements PromoCodeServiceI {
    private static final Logger LOGGER = LoggerFactory.getLogger(PromoCodeCourierService.class);
    @Autowired
    private PromoCodeCourierRepository promoCodeCourierRepository;
    @Autowired
    private PromoCodeUsesRepository promoCodeUsesRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientCourierService clientCourierService;
    @Autowired
    private CommonService commonService;


    @Override
    public void generatePromoCode(int count, int amount, String channel, long fromClientId, int capacity, int availableUseCount) {

    }



    private PromoCodeUses buildPromoCodeUses(Client client, PromoCodeCourier promoCodeCourier){
        PromoCodeUses promoCodeUses = new PromoCodeUses();
        promoCodeUses.setPromoCode(promoCodeCourier);
        promoCodeUses.setClient(client);
        promoCodeUses.setDateOfUse(DateTimeUtils.nowNovosib_GMT6());
        return promoCodeUses;
    }





    @Override
    public void activatePromo(long clientId, long promoId, String value) {
        PromoCodeCourier promoCodeCourier;
        if(promoId != 0){
            promoCodeCourier = promoCodeCourierRepository.findOne(promoId);
        } else {
            promoCodeCourier = promoCodeCourierRepository.findByValue(value);
        }
        if(promoCodeCourier == null){
            throw new CustomException(1, "Промокод не найден");
        }
        if(promoCodeCourier.getUseCountInFact() >= promoCodeCourier.getAvailableUseCount()){
            throw new CustomException(2, "Превышено количество использований промокода");
        }

        // todo: вы активировали данный промокод ранее

        List<PromoCodeUses> promoCodesUses = promoCodeUsesRepository.findByClientIdOrderByDateOfUseDesc(clientId, new PageRequest(0, 1));
        Client client = clientRepository.findOne(clientId);

        if(CollectionUtils.isEmpty(promoCodesUses)){
            promoCodeUsesRepository.save(buildPromoCodeUses(client, promoCodeCourier));

            promoCodeCourier.setUseCountInFact(promoCodeCourier.getUseCountInFact() + 1);
            promoCodeCourierRepository.save(promoCodeCourier);

            clientCourierService.updateBonuses(client, promoCodeCourier.getAmount());
        } else {
            PromoCodeUses promoCodeUses = promoCodesUses.get(0);
            int countDay = Integer.parseInt(commonService.getPropertyValue("count_day_after_last_activation_promo"));

            DateTime lastActivationPromo = promoCodeUses.getDateOfUse();
            lastActivationPromo = lastActivationPromo.plusDays(countDay);

            if(!lastActivationPromo.isBeforeNow()){
                throw new CustomException(4, "Невозможно активировать промокод");
            }


        }
        /* todo: лезу в таблицу активаций, смотрю когда в последний раз юзер активировал промо, прибавляю к этому времени кол-во дней из проперти, если текущая дата входит в этот диапазон - активация запрещена */
    }




    @Override
    public List<PromoCode> getListPromo() {
        return null;
    }
}
