package ru.trendtech.services.watchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.domain.ClientCard;
import ru.trendtech.domain.MDOrder;
import ru.trendtech.repositories.MdOrderRepository;
import ru.trendtech.repositories.billing.ClientCardRepository;
import ru.trendtech.services.billing.BillingService;

import java.util.List;

/**
 * Created by petr on 28.10.2014.
 */

@Service("paymenetRefundWatcher")
public class PaymenetRefundWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymenetRefundWatcher.class);

    @Autowired
    BillingService billingService;

    @Autowired
    ClientCardRepository clientCardRepository;

    @Autowired
    MdOrderRepository mdOrderRepository;


    @Scheduled(fixedRate = 120000)
    @Transactional
    public void paymenetRefundChecker() {
        // список заказов, по которым мы не вернули рубль

//        ClientCard clientCard =  clientCardRepository.findByMdOrderNumber("86a86e98-f444-43f8-9f40-18fefc0ceb94");
//          if(clientCard!=null){
//              ErrorCodeHelper errorCodeHelper = billingService.refundPayment(clientCard);
//          }

        //86a86e98-f444-43f8-9f40-18fefc0ceb94

        /* for 1 ruble*/
        List<ClientCard> clientCardList = clientCardRepository.findByRefundStatusIsNullAndPaymentStatusIsNull();
           if(clientCardList!=null && !clientCardList.isEmpty()){
                 for(ClientCard clientCard: clientCardList){
                           if(clientCard!=null){
                               billingService.refundPayment(clientCard);
                           }
                 }
           }
        /* end */

        /*update status payment for mdOrder*/
        List<MDOrder> mdOrderList = mdOrderRepository.findByRefundStatusIsNullAndPaymentStatusIsNull();
        if(mdOrderList!=null && !mdOrderList.isEmpty()){
            for(MDOrder mdOrder: mdOrderList){
                if(mdOrder!=null){
                    billingService.updatePaymentStatusByMDOrder(mdOrder);
                }
            }
        }
        /*end*/


     }
}
