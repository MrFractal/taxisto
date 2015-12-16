package ru.trendtech.services.billing;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.common.mobileexchange.model.client.*;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.billing.paymobile.*;
import ru.trendtech.common.mobileexchange.model.common.rates.*;
import ru.trendtech.common.mobileexchange.model.common.rates.AutoClassRateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfoV2;
import ru.trendtech.common.mobileexchange.model.web.MoneyRefuseResponse;
import ru.trendtech.domain.*;
import ru.trendtech.domain.billing.Account;
import ru.trendtech.domain.billing.AutoClassPrice;
import ru.trendtech.domain.billing.MissionRate;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.*;
import ru.trendtech.repositories.billing.AccountRepository;
import ru.trendtech.repositories.billing.ClientCardRepository;
import ru.trendtech.repositories.billing.MissionRatesRepository;
import ru.trendtech.services.MongoDBServices;
import ru.trendtech.services.client.ClientService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.notifications.node.NodeJsService;
import ru.trendtech.services.TimeService;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.services.validate.ValidatorService;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.MoneyUtils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;


@Service
@Transactional
public class BillingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillingService.class);
    private static final long BONUSES_ACCOUNT_ID = 2;
    private static final long COMPANY_ACCOUNT_ID = 1;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MissionRatesRepository missionRatesRepository;
    @Autowired
    private TimeService timeService;
    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private PromoCodeRepository promoCodeRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ClientCardRepository clientCardRepository;
    @Autowired
    private NodeJsService nodeJsService;
    @Autowired
    private MdOrderRepository mdOrderRepository;
    @Autowired
    private MongoDBServices mongoDBServices;
    @Autowired
    private ClientAvailableActivatePromoCodeRepository clientAvailableActivatePromoCodeRepository;
    @Autowired
    private ClientActivatedPromoCodesRepository clientActivatedPromoCodesRepository;
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private TipPercentRepository tipPercentRepository;
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private NodeJsNotificationsService notificationsService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ValidatorService validatorService;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;

    private boolean result = false;
    private final String DEPOSITED = "2";
    private final String ERROR = "-1";

    /*
    public void registerOrderClient(boolean order, long driverId, long missionId){
            проверил соотв-ет ли пришедший драйвер айди тому драйвер айди который находится в миссии, если да шлю клиенту нотификацию (событие и ответ)
            устанавливаю для миссии состояние paymant_answer_sent

    }
     */




    /* отправить водителю ответ на запрос оплаты по карте с учетом чаевых */
    @Transactional
    public void sentOrderEventFromClientToDriverWithTip(boolean isYes, long clientId, long missionId, String security_token, long tipPercentId, int sumWithoutTipPercent){
        if(!validatorService.validateUser(clientId, security_token, 1)){
            throw new CustomException(3, "Tokens are not equals");
        }
        Mission mission = missionRepository.findOne(missionId);
        MDOrder mdOrderExist = mdOrderRepository.findByMission(mission);
        if(mission==null){
            throw new CustomException(1, "Миссия не найдены");
        }
        Client client = clientRepository.findOne(clientId);
        if(client.getMission() == null || client.getMission().getId() != mission.getId()){
            throw new CustomException(4, "Миссия на клиенте не соответсвует текущей миссии");
        }
        TipPercent tipPercent = tipPercentRepository.findOne(tipPercentId);
        if(tipPercent==null){
            throw new CustomException(5, "Процент чаевых не найден");
        }

        int sumDebt = commonService.clientSumDebt(client);
        LOGGER.debug("%%%%%%%%%%%%%%%%%% sentOrderEventFromClientToDriverWithTip sumWithoutTipPercent: "+sumWithoutTipPercent+" missionId: "+missionId+" clientId: "+clientId+" sumDebt: "+sumDebt);
        if(sumDebt<0){
            sumDebt = Math.abs(sumDebt);
        }
                        if(isYes){

                            /*
                            double sumWithTipPercent = 0;
                            int sumDebt = clientSumDebt(client);
                            LOGGER.debug("%%%%%%%%%%%%%%%%%% sentOrderEventFromClientToDriverWithTip sumWithoutTipPercent: "+sumWithoutTipPercent+" missionId: "+missionId+" clientId: "+clientId+" sumWithTipPercent: "+sumWithTipPercent+" sumDebt: "+sumDebt);
                            if(sumDebt<0){
                                 sumWithTipPercent += Math.abs(sumDebt);
                            }
                            */

                            double sumWithTipPercent = ((sumWithoutTipPercent/100.0) + ((sumWithoutTipPercent/100.0)*(tipPercent.getPercent()/100.0)));

                            double z = sumWithTipPercent % 1;
                            if (z != 0) {
                                z = 1 - z;
                                sumWithTipPercent += z;
                            }


                            /* сохраняем заказ в базе данных */
                            MDOrder mdOrder = saveMdOrderWithTipPercent(mission, mdOrderExist, tipPercent, sumWithoutTipPercent, (int)sumWithTipPercent*100);
                            /* сохраняем заказ в банке*/
                            if(mdOrderExist == null){
                                registerOrderInAlphaBankWithTip(missionId, mission.getClientInfo().getId(), (int)sumWithTipPercent*100, mdOrder);
                            }
                            // шлем водителю ответ - да!
                            mission.setPaymentStateCard(Mission.PaymentStateCard.PAYMENT_CLIENT_YES);
                            notificationsService.paymentCardAnswer(missionId, Boolean.TRUE);
                        }else{
                            // шлем водителю ответ - нет!
                            mission.setPaymentStateCard(Mission.PaymentStateCard.PAYMENT_CLIENT_NO);
                            notificationsService.paymentCardAnswer(missionId, Boolean.FALSE);
                        }
               missionRepository.save(mission);
    }




    @Transactional
    // регистрация заказа в Альфа-Банке
    private void registerOrderInAlphaBankWithTip(long orderNumber, long clientId, int amount, MDOrder mdOrder){
        LOGGER.debug("Registration order in AlphaBank with sum ="+amount+" orderNumber="+orderNumber+" clientId="+clientId);
        String orderNumberStr = Long.toString(orderNumber);
        String clientIdStr = Long.toString(clientId);
        String amountStr = Integer.toString(amount);

        PaymentHelper paymentHelper =new PaymentHelper();
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/register.do");
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("userName", "test"));
            urlParameters.add(new BasicNameValuePair("password", "test"));
            urlParameters.add(new BasicNameValuePair("orderNumber", orderNumberStr));
            urlParameters.add(new BasicNameValuePair("amount", amountStr));
            urlParameters.add(new BasicNameValuePair("clientId", clientIdStr));
            urlParameters.add(new BasicNameValuePair("returnUrl", "finish.html"));
            urlParameters.add(new BasicNameValuePair("pageView", "MOBILE"));

            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = httpClient.execute(postRequest);

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            LOGGER.debug("AlphaBank answer: "+sb.toString());

            JSONObject jObject  = new JSONObject(sb.toString()); // json

            if(jObject.has("orderId")){
                String orderIdStr = jObject.getString("orderId"); // get the name from data.
                if(orderIdStr!=null){
                    paymentHelper.setOrderId(orderIdStr);
                    paymentHelper.setAmount(amountStr);

                    Client client = clientRepository.findOne(clientId);
                    ClientCard clientCard = clientCardRepository.findByClientAndStatusDeleteAndActive(client, false, true);

                    mdOrder.setClientCard(clientCard);
                    //mdOrder.setSum(amount);
                    mdOrder.setMdOrderNumber(orderIdStr);
                    mdOrderRepository.save(mdOrder);
                }else{
                    throw new CustomException(8, "При регистрации заказа в банке произошла ошибка");
                }
            }else{
                throw new CustomException(7, "При регистрации заказа в банке произошла ошибка");
            }
        } catch (IOException e) {
            throw new CustomException(9, "При регистрации заказа в банке произошла ошибка: "+e.toString());
        } catch (org.json.JSONException r) {
            throw new CustomException(10, "При регистрации заказа в банке произошла ошибка: "+r.toString());
        }
    }






    @Transactional
    public boolean sentOrderEventFromClientToDriver(boolean isYes, long clientId, long missionId, String token){
        //проверил соотв-ет ли пришедший клиент айди тому клиент айди который находится в миссии, если да шлю драйверу нотификацию (событие и ответ)
        //устанавливаю для миссии состояние paymant_answer_sent// отсылаю событие водителю

        /*
           добавить сюда сумму чаевых, отправить.
        */

        boolean result =false;
        Client client = clientRepository.findOne(clientId);
        Mission mission = missionRepository.findOne(missionId);

        if(client!=null && mission!=null){
            if (client.getToken() != null && client.getToken().equals(token)) {
               if(client.getMission().getId()==mission.getId()){ // ЕЩЕ ПОСТАВИТЬ ПРОВЕРКУ НА СУММУ
                    boolean isSent = false;
                    try{
                        if(isYes){
                        // шлем водителю ответ - да!
                        mission.setPaymentStateCard(Mission.PaymentStateCard.PAYMENT_CLIENT_YES);
                        JSONObject json = new JSONObject();
                        json.put("missionId", missionId);
                        json.put("agree", Boolean.TRUE);
                        //json.put("isOld", Boolean.TRUE);

                        isSent = notified("send_client_card_payment_decision",json);
                    }else{
                        // шлем водителю ответ - нет!
                        mission.setPaymentStateCard(Mission.PaymentStateCard.PAYMENT_CLIENT_NO);
                        JSONObject json = new JSONObject();
                        json.put("missionId", missionId);
                        json.put("agree", Boolean.FALSE);
                        //json.put("isOld", Boolean.TRUE);
                        isSent = notified("send_client_card_payment_decision",json);
                     }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(isSent){
                        LOGGER.debug("*Event 'send_client_card_payment_decision' send to node, missionId: "+mission.getId());
                    }else{
                        LOGGER.debug("*Event 'send_client_card_payment_decision' NOT SEND, missionId: "+mission.getId());
                    }
                    missionRepository.save(mission);
                    result=true;
                }
              }else{
                LOGGER.info("Tokens are not equal");
            }
        }
        return result;
    }




    // метод для возврата суммы
    public MoneyRefuseResponse startMoneyRefuse(long mdOrderId){
        MoneyRefuseResponse resp = new MoneyRefuseResponse();
        try {

            MDOrder mdOrderRefund = mdOrderRepository.findOne(mdOrderId);

            if(mdOrderRefund!=null) {
                   if(mdOrderRefund.getPaymentStatus()!=null && mdOrderRefund.getPaymentStatus().equals(Boolean.TRUE)) {
                       DefaultHttpClient httpClient = new DefaultHttpClient();
                       HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/refund.do");
                       List<NameValuePair> urlParameters = new ArrayList<>();

                       urlParameters.add(new BasicNameValuePair("userName", "test"));
                       urlParameters.add(new BasicNameValuePair("password", "test"));
                       urlParameters.add(new BasicNameValuePair("amount", Integer.toString(mdOrderRefund.getSum()))); // любая сумма
                       urlParameters.add(new BasicNameValuePair("currency", "810"));
                       urlParameters.add(new BasicNameValuePair("orderId", mdOrderRefund.getMdOrderNumber()));

                       postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
                       HttpResponse response = httpClient.execute(postRequest);

                       BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
                       StringBuilder sb = new StringBuilder();
                       String line;

                       while ((line = br.readLine()) != null) {
                           sb.append(line);
                       }

                       JSONObject jsonObj = new JSONObject(sb.toString());
                       httpClient.getConnectionManager().shutdown();

                       LOGGER.info("refundPayment answer= " + jsonObj);

                       if (jsonObj.has("errorCode")) {
                           String errorCode = jsonObj.get("errorCode").toString();

                           if (errorCode.equals("0")) {
                               mdOrderRefund.setRefundStatus(true);
                               mdOrderRefund.setRefundDescription("Обработка запроса прошла без системных ошибок");
                               mdOrderRefund.setRefundDate(DateTimeUtils.nowNovosib().getMillis());
                           } else if (errorCode.equals("5")) {
                               mdOrderRefund.setRefundStatus(false); // типа не вернули, какая-то трабла возникла
                               mdOrderRefund.setRefundDescription("Ошибка значение параметра запроса");
                               resp.getErrorCodeHelper().setErrorCode(7);
                               resp.getErrorCodeHelper().setErrorMessage("Ошибка значение параметра запроса");
                           } else if (errorCode.equals("6")) {
                               mdOrderRefund.setRefundStatus(false);
                               mdOrderRefund.setRefundDescription("Незарегистрированный OrderId");
                               resp.getErrorCodeHelper().setErrorCode(8);
                               resp.getErrorCodeHelper().setErrorMessage("Незарегистрированный OrderId");
                           } else if (errorCode.equals("7")) {
                               mdOrderRefund.setRefundStatus(false);
                               mdOrderRefund.setRefundDescription("Системная ошибка");
                               resp.getErrorCodeHelper().setErrorCode(9);
                               resp.getErrorCodeHelper().setErrorMessage("Системная ошибка");
                           }
                           mdOrderRepository.save(mdOrderRefund);
                       } else {
                           resp.getErrorCodeHelper().setErrorCode(1);
                           resp.getErrorCodeHelper().setErrorMessage("В JSON не найден ключ errorCode");
                       }
                   }else{
                       resp.getErrorCodeHelper().setErrorCode(6);
                       resp.getErrorCodeHelper().setErrorMessage("По данному mdOrder оплата не произведена");
                   }
            }else{
                resp.getErrorCodeHelper().setErrorCode(5);
                resp.getErrorCodeHelper().setErrorMessage("Не найден mdOrder");
            }
            return  resp;
        } catch (IOException e) {
            e.printStackTrace();
            resp.getErrorCodeHelper().setErrorCode(2);
            resp.getErrorCodeHelper().setErrorMessage("Ошибка: "+e.getMessage());
            return resp;
        } catch (JSONException j) {
            j.printStackTrace();
            resp.getErrorCodeHelper().setErrorCode(4);
            resp.getErrorCodeHelper().setErrorMessage("Ошибка: "+j.getMessage());
            return resp;
        }
    }





     public ErrorCodeHelper startRefuseRubleMoney(ErrorCodeHelper errorCodeHelper, ClientCard clientCard){
         try {
             DefaultHttpClient httpClient = new DefaultHttpClient();
             HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/reverse.do"); // refund.do
             List<NameValuePair> urlParameters = new ArrayList<>();

             urlParameters.add(new BasicNameValuePair("userName", "test"));
             urlParameters.add(new BasicNameValuePair("password", "test"));
             //urlParameters.add(new BasicNameValuePair("amount", "100")); // 1 рубль
             //urlParameters.add(new BasicNameValuePair("currency", "810"));
             urlParameters.add(new BasicNameValuePair("orderId", clientCard.getMdOrderNumber()));

             postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
             HttpResponse response = httpClient.execute(postRequest);

             BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
             StringBuilder sb = new StringBuilder();
             String line = null;

             while ((line = br.readLine()) != null) {
                 sb.append(line);
             }

             JSONObject jsonObj = new JSONObject(sb.toString());
             httpClient.getConnectionManager().shutdown();

             LOGGER.info("reversePayment answer= " + jsonObj);
                          /*
                              0 Обработка запроса прошла без системных ошибок
                              5 Ошибка значение параметра запроса
                              6 Незарегистрированный OrderId
                              7 Системная ошибка
                           */
             if (jsonObj.has("errorCode")) {
                 String errorCode = jsonObj.get("errorCode").toString();
                 clientCard.setRefundDate(DateTimeUtils.nowNovosib().getMillis());
                 if (errorCode.equals("0")) {
                     clientCard.setRefundStatus(true);
                     clientCard.setRefundDescription("Обработка запроса прошла без системных ошибок");
                 } else if (errorCode.equals("5")) {
                     clientCard.setRefundStatus(false); // типа не вернули, какая-то трабла возникла
                     clientCard.setRefundDescription("Ошибка значение параметра запроса");
                 } else if (errorCode.equals("6")) {
                     clientCard.setRefundStatus(false);
                     clientCard.setRefundDescription("Незарегистрированный OrderId");
                 } else if (errorCode.equals("7")) {
                     clientCard.setRefundStatus(false);
                     clientCard.setRefundDescription("Системная ошибка");
                 }

                 clientCardRepository.save(clientCard);
             } else {
                 errorCodeHelper.setErrorCode(1);
                 errorCodeHelper.setErrorMessage("В JSON не найден ключ errorCode");
             }
             return errorCodeHelper;

         } catch (IOException e) {
             e.printStackTrace();
             return errorCodeHelper;
         } catch (JSONException j) {
             j.printStackTrace();
             return errorCodeHelper;
         }
     }





    // заполняем статус по оплате миссии
    @Transactional
    public void updatePaymentStatusByMDOrder(MDOrder mdOrderRefund) {
        //{"depositAmount":0,"currency":"810","authCode":2,"clientId":"642","ErrorCode":"2","ErrorMessage":"Платеж отклонен","OrderStatus":6,"OrderNumber":"4774","Amount":15000}
        OrderStatusResponse orderStatusResponse = getOrderStatusFull(mdOrderRefund.getMdOrderNumber());

        if (orderStatusResponse.getOrderStatus() != null) {
            if (orderStatusResponse.getOrderStatus().equals("2")) {
                // DEPOSITED
                mdOrderRefund.setPaymentStatus(true);
                mdOrderRefund.setPaymentDescription("Проведена полная авторизация суммы заказа");
                mdOrderRepository.save(mdOrderRefund);
            } else if (orderStatusResponse.getOrderStatus().equals("6")) {
                // DECLINED
                mdOrderRefund.setPaymentStatus(false);
                mdOrderRefund.setPaymentDescription("Авторизация отклонена");
            } else if (orderStatusResponse.getOrderStatus().equals("0")) {
                // CREATED
                //mdOrderRefund.setPaymentStatus(false);
                mdOrderRefund.setPaymentDescription("Заказ зарегистрирован, но не оплачен");
            } else if (orderStatusResponse.getOrderStatus().equals("1")) {

                //Предавторизованная сумма захолдирована (для двухстадийных платежей)
                //mdOrderRefund.setPaymentStatus(true);
                mdOrderRefund.setPaymentDescription("Предавторизованная сумма захолдирована (для двухстадийных платежей");
                mdOrderRefund.setPaymentStatus(true);
                mdOrderRepository.save(mdOrderRefund);
            } else if (orderStatusResponse.getOrderStatus().equals("3")) {
                mdOrderRefund.setPaymentStatus(false);
                mdOrderRefund.setPaymentDescription("Авторизация отменена");
            } else if (orderStatusResponse.getOrderStatus().equals("4")) {
                mdOrderRefund.setPaymentStatus(false);
                mdOrderRefund.setPaymentDescription("По транзакции была проведена операция возврата");
            } else if (orderStatusResponse.getOrderStatus().equals("5")) {
                mdOrderRefund.setPaymentStatus(false);
                mdOrderRefund.setPaymentDescription("Инициирована авторизация через ACS банка-эмитента");
            }
        }else{
            if(orderStatusResponse.getErrorCode().equals("6")){
                mdOrderRefund.setPaymentDescription("Незарегистрированный OrderId");
                mdOrderRefund.setRefundStatus(false);
                mdOrderRefund.setPaymentStatus(false);
            }else if(orderStatusResponse.getErrorCode().equals("5")){
                mdOrderRefund.setPaymentDescription("Доступ запрещён или [orderId] не указан");
                mdOrderRefund.setRefundStatus(false);
                mdOrderRefund.setPaymentStatus(false);
            }else if(orderStatusResponse.getErrorCode().equals("2")){
                mdOrderRefund.setPaymentDescription("Заказ отклонен по причине ошибки в реквизитах платежа");
                mdOrderRefund.setRefundStatus(false);
                mdOrderRefund.setPaymentStatus(false);
            }else if(orderStatusResponse.getErrorCode().equals("0")){
                mdOrderRefund.setPaymentDescription("Обработка запроса прошла без системных ошибок");
                mdOrderRefund.setPaymentStatus(true);
            }
        }
        mdOrderRepository.save(mdOrderRefund);
    }










     // возврат рубля
     @Transactional
     public ErrorCodeHelper refundPayment(ClientCard clientCard) {
            ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();
            LOGGER.info("clientCard id = " + clientCard.getId() + " clientCard mdOrder = " + clientCard.getMdOrderNumber());

            // не проверять если уже у нее true или false
            // возврат делаем только если true (deposited)

        OrderStatusResponse orderStatusResponse = getOrderStatusFull(clientCard.getMdOrderNumber());

        if (orderStatusResponse.getOrderStatus() != null) {
              if (orderStatusResponse.getOrderStatus().equals("2")) {
                // DEPOSITED
                clientCard.setPaymentStatus(true);
                clientCard.setPaymentDescription("Проведена полная авторизация суммы заказа");
                clientCardRepository.save(clientCard);
                errorCodeHelper  = startRefuseRubleMoney(errorCodeHelper, clientCard);

            } else if (orderStatusResponse.getOrderStatus().equals("6")) {
                // DECLINED
                // jsonAnswer.put("answer", "Авторизация отклонена");
                clientCard.setPaymentStatus(false);
                clientCard.setPaymentDescription("Авторизация отклонена");
            } else if (orderStatusResponse.getOrderStatus().equals("0")) {
                // CREATED
                clientCard.setPaymentStatus(false);
                clientCard.setPaymentDescription("Заказ зарегистрирован, но не оплачен");
            } else if (orderStatusResponse.getOrderStatus().equals("1")) {
                //Предавторизованная сумма захолдирована (для двухстадийных платежей)
                clientCard.setPaymentStatus(true);
                clientCard.setPaymentDescription("Предавторизованная сумма захолдирована (для двухстадийных платежей");
                clientCardRepository.save(clientCard);
                errorCodeHelper  = startRefuseRubleMoney(errorCodeHelper, clientCard);
            } else if (orderStatusResponse.getOrderStatus().equals("3")) {
                clientCard.setPaymentStatus(false);
                clientCard.setPaymentDescription("Авторизация отменена");
            } else if (orderStatusResponse.getOrderStatus().equals("4")) {
                clientCard.setPaymentStatus(false);
                clientCard.setPaymentDescription("По транзакции была проведена операция возврата");
            } else if (orderStatusResponse.getOrderStatus().equals("5")) {
                clientCard.setPaymentStatus(false);
                clientCard.setPaymentDescription("Инициирована авторизация через ACS банка-эмитента");
            }
        }else{
            /*
    0 Обработка запроса прошла без системных ошибок
    2 Заказ отклонен по причине ошибки в реквизитах платежа
    5 Доступ запрещён
    5 Пользователь должен сменить свой пароль
    5 [orderId] не указан
    6 Незарегистрированный OrderId
             */

              if(orderStatusResponse.getErrorCode().equals("6")){
                  clientCard.setPaymentDescription("Незарегистрированный OrderId");
                  clientCard.setRefundStatus(false);
                  clientCard.setPaymentStatus(false);
              }else if(orderStatusResponse.getErrorCode().equals("5")){
                  clientCard.setPaymentDescription("Доступ запрещён или [orderId] не указан");
                  clientCard.setRefundStatus(false);
                  clientCard.setPaymentStatus(false);
              }else if(orderStatusResponse.getErrorCode().equals("2")){
                  clientCard.setPaymentDescription("Заказ отклонен по причине ошибки в реквизитах платежа");
                  clientCard.setRefundStatus(false);
                  clientCard.setPaymentStatus(false);
              }else if(orderStatusResponse.getErrorCode().equals("0")){
                  clientCard.setPaymentDescription("Обработка запроса прошла без системных ошибок");
                  clientCard.setPaymentStatus(true);
                  errorCodeHelper  = startRefuseRubleMoney(errorCodeHelper, clientCard);
              }
        }
                  clientCardRepository.save(clientCard);
               return errorCodeHelper;
        }






    @Transactional
    public AskClientForPaymentCardResponse askClientForPaymentCard(long driverId, long missionId, int sum, String security_token) {
        AskClientForPaymentCardResponse response = new AskClientForPaymentCardResponse();
        Driver driver = driverRepository.findOne(driverId);
        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //throw new CustomException(2, "Водитель не найден");
        //}

        Mission mission = missionRepository.findOne(missionId);
        Client client = mission.getClientInfo();
        if (mission == null) {
            throw new CustomException(2, "Миссия не найдены");
        }
        ClientCard clientCard;
        if(mission.getPaymentType().equals(PaymentType.CORPORATE_CARD)){
            if(mission.getClientInfo().getMainClient()==null){
                throw new CustomException(7, "Вы не являетесь корпоративным клиентом");
            }
            // оплата корпоративной карты
            clientCard = clientService.getActiveClientCard(client.getMainClient());
        }else{
            // оплата собственной картой
            clientCard = clientService.getActiveClientCard(client);
        }
        if (clientCard == null) {
            throw new CustomException(1, "Данный вид заказа станет доступным только после добавления карты в приложение!");
        }
        LOGGER.info("askClientForPaymentCard: driver.getCurrentMission() = " + driver.getCurrentMission() +" != :" + (driver.getCurrentMission().getId() != mission.getId()));
        if (driver.getCurrentMission() == null || (driver.getCurrentMission().getId() != mission.getId())) {
            throw new CustomException(4, "Не указан driverId или driverId не соответсвует миссии");
        }

        /*
        int sumDebt = commonService.clientSumDebt(client);
        if(sumDebt<0){
            sumDebt = Math.abs(sumDebt);
            //notificationsService.driverCustomMessage(driverId, String.format(commonService.getPropertyValue("client_has_debt_message"), sumDebt));
        }
        */

             notificationsService.askClientForCardPayment(mission.getId(), sum); // +sumDebt
             mission.setPaymentStateCard(Mission.PaymentStateCard.WAIT_PAYMENT_CLIENT);
             // НУЖНО ЛИ СЮДА ДОБАВЛЯТЬ СУММУ ДОЛГА, ЕСЛИ КЛИЕНТ СКАЖЕТ НЕТ, ТОГДА В ПРАЙС ИН ФАКТ БУДЕТ КРИВАЯ СУММА
             mission.getStatistics().setPriceInFact(MoneyUtils.getRubles((sum)/100));
             missionRepository.save(mission);
        return response;
    }




    @Transactional
    public RegisterOrderDriver_V2_Response sentOrderEventFromDriverToClient_V2(long driverId, long missionId, int sum) {
        RegisterOrderDriver_V2_Response response = new RegisterOrderDriver_V2_Response();
        Driver driver = driverRepository.findOne(driverId);
        Mission mission = missionRepository.findOne(missionId);
        MDOrder mdOrderExist = mdOrderRepository.findByMission(mission);
        boolean corporate = false;

        if (driver != null && mission != null) {

            ClientCard clientCardActive;

            if(mission.getPaymentType().equals(PaymentType.CORPORATE_CARD)){
                if(mission.getClientInfo().getMainClient() == null){
                    response.getErrorCodeHelper().setErrorCode(6);
                    response.getErrorCodeHelper().setErrorMessage("Вы не являетесь корпоративным клиентом");
                      return response;
                }else{
                    corporate = true;
                    clientCardActive = clientService.getActiveClientCard(mission.getClientInfo().getMainClient());
                }
            }else{
                    clientCardActive = clientService.getActiveClientCard(mission.getClientInfo());
            }

            if (clientCardActive != null) {
                // есть вбитая карточка
                if (driver.getCurrentMission() != null && (driver.getCurrentMission().getId() == mission.getId())) {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("missionId", mission.getId());
                        json.put("amount", sum);
                        notified("ask_client_for_card_payment", json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        response.getErrorCodeHelper().setErrorCode(5);
                        response.getErrorCodeHelper().setErrorMessage("Произошла ошибка при отправке json сообщения клиенту");
                        return response;
                    }
                    mission.setPaymentStateCard(Mission.PaymentStateCard.WAIT_PAYMENT_CLIENT);
                    missionRepository.save(mission);

                    // сохраняем в базе данных
                    MDOrder mdOrderT = saveMdOrder(mission, sum, mdOrderExist, corporate);

                    if (mdOrderT != null && mdOrderT.getId() != -1) {
                        if(mdOrderExist==null) {
                            registerOrderInAlphaBank(missionId, mission.getClientInfo().getId(), sum, mdOrderT);
                        }
                    } else {
                        response.getErrorCodeHelper().setErrorCode(3);
                        response.getErrorCodeHelper().setErrorMessage("Не удалось сохранить MdOrder");
                        return response;
                    }
                }else{
                    response.getErrorCodeHelper().setErrorCode(4);
                    response.getErrorCodeHelper().setErrorMessage("Не указан driverId или driverId не соответсвует миссии");
                    return response;
                }
            } else {
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Данный вид заказа станет доступным только после добавления карты в приложение!");
                return response;
            }
        }else{
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("Не указан driverId или missionId");
            return response;
        }
        return response;
    }





    @Transactional
    public boolean sentOrderEventFromDriverToClient(long driverId, long missionId, int sum) {
        //проверил соотв-ет ли пришедший драйвер айди тому драйвер айди который находится в миссии, если да шлю клиенту нотификацию (событие и ответ)
        //устанавливаю для миссии состояние paymant_answer_sent
        boolean result = false;
        Driver driver = driverRepository.findOne(driverId);
        Mission mission = missionRepository.findOne(missionId);
        MDOrder mdOrderExist = mdOrderRepository.findByMission(mission);

           if (driver != null && mission != null) {
              if (driver.getCurrentMission() != null && (driver.getCurrentMission().getId().equals(mission.getId()))) { // ЕЩЕ ПОСТАВИТЬ ПРОВЕРКУ НА СУММУ
                  boolean corporate = false;
                  ClientCard clientCardActive = null;

                  if(mission.getPaymentType().equals(PaymentType.CORPORATE_CARD)){
                      if(mission.getClientInfo().getMainClient()==null){
                          return false;
                      }else{
                          corporate = true;
                          clientCardActive = clientService.getActiveClientCard(mission.getClientInfo().getMainClient());
                      }
                  }else{
                      clientCardActive = clientService.getActiveClientCard(mission.getClientInfo());
                  }

                  if(clientCardActive == null){
                      return false;
                  }

                try {
                    JSONObject json = new JSONObject();
                    json.put("missionId", mission.getId());
                    json.put("amount", sum);

                    boolean isSent = notified("ask_client_for_card_payment", json);

                    if (isSent) {
                        LOGGER.debug("*Event 'ask_client_for_card_payment' send to node, missionId: " + mission.getId());
                    } else {
                        LOGGER.debug("*Event 'ask_client_for_card_payment' NOT SEND");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mission.setPaymentStateCard(Mission.PaymentStateCard.WAIT_PAYMENT_CLIENT);
                missionRepository.save(mission);
                result = true;

                // сохраняем в базе данных
                MDOrder mdOrderT = saveMdOrder(mission, sum, mdOrderExist, corporate);
                if (mdOrderT != null && mdOrderT.getId() != -1) {
                       if(mdOrderExist==null) {
                           PaymentHelper paymentHelper = registerOrderInAlphaBank(missionId, mission.getClientInfo().getId(), sum, mdOrderT);
                       }
                } else {
                    LOGGER.debug("User active card not found!");
                }
            }
        }else{
               LOGGER.debug("driver == null && mission == null");
           }
          return result;
    }





    private MDOrder saveMdOrderWithTipPercent(Mission mission, MDOrder mdOrder, TipPercent tipPercent, int sumWithoutTipPercent, int sumWithTipPercent){
        if(mdOrder==null){
            mdOrder = new MDOrder();
        }
        //делаю поиск по айди, получаю кол-во процентов, вычисляю новую сумму, записываю, отсылаю водиле
        Client client = mission.getClientInfo();
        ClientCard clientCard;

        if(mission.getPaymentType().equals(PaymentType.CORPORATE_CARD)){
            if(client.getMainClient() == null){
                throw new CustomException(7, "Вы не являетесь корпоративным клиентом");
            }
            // оплата корпоративной карты
            clientCard = clientService.getActiveClientCard(mission.getClientInfo().getMainClient());
            mdOrder.setCorporateCard(true);
        }else{
            // оплата собственной картой
            clientCard = clientService.getActiveClientCard(mission.getClientInfo());
        }

            if(clientCard==null){
                throw new CustomException(6, "Карта клиента отсутствует");
            }
                mdOrder.setClient(mission.getClientInfo());
                mdOrder.setMission(mission);
                mdOrder.setSum(sumWithoutTipPercent);
                mdOrder.setBindingId(clientCard.getBindingId());
                mdOrder.setTimeOfInsert(DateTimeUtils.nowNovosib_GMT6());
                mdOrder.setTipPercent(tipPercent);
                mdOrder.setSumWithTip(sumWithTipPercent);

                mdOrderRepository.save(mdOrder);
        return mdOrder;
    }






    private MDOrder saveMdOrder(Mission mission, int sum, MDOrder mdOrderExist, boolean corporate){
        // corporate = true - значит заказ CORPORATE_CARD и клиент является корпоративным
           if(mdOrderExist==null){
               mdOrderExist = new MDOrder();
           }

                ClientCard clientCard;
                if(corporate){
                    clientCard = clientService.getActiveClientCard(mission.getClientInfo().getMainClient());
                }else{
                    clientCard = clientService.getActiveClientCard(mission.getClientInfo());
                }

                if(clientCard!=null){
                      mdOrderExist.setClient(mission.getClientInfo());
                      mdOrderExist.setMission(mission);
                      mdOrderExist.setSum(sum);
                      mdOrderExist.setBindingId(clientCard.getBindingId());
                      mdOrderExist.setTimeOfInsert(DateTimeUtils.nowNovosib_GMT6());
                      mdOrderExist.setCorporateCard(corporate);
                      mdOrderRepository.save(mdOrderExist);
                }

             return mdOrderExist;
    }



/*
// DEPOSITED(OrderStatus=2) = {"expiration":"201512","cardholderName":"asd asd","depositAmount":80,"currency":"810","approvalCode":"123456","authCode":2,"clientId":"1","bindingId":"dd9c7912-b54b-49fe-aa8e-94012728e202","ErrorCode":"0","ErrorMessage":"Успешно","OrderStatus":2,"OrderNumber":"17","Pan":"411111**1111","Amount":80,"Ip":"109.202.27.139"}
// DECLINED(OrderStatus=6) =  {"depositAmount":0,"currency":"810","authCode":2,"clientId":"4","ErrorCode":"2","ErrorMessage":"Платеж отклонен","OrderStatus":6,"OrderNumber":"card_4141103538839","Amount":100}
// CREATED(OrderStatus=0) = {"depositAmount":0,"currency":"810","authCode":2,"clientId":"1","ErrorCode":"0","ErrorMessage":"Успешно","OrderStatus":0,"OrderNumber":"1112","Amount":55}
*/

    @Transactional
    public String getOrderStatus_old(String orderId){
        String statusOrder = "-1";
        try{

            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/getOrderStatus.do"); // Extended

            List<NameValuePair> urlParameters = new ArrayList<>();

            urlParameters.add(new BasicNameValuePair("userName", "taxisto_auto-api"));
            urlParameters.add(new BasicNameValuePair("password", "test"));
            urlParameters.add(new BasicNameValuePair("orderId", orderId));
            //urlParameters.add(new BasicNameValuePair("orderNumber", "888888"));

            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = httpClient.execute(postRequest);

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObj = new JSONObject(sb.toString());
            httpClient.getConnectionManager().shutdown();

            statusOrder = jsonObj.get("OrderStatus").toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch(JSONException j){
            j.printStackTrace();
        }
            return statusOrder;
    }





    @Transactional
    public OrderStatusResponse getOrderStatusFull(String mdOrder){
        OrderStatusResponse resp = new OrderStatusResponse();
        if(mdOrder==null){
            resp.setErrorCode("-2");
            resp.setErrorMessage("mdOrder = null");
             return resp;
        }

        try{

            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/getOrderStatus.do"); // Extended
            //HttpPost postRequest = new HttpPost("https://test.paymentgate.ru/testpayment/rest/getOrderStatus.do");

            List<NameValuePair> urlParameters = new ArrayList<>();

            urlParameters.add(new BasicNameValuePair("userName", "taxisto_auto-api"));
            urlParameters.add(new BasicNameValuePair("password", "test"));
            //urlParameters.add(new BasicNameValuePair("password", "taxisto"));
            //urlParameters.add(new BasicNameValuePair("userName", "taxisto-api"));
            urlParameters.add(new BasicNameValuePair("orderId", mdOrder));
            //urlParameters.add(new BasicNameValuePair("orderNumber", "888888"));

            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = httpClient.execute(postRequest);

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            LOGGER.info("AlphaB: getOrderStatusFull, sb.toString()="+sb.toString()+" mdOrder="+mdOrder);

            JSONObject jsonObj = new JSONObject(sb.toString());
            httpClient.getConnectionManager().shutdown();

            LOGGER.info("JSON Alpha status order: "+jsonObj);

            if(jsonObj.has("OrderStatus")){
                resp.setOrderStatus(jsonObj.get("OrderStatus").toString());
            }
            if(jsonObj.has("cardholderName")){
                resp.setCardholderName(jsonObj.get("cardholderName").toString());
            }
            if(jsonObj.has("depositAmount")){
                resp.setDepositAmount(jsonObj.get("depositAmount").toString());
            }
            if(jsonObj.has("bindingId")){
                resp.setBindingId(jsonObj.get("bindingId").toString());
            }
            if(jsonObj.has("Pan")){
                resp.setPan(jsonObj.get("Pan").toString());
            }
            if(jsonObj.has("expiration")){
                resp.setExpiration(jsonObj.get("expiration").toString());
            }
            if(jsonObj.has("ErrorCode")){
                resp.setErrorCode(jsonObj.get("ErrorCode").toString());
            }
            if(jsonObj.has("ErrorMessage")){
                resp.setErrorMessage(jsonObj.get("ErrorMessage").toString());
            }

            return resp;

        } catch (IOException e) {
            e.printStackTrace();
              resp.setOrderStatus("-1");
              resp.setErrorCode("-1");
              resp.setErrorMessage("IOException");
               return resp;
        } catch(JSONException j){
            j.printStackTrace();
            resp.setOrderStatus("-1");
            resp.setErrorCode("-1");
             resp.setErrorMessage("JsonException");
               return resp;
        }
   }



    /*
    params[@"userName"] = kAlfaLogin;
    params[@"password"] = kAlfaPass;
    params[@"currency"] = @(kRubleCode);
    params[@"language"] = @"ru";
    params[@"pageView"] = @"MOBILE";
    params[@"description"] = @"Create card";
    params[@"clientId"] = @(clientId);
    params[@"orderNumber"] = orderId;
    params[@"amount"] = @(amount);
    params[@"returnUrl"] = returnUrl;
    params[@"failUrl"] = failUrl;
    */





    @Transactional
    // регистрация заказа в Альфа-Банке
    public PayTestForRegisterCardResponse testRegisterOrderInAlphaBank(long clientId, String orderNumber){
        PayTestForRegisterCardResponse resp = new PayTestForRegisterCardResponse();
        LOGGER.debug("Registration order in AlphaBank_Version_2 with sum ="+1+" orderNumber="+orderNumber+" clientId="+clientId);
        String clientIdStr = Long.toString(clientId);
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/registerPreAuth.do"); // register registerPreAuth

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("userName", "taxisto_auto-api"));
            urlParameters.add(new BasicNameValuePair("password", "test"));
            //urlParameters.add(new BasicNameValuePair("password", "taxisto"));
            //urlParameters.add(new BasicNameValuePair("userName", "taxisto-api"));
            urlParameters.add(new BasicNameValuePair("orderNumber", orderNumber));
            urlParameters.add(new BasicNameValuePair("amount", "100"));
            urlParameters.add(new BasicNameValuePair("clientId", clientIdStr));
            urlParameters.add(new BasicNameValuePair("returnUrl", "finish.html")); //?login=taxisto-api?password=taxisto&userName=taxisto-api &jsonParams={"orderNumber":"+orderNumberStr+"}
            urlParameters.add(new BasicNameValuePair("failUrl", "mobile_errors_ru.html"));
            urlParameters.add(new BasicNameValuePair("pageView", "MOBILE"));

            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            LOGGER.info("postRequest url with param : " + postRequest.getURI() + " param : " + urlParameters);
            HttpResponse response = httpClient.execute(postRequest);
            LOGGER.info("Response status code : " + response.getStatusLine().getStatusCode());

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            LOGGER.debug("AlphaBank answer: "+sb.toString());

            // output = {"orderId":"258570a3-de11-4830-b826-1da23569bee4","formUrl":"https://test.paymentgate.ru/testpayment/merchants/taxisto/mobile_payment_ru.html?mdOrder=258570a3-de11-4830-b826-1da23569bee4&pageView=MOBILE"}
            JSONObject jObject  = new JSONObject(sb.toString()); // json

            if(jObject.has("orderId")){
                String orderIdStr = jObject.getString("orderId"); // get the name from data.
                String formUrl = jObject.getString("formUrl"); // get the name from data.

                resp.setMdOrder(orderIdStr);
                resp.setFormUrl(formUrl);
                resp.getErrorCodeHelper().setErrorMessage("");
                resp.getErrorCodeHelper().setErrorCode(0);
                LOGGER.info("Сгенерированный orderId = "+orderIdStr);
            }else{
                resp.setErrorCode(jObject.get("errorCode").toString());
                resp.setErrorMessage(jObject.get("errorMessage").toString());
                resp.getErrorCodeHelper().setErrorMessage(jObject.get("errorMessage").toString());
                resp.getErrorCodeHelper().setErrorCode(-3);
            }
                return resp;
        } catch (IOException e) {
            LOGGER.debug("AlphaBank registration TEST order error: "+e.toString());
            resp.setErrorCode("-1");
            resp.setErrorMessage(""+e.getMessage());
            resp.getErrorCodeHelper().setErrorCode(-2);
            resp.getErrorCodeHelper().setErrorMessage(e.getMessage());
            return resp;
        } catch (org.json.JSONException r) {
            LOGGER.debug("AlphaBank registration TEST order error: "+r.toString());
            resp.setErrorCode("-1");
            resp.setErrorMessage(""+r.getMessage());
            resp.getErrorCodeHelper().setErrorCode(-1);
            resp.getErrorCodeHelper().setErrorMessage(r.getMessage());
            return resp;
        }
    }







    @Transactional
    // регистрация заказа в Альфа-Банке
    private PaymentHelper registerOrderInAlphaBank(long orderNumber, long clientId, int amount, MDOrder mdOrder){
        LOGGER.debug("Registration order in AlphaBank with sum =" + amount + " orderNumber=" + orderNumber + " clientId=" + clientId);
        String orderNumberStr = Long.toString(orderNumber);
        String clientIdStr = Long.toString(clientId);
        String amountStr = Integer.toString(amount);

        PaymentHelper paymentHelper =new PaymentHelper();
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/register.do");
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("userName", "taxisto_auto-api"));
            urlParameters.add(new BasicNameValuePair("password", "test"));
            urlParameters.add(new BasicNameValuePair("orderNumber", orderNumberStr));
            urlParameters.add(new BasicNameValuePair("amount", amountStr));
            urlParameters.add(new BasicNameValuePair("clientId", clientIdStr));
            urlParameters.add(new BasicNameValuePair("returnUrl", "finish.html")); //?login=taxisto-api?password=taxisto&userName=taxisto-api &jsonParams={"orderNumber":"+orderNumberStr+"}
            urlParameters.add(new BasicNameValuePair("pageView", "MOBILE"));

            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = httpClient.execute(postRequest);

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            LOGGER.debug("AlphaBank answer: "+sb.toString());

            // output = {"orderId":"258570a3-de11-4830-b826-1da23569bee4","formUrl":"https://test.paymentgate.ru/testpayment/merchants/taxisto/mobile_payment_ru.html?mdOrder=258570a3-de11-4830-b826-1da23569bee4&pageView=MOBILE"}
            JSONObject jObject  = new JSONObject(sb.toString()); // json

            if(jObject.has("orderId")){
                String orderIdStr = jObject.getString("orderId"); // get the name from data.
                if(orderIdStr!=null){
                    paymentHelper.setOrderId(orderIdStr);
                    paymentHelper.setAmount(amountStr);
                    LOGGER.info("Сгенерированный orderId = "+orderIdStr+" amount = "+amount);

                    Client client = clientRepository.findOne(clientId);
                    ClientCard clientCard = clientCardRepository.findByClientAndStatusDeleteAndActive(client, false, true);

                    mdOrder.setClientCard(clientCard);
                    mdOrder.setSum(amount);
                    mdOrder.setMdOrderNumber(orderIdStr);
                    mdOrderRepository.save(mdOrder);
                }else{
                    paymentHelper.setOrderId("-1");
                }
            }else{
                    paymentHelper.setOrderId("-1");
            }

            return paymentHelper;

        } catch (IOException e) {
            e.printStackTrace();
                LOGGER.debug("AlphaBank registration order error: " + e.toString());
        } catch (org.json.JSONException r) {
             LOGGER.debug("JsonException in  registerOrderInAlphaBank error: " + r.toString());
              r.printStackTrace();
        }
        return paymentHelper;
    }





    public boolean notified(String event, JSONObject jsonRes) {
        //  ---------- send to socket [mission fired]-----------------
        boolean result = false;
            if(nodeJsService.isConnected()){
                nodeJsService.sendMessageSocket(event,jsonRes);
                result =true;
            }
              return result;
    }





    @Transactional
    public PayOrderFinishResponse payOrderFinishWithTip(long missionId, String security_token){
        //Driver driver = driverRepository.findByToken(security_token);
        //if(driver == null){
        //     throw new CustomException(5, "Водитель не найден");
        //}
        Mission mission = missionRepository.findOne(missionId);
        if(mission ==null ) {
            throw new CustomException(1, String.format("Миссия %s не найдена", missionId));
        }
        MDOrder mdOrder = mdOrderRepository.findByMission(mission);
        if (mdOrder == null) {
            throw new CustomException(2, "MdOrder не найден");
        }
        if(mdOrder.getSum()==0){
            throw new CustomException(4, "Сумма в MdOrder не может быть равна 0");
        }
        return paymentOrderBindingFinishWithTipPercent(mdOrder, mission);
    }









    @Transactional
    public boolean payOrder(final long missionId) throws IOException, JSONException {
        final Mission mission = missionRepository.findOne(missionId);
        if(mission!=null ) {
            MDOrder mdOrder = mdOrderRepository.findByMission(mission);
            if (mdOrder != null) {
                   if(mdOrder.getSum()!=0){
                       result = paymentOrderBinding(mdOrder, missionId);
                   }
            } else {
                LOGGER.debug("ClientCard active not found in payOrder(missionId), missionId: " + missionId);
            }
        }
        return result;
    }



    public static class PaymentHelper{
        private String orderId;
        private String amount;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }




    /* погасить долг клиента */
    private void clientDebtOff(Mission mission){
        int sumDebt = commonService.clientSumDebt(mission.getClientInfo());
        if(sumDebt<0){
            /* обнулить долг клиента */
            Account clientAccount = mission.getClientInfo().getAccount();
            clientAccount.setBonuses(MoneyUtils.getBonuses(0));
            accountRepository.save(clientAccount);

            /* сделать отметку в кошельке клиента */
            administrationService.operationWithClientCashFlow(mission.getClientInfo().getId(), mission.getId(), Math.abs(sumDebt), 5);
        }
    }







    @Transactional
    private PayOrderFinishResponse paymentOrderBindingFinishWithTipPercent(final MDOrder mdOrder, final Mission mission) {
            PayOrderFinishResponse payOrderFinishResponse = new PayOrderFinishResponse();

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/paymentOrderBinding.do");
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("userName", "taxisto_auto-api"));
                urlParameters.add(new BasicNameValuePair("password", "test"));
                urlParameters.add(new BasicNameValuePair("mdOrder", mdOrder.getMdOrderNumber()));
                urlParameters.add(new BasicNameValuePair("bindingId", mdOrder.getBindingId()));

                postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));

                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }


                String alphaBankAnswer = sb.toString();

                LOGGER.info("paymentOrderBindingFinishWithTipPercent answer bank: = " + alphaBankAnswer);

                JSONObject jObject = new JSONObject(alphaBankAnswer); // json answer
                LOGGER.info("3333333333333333");
                int res = Integer.parseInt(jObject.get("errorCode").toString());

                LOGGER.info("44444444444444 res: " + res);

                Mission.PaymentStateCard FAILED = Mission.PaymentStateCard.FAILED_PAYMENT;
                Mission.PaymentStateCard GOOD_PAYMENT = Mission.PaymentStateCard.GOOD_PAYMENT;

                LOGGER.info("RES = " + res);

                boolean result = false;
                String answer = "";

                if (res == 0) { // errorCode = 0, значит все хорошо, смотрим на ордерСтатус
                    OrderStatusResponse statusOrderResponse = getOrderStatusFull(mdOrder.getMdOrderNumber());

                    if (!statusOrderResponse.getOrderStatus().equals(ERROR)) {
                        if (statusOrderResponse.getOrderStatus().equals(DEPOSITED)) {
                            // DEPOSITED
                            TipPercent tipPercent = mdOrder.getTipPercent();
                            String tip = "";
                            if (tipPercent != null && tipPercent.getPercent() != 0) {
                                    /* 11 - чаевые*/
                                DriverCashFlow driverCashFlow = cashRepository.findByMissionAndOperationAndDriver(mission, 11, mission.getDriverInfo());
                                if (driverCashFlow == null) {
                                    administrationService.updateDriverBalanceSystem(mission.getDriverInfo().getId(), mission.getId(), (mdOrder.getSumWithTip() - mdOrder.getSum()) / 100, 11);// mdOrder.getSumWithTip()
                                    tip = " Клиент оставил Вам сумму чаевых: " + (mdOrder.getSumWithTip() - mdOrder.getSum()) / 100;
                                }
                                    /* end */
                            }

                            // закрыть долг клиента
                            clientDebtOff(mission);

                            result = true;
                            answer = "Оплата картой произведена успешно." + tip;
                            mission.setPaymentStateCard(GOOD_PAYMENT);
                            missionRepository.save(mission);
                        } else if (statusOrderResponse.getOrderStatus().equals("6")) {
                            // DECLINED
                            answer = "Авторизация отклонена";
                            mission.setPaymentStateCard(FAILED);
                            missionRepository.save(mission);
                            payOrderFinishResponse.setErrorCode(1);
                        } else if (statusOrderResponse.getOrderStatus().equals("0")) {
                            // CREATED
                            answer = "Заказ зарегистрирован, но не оплачен";
                            mission.setPaymentStateCard(FAILED);
                            missionRepository.save(mission);
                            payOrderFinishResponse.setErrorCode(2);
                        } else if (statusOrderResponse.getOrderStatus().equals("1")) {
                            //Предавторизованная сумма захолдирована (для двухстадийных платежей)
                            answer = "Предавторизованная сумма захолдирована (для двухстадийных платежей)";
                            mission.setPaymentStateCard(FAILED);
                            missionRepository.save(mission);
                            payOrderFinishResponse.setErrorCode(4);
                        } else if (statusOrderResponse.getOrderStatus().equals("3")) {
                            answer = "Авторизация отменена";
                            mission.setPaymentStateCard(FAILED);
                            missionRepository.save(mission);
                            payOrderFinishResponse.setErrorCode(5);
                        } else if (statusOrderResponse.getOrderStatus().equals("4")) {
                            answer = "По транзакции была проведена операция возврата";
                            mission.setPaymentStateCard(FAILED);
                            missionRepository.save(mission);
                            payOrderFinishResponse.setErrorCode(6);
                        } else if (statusOrderResponse.getOrderStatus().equals("5")) {
                            answer = "Инициирована авторизация через ACS банка-эмитента";
                            mission.setPaymentStateCard(FAILED);
                            missionRepository.save(mission);
                            payOrderFinishResponse.setErrorCode(7);
                        }
                    } else {
                        answer = "При оплате картой произошла системная ошибка";
                        mission.setPaymentStateCard(FAILED);
                        missionRepository.save(mission);
                        payOrderFinishResponse.setErrorCode(8);
                    }
                } else {
                    answer = "При оплате картой произошла ошибка";
                    mission.setPaymentStateCard(FAILED);
                    missionRepository.save(mission);
                    payOrderFinishResponse.setErrorCode(9);
                }

                payOrderFinishResponse.setErrorMessage(answer);
                LOGGER.info("mission.getId(): " + mission.getId() + " result: " + result + " answer: " + answer);
                nodeJsNotificationsService.sendMissionPaymentResult(mission.getId(), result, answer);

            } catch(Exception ex){

            }
        return payOrderFinishResponse;
    }







    @Transactional
    private PayOrderFinishResponse paymentOrderBindingFinishWithTipPercent_OFF_(final MDOrder mdOrder, final Mission mission) {
        final PayOrderFinishResponse payOrderFinishResponse = new PayOrderFinishResponse();

        final SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/paymentOrderBinding.do");
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("userName", "taxisto_auto-api"));
                urlParameters.add(new BasicNameValuePair("password", "test"));
                urlParameters.add(new BasicNameValuePair("mdOrder", mdOrder.getMdOrderNumber()));
                urlParameters.add(new BasicNameValuePair("bindingId", mdOrder.getBindingId()));

                postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));

                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                LOGGER.info("paymentOrderBindingFinishWithTipPercent answer bank: = "+sb);
                  return sb.toString();
            }

            protected void done() {
                String alphaBankAnswer;
                try {

                    LOGGER.info("11111111");

                    alphaBankAnswer = get();

                    LOGGER.info("22222222222 alphaBankAnswer: " + alphaBankAnswer);

                    JSONObject jObject = new JSONObject(alphaBankAnswer); // json answer
                    int res = Integer.parseInt(jObject.get("errorCode").toString());


                    LOGGER.info("33333333333 res: " + res);

                    //JSONObject jsonAnswer = new JSONObject();
                    //jsonAnswer.put("missionId", mission.getId());

                    Mission.PaymentStateCard FAILED = Mission.PaymentStateCard.FAILED_PAYMENT;
                    Mission.PaymentStateCard GOOD_PAYMENT = Mission.PaymentStateCard.GOOD_PAYMENT;

                    LOGGER.info("RES = " + res);

                    boolean result = false;
                    String answer = "";

                    if (res == 0) { // errorCode = 0, значит все хорошо, смотрим на ордерСтатус
                        OrderStatusResponse statusOrderResponse = getOrderStatusFull(mdOrder.getMdOrderNumber());

                        if(!statusOrderResponse.getOrderStatus().equals(ERROR)){
                            if(statusOrderResponse.getOrderStatus().equals(DEPOSITED)){
                                // DEPOSITED
                                TipPercent tipPercent = mdOrder.getTipPercent();
                                String tip = "";
                                if(tipPercent!=null && tipPercent.getPercent()!=0){
                                    /* 11 - чаевые*/
                                     DriverCashFlow driverCashFlow = cashRepository.findByMissionAndOperationAndDriver(mission, 11, mission.getDriverInfo());
                                     if(driverCashFlow == null){
                                         administrationService.updateDriverBalanceSystem(mission.getDriverInfo().getId(), mission.getId(), (mdOrder.getSumWithTip() - mdOrder.getSum())/100, 11);// mdOrder.getSumWithTip()
                                         tip =" Клиент оставил Вам сумму чаевых: "+(mdOrder.getSumWithTip() - mdOrder.getSum())/100;
                                     }
                                    /* end */
                                }

                                // закрыть долг клиента
                                clientDebtOff(mission);

                                result = true;
                                answer = "Оплата картой произведена успешно."+tip;
                                //jsonAnswer.put("answer", "Оплата картой произведена успешно."+tip);
                                //jsonAnswer.put("result", Boolean.TRUE);
                                mission.setPaymentStateCard(GOOD_PAYMENT);
                                missionRepository.save(mission);
                            }else if(statusOrderResponse.getOrderStatus().equals("6")){
                                // DECLINED
                                answer = "Авторизация отклонена";
                                //jsonAnswer.put("answer", "Авторизация отклонена");
                                //jsonAnswer.put("result", Boolean.FALSE);
                                mission.setPaymentStateCard(FAILED);
                                missionRepository.save(mission);
                                //payOrderFinishResponse.setErrorMessage("Авторизация отклонена");
                                payOrderFinishResponse.setErrorCode(1);
                            }else if(statusOrderResponse.getOrderStatus().equals("0")){
                                // CREATED
                                answer = "Заказ зарегистрирован, но не оплачен";
                                //jsonAnswer.put("answer", "Заказ зарегистрирован, но не оплачен");
                                //jsonAnswer.put("result", Boolean.FALSE);
                                mission.setPaymentStateCard(FAILED);
                                missionRepository.save(mission);
                                //payOrderFinishResponse.setErrorMessage("Заказ зарегистрирован, но не оплачен");
                                payOrderFinishResponse.setErrorCode(2);
                            }else if(statusOrderResponse.getOrderStatus().equals("1")){
                                //Предавторизованная сумма захолдирована (для двухстадийных платежей)
                                answer = "Предавторизованная сумма захолдирована (для двухстадийных платежей)";
                                //jsonAnswer.put("answer", "Предавторизованная сумма захолдирована (для двухстадийных платежей)");
                                //jsonAnswer.put("result", Boolean.FALSE);
                                mission.setPaymentStateCard(FAILED);
                                missionRepository.save(mission);
                                //payOrderFinishResponse.setErrorMessage("Предавторизованная сумма захолдирована (для двухстадийных платежей");
                                payOrderFinishResponse.setErrorCode(4);
                            }else if(statusOrderResponse.getOrderStatus().equals("3")){
                                answer = "Авторизация отменена";
                                //jsonAnswer.put("answer", "Авторизация отменена");
                                //jsonAnswer.put("result", Boolean.FALSE);
                                mission.setPaymentStateCard(FAILED);
                                missionRepository.save(mission);
                                //payOrderFinishResponse.setErrorMessage("Авторизация отменена");
                                payOrderFinishResponse.setErrorCode(5);
                            }else if(statusOrderResponse.getOrderStatus().equals("4")){
                                answer = "По транзакции была проведена операция возврата";
                                //jsonAnswer.put("answer", "По транзакции была проведена операция возврата");
                                //jsonAnswer.put("result", Boolean.FALSE);
                                mission.setPaymentStateCard(FAILED);
                                missionRepository.save(mission);
                                //payOrderFinishResponse.setErrorMessage("По транзакции была проведена операция возврата");
                                payOrderFinishResponse.setErrorCode(6);
                            }else if(statusOrderResponse.getOrderStatus().equals("5")){
                                answer = "Инициирована авторизация через ACS банка-эмитента";
                                //jsonAnswer.put("answer", "Инициирована авторизация через ACS банка-эмитента");
                                //jsonAnswer.put("result", Boolean.FALSE);
                                mission.setPaymentStateCard(FAILED);
                                missionRepository.save(mission);
                                //payOrderFinishResponse.setErrorMessage("Инициирована авторизация через ACS банка-эмитента");
                                payOrderFinishResponse.setErrorCode(7);
                            }
                        }else{
                                answer = "При оплате картой произошла системная ошибка";
                                //jsonAnswer.put("answer", "При оплате картой произошла системная ошибка");
                                //jsonAnswer.put("result", Boolean.FALSE);
                                mission.setPaymentStateCard(FAILED);
                                missionRepository.save(mission);
                                //payOrderFinishResponse.setErrorMessage("При оплате картой произошла системная ошибка");
                                payOrderFinishResponse.setErrorCode(8);
                        }
                    } else {
                                answer = "При оплате картой произошла ошибка";
                                //jsonAnswer.put("answer", "При оплате картой произошла ошибка");
                                //jsonAnswer.put("result", Boolean.FALSE);
                                mission.setPaymentStateCard(FAILED);
                                missionRepository.save(mission);
                                //payOrderFinishResponse.setErrorMessage("При оплате картой произошла ошибка");
                                payOrderFinishResponse.setErrorCode(9);
                    }

                    payOrderFinishResponse.setErrorMessage(answer);
                    LOGGER.info("mission.getId(): " + mission.getId() + " result: " + result + " answer: " +answer);
                    nodeJsNotificationsService.sendMissionPaymentResult(mission.getId(), result, answer);
                    //nodeJsService.notified("send_mission_payment_result", jsonAnswer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (org.json.JSONException r) {
                    r.printStackTrace();
                }
            }
        };
        worker.execute();
           return payOrderFinishResponse;
    }






    @Transactional
    private boolean paymentOrderBinding(final MDOrder mdOrder, final long missionId){
       final SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/paymentOrderBinding.do");
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("userName", "taxisto_auto-api"));
                urlParameters.add(new BasicNameValuePair("password", "test"));
                urlParameters.add(new BasicNameValuePair("mdOrder",mdOrder.getMdOrderNumber()));
                urlParameters.add(new BasicNameValuePair("bindingId",mdOrder.getBindingId()));

                postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));

                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

                  StringBuilder sb = new StringBuilder();
                     String line = "";
                       while ((line = br.readLine()) != null) {
                           sb.append(line);
                       }
                               LOGGER.info("Answer paymentOrderBinding.do = "+sb);
                                  return sb.toString();
            }

            protected void done() {
                String alphaBankAnswer;
                try {
                    // Retrieve the return value of doInBackground.
                    alphaBankAnswer = get();

                            Mission mission = missionRepository.findOne(missionId);

                            JSONObject jObject = new JSONObject(alphaBankAnswer.toString()); // json answer
                            int res = Integer.parseInt(jObject.get("errorCode").toString());

                            JSONObject jsonAnswer = new JSONObject();
                            jsonAnswer.put("missionId", missionId);

                            if (res == 0) { // errorCode = 0, значит все хорошо, смотрим на ордерСтатус
                                OrderStatusResponse statusOrderResponse = getOrderStatusFull(mdOrder.getMdOrderNumber());
/*
0 Заказ зарегистрирован, но не оплачен
1 Предавторизованная сумма захолдирована (для двухстадийных платежей)
2 Проведена полная авторизация суммы заказа
3 Авторизация отменена
4 По транзакции была проведена операция возврата
5 Инициирована авторизация через ACS банка-эмитента
6 Авторизация отклонена
 */
                                LOGGER.debug("%%%%%%%%%%%%%%%%%%%%%%% statusOrderResponse.getOrderStatus() "+statusOrderResponse.getOrderStatus());
                                    if(!statusOrderResponse.getOrderStatus().equals("-1")){
                                         if(statusOrderResponse.getOrderStatus().equals("2")){
                                             // DEPOSITED
                                             jsonAnswer.put("answer", "Оплата картой произведена успешно");
                                             jsonAnswer.put("result", Boolean.TRUE);
                                             if(mission!=null){
                                                 mission.setPaymentStateCard(Mission.PaymentStateCard.GOOD_PAYMENT);
                                                 missionRepository.save(mission);

                                                 /* 4 - оплата безналом [начисляет Дима]*/
                                                 //administrationService.updateDriverBalanceSystem(mission.getDriverInfo().getId(), missionId, mdOrder.getSum(), 4);
                                                 /* end */

                                                 /*
                                                   сюда нужно поставить проверку - есть ли в табличке чаевых запись с текущей миссией, если да, обновляем баланс водителя на данную сумму чаевых,
                                                   делаем отметку в DriverCashFlow с операцией 11, оповещаем водителя о том, что клиент оставил Вам x рублей чаевых
                                                   при этом
                                                 */
                                             }
                                             result = true;
                                             LOGGER.debug("AlphaB: Оплата картой произведена успешно");
                                         }else if(statusOrderResponse.getOrderStatus().equals("6")){
                                             // DECLINED
                                             jsonAnswer.put("answer", "Авторизация отклонена");
                                             jsonAnswer.put("result", Boolean.FALSE);
                                             if(mission!=null){
                                                 mission.setPaymentStateCard(Mission.PaymentStateCard.FAILED_PAYMENT);
                                                 missionRepository.save(mission);
                                             }
                                             result = false;
                                             LOGGER.debug("AlphaB: Авторизация отклонена");
                                         }else if(statusOrderResponse.getOrderStatus().equals("0")){
                                             // CREATED
                                             jsonAnswer.put("answer", "Заказ зарегистрирован, но не оплачен");
                                             jsonAnswer.put("result", Boolean.FALSE);
                                             if(mission!=null){
                                                 mission.setPaymentStateCard(Mission.PaymentStateCard.FAILED_PAYMENT);
                                                 missionRepository.save(mission);
                                             }
                                             result = false;
                                             LOGGER.debug("AlphaB: Заказ зарегистрирован, но не оплачен");
                                         }else if(statusOrderResponse.getOrderStatus().equals("1")){
                                             //Предавторизованная сумма захолдирована (для двухстадийных платежей)
                                             jsonAnswer.put("answer", "Предавторизованная сумма захолдирована (для двухстадийных платежей)");
                                             jsonAnswer.put("result", Boolean.FALSE);
                                             if(mission!=null){
                                                 mission.setPaymentStateCard(Mission.PaymentStateCard.FAILED_PAYMENT);
                                                 missionRepository.save(mission);
                                             }
                                             result = false;
                                             LOGGER.debug("AlphaB: Предавторизованная сумма захолдирована (для двухстадийных платежей)");
                                         }else if(statusOrderResponse.getOrderStatus().equals("3")){
                                             jsonAnswer.put("answer", "Авторизация отменена");
                                             jsonAnswer.put("result", Boolean.FALSE);
                                             if(mission!=null){
                                                 mission.setPaymentStateCard(Mission.PaymentStateCard.FAILED_PAYMENT);
                                                 missionRepository.save(mission);
                                             }
                                             result = false;
                                             LOGGER.debug("AlphaB: Авторизация отменена");
                                         }else if(statusOrderResponse.getOrderStatus().equals("4")){
                                             jsonAnswer.put("answer", "По транзакции была проведена операция возврата");
                                             jsonAnswer.put("result", Boolean.FALSE);
                                             if(mission!=null){
                                                 mission.setPaymentStateCard(Mission.PaymentStateCard.FAILED_PAYMENT);
                                                 missionRepository.save(mission);
                                             }
                                             result = false;
                                             LOGGER.debug("AlphaB: По транзакции была проведена операция возврата");
                                         }else if(statusOrderResponse.getOrderStatus().equals("5")){
                                             jsonAnswer.put("answer", "Инициирована авторизация через ACS банка-эмитента");
                                             jsonAnswer.put("result", Boolean.FALSE);
                                             if(mission!=null){
                                                 mission.setPaymentStateCard(Mission.PaymentStateCard.FAILED_PAYMENT);
                                                 missionRepository.save(mission);
                                             }
                                             result = false;
                                             LOGGER.debug("AlphaB: Инициирована авторизация через ACS банка-эмитента");
                                         }
                                    }else{
                                        jsonAnswer.put("answer", "При оплате картой произошла системная ошибка");
                                        jsonAnswer.put("result", Boolean.FALSE);
                                        if(mission!=null){
                                            mission.setPaymentStateCard(Mission.PaymentStateCard.FAILED_PAYMENT);
                                            missionRepository.save(mission);
                                        }
                                        result = false;
                                        LOGGER.debug("AlphaB: При оплате картой произошла системная ошибка");
                                    }
                            } else {
                                jsonAnswer.put("answer", "При оплате картой произошла ошибка");
                                jsonAnswer.put("result", Boolean.FALSE);
                                if(mission!=null){
                                    mission.setPaymentStateCard(Mission.PaymentStateCard.FAILED_PAYMENT);
                                    missionRepository.save(mission);
                                }
                                result = false;
                                LOGGER.debug("AlphaB: При оплате картой произошла ошибка");
                            }

                  // 6.  Отправить водителю результат оплаты
                  // event: send_mission_payment_result
                  // data: {“missionId”: 23, “answer”: “olololo”, “result”: true}


                    LOGGER.debug("%%%%%%%%%%%%%%%%%%%%%%% send_mission_payment_result" + jsonAnswer.toString());

                    boolean isSent = notified("send_mission_payment_result", jsonAnswer);

                } catch (InterruptedException e) {
                      e.printStackTrace();
                } catch (ExecutionException e) {
                      e.printStackTrace();
                } catch (org.json.JSONException r) {
                      r.printStackTrace();
                }
            }
        };
        worker.execute();

    return result;
    }





    public GetIdCardResponse getIdCard(long clientId, String mdOrder){
        GetIdCardResponse response = new GetIdCardResponse();

                   Client client = clientRepository.findOne(clientId);

                    if(client!=null){
                     List<ClientCard> clientCardList= clientCardRepository.findByClient(client);

                      boolean status = false;

                         for(ClientCard clientC: clientCardList){
                              if(clientC.getBindingId()==null){
                                  response.setIdCard(clientC.getId());
                                  response.setIdCardStr(clientC.getId()+"_card");
                                  response.setMdOrder(mdOrder);
                                  status = true;
                              }
                         }
                             if(!status){
                                 ClientCard clientCard = createEmptyCard(client, mdOrder);
                                 response.setIdCard(clientCard.getId());
                                 response.setIdCardStr(clientCard.getId()+"_card");
                                 response.setMdOrder(mdOrder);
                             }
                    }else{
                                 ClientCard clientCard = createEmptyCard(client, mdOrder);
                                 response.setIdCard(clientCard.getId());
                                 response.setIdCardStr(clientCard.getId()+"_card");
                                 response.setMdOrder(mdOrder);
                    }
             return response;
    }



    public ClientCard createEmptyCard(Client client, String mdOrder){
        ClientCard clientCard = new ClientCard();
        clientCard.setMdOrderNumber(mdOrder);
        clientCard.setClient(client);
        clientCardRepository.save(clientCard);
          return clientCard;
    }




    public StoreCardDataResponse storeCardData(Long cardId,  Long clientId, String bindingId, String mdOrder, Boolean active, String cardholderName, String pan, Integer expirationYear, Integer expirationMonth, String mrchOrder, String token){
        StoreCardDataResponse response = new StoreCardDataResponse();
        //ClientCard clientCard = clientCardRepository.findOne(cardId);
        ClientCard clientCard = new ClientCard();

        //if(clientCard!=null){
        Client client = null;
               if(clientId!=null){
                    client = clientRepository.findOne(clientId);
                   if(client!=null){
                       if (client.getToken() != null && client.getToken().equals(token)) {
                           List<ClientCard> clientCardList = clientCardRepository.findByClientAndPanAndStatusDelete(client, pan, false);
                           if(clientCardList.isEmpty()){
                               clientCard.setClient(client);
                               clientCard.setBindingId(bindingId);
                               clientCard.setMdOrderNumber(mdOrder);
                               clientCard.setActive(active);
                               clientCard.setCardholderName(cardholderName);
                               clientCard.setPan(pan);
                               clientCard.setExpirationYear(expirationYear);
                               clientCard.setExpirationMonth(expirationMonth);
                               clientCard.setMrchOrder(mrchOrder);
                               clientCard.setCardId(cardId);
                               clientCard.setPaymentDate(DateTimeUtils.nowNovosib().getMillis());
                               if (client != null) {
                                   clientService.setAllClientCardNotActive(clientCardRepository.findByClient(client));
                               }
                               clientCard.setActive(true);
                               clientCardRepository.save(clientCard);
                               response.setResult(true);
                               //response.getErrorCodeHelper().setErrorCode(0);
                               //response.getErrorCodeHelper().setErrorMessage("");
                           }else{
                               LOGGER.info("storeCardData: У вас уже есть карта с таким номером");
                               response.setResult(false);
                               //response.getErrorCodeHelper().setErrorCode(1);
                               //response.getErrorCodeHelper().setErrorMessage("У вас уже есть карта с таким номером");
                           }
                       }else{
                           LOGGER.info("storeCardData: Tokens are not equal");
                           response.setResult(false);
                           //response.getErrorCodeHelper().setErrorCode(3);
                           //response.getErrorCodeHelper().setErrorMessage("Несоответсвие ключа безопасности");
                       }
                   }else{
                       LOGGER.info("storeCardData: Client not found");
                       response.setResult(false);
                       //response.getErrorCodeHelper().setErrorCode(2);
                       //response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
                   }
               }else{
                   LOGGER.info("storeCardData: clientId is null");
                   response.setResult(false);
               }
          return response;
    }



    @Transactional
    public void transferMoneyToDriver(long driverId, double amount) {
        Money money = Money.of(MoneyUtils.DEFAULT_CURRENCY, amount);
        Account driverAccount = accountRepository.findOne(driverId);
        driverAccount.setMoney(Money.total(driverAccount.getMoney(), money));
        accountRepository.save(driverAccount);
    }





    @Transactional
    public Account createCompanyAccount() {
        DateTime nowDateTime = timeService.nowDateTime();

        Account account = new Account();
        account.setState(Account.State.ACTIVE);
        account.setTimeCreated(nowDateTime);
        account.setTimeActivated(nowDateTime);
        account.setMoney(MoneyUtils.getRubles(1000));
        account.setKind(Account.Kind.INTERNAL);
        account.setDescription("Main company account");
        account = accountRepository.save(account);
        return account;
    }


    @Transactional
    public Account createClientAccountWithBonuses(double amount) {
        DateTime nowDateTime = timeService.nowDateTime();
        Account account = new Account();
        account.setState(Account.State.ACTIVE);
        account.setTimeCreated(nowDateTime);
        account.setTimeActivated(nowDateTime);
        account.setBonuses(MoneyUtils.getBonuses(amount));
        account.setKind(Account.Kind.CLIENT);
        account = accountRepository.save(account);
        return account;
    }



    @Transactional
    public Account createDriverAccountWithRubles(double amount) {
        Account account = new Account();
        try {
              DateTime nowDateTime = timeService.nowDateTime();
              account.setState(Account.State.ACTIVE);
              account.setTimeCreated(nowDateTime);
              account.setTimeActivated(nowDateTime);
              account.setMoney(MoneyUtils.getRubles(amount));
              account.setKind(Account.Kind.DRIVER);
              account = accountRepository.save(account);
          }catch(Exception e){
              throw e;
          }
        return account;
    }




    public AutoClassRateInfoV2 getAutoClassRateInfoV2(Set<AutoClassPrice> prices, int autoType) {
        AutoClassRateInfoV2 autoClassRateInfoV2 = new AutoClassRateInfoV2();
        for (AutoClassPrice price : prices) {
              if(price.getAutoClass().getValue()==autoType){
                  autoClassRateInfoV2.setAutoClassStr(ModelsUtils.autoTypeStr(price.getAutoClass().getValue())); // administrationService.nameAutoClass(price.getAutoClass().getValue())
                  autoClassRateInfoV2.setAutoClass(price.getAutoClass().getValue());
                  autoClassRateInfoV2.setFreeWaitMinutes(price.getFreeWaitMinutes());
                  autoClassRateInfoV2.setIntercity(price.getIntercity());
                  autoClassRateInfoV2.setKmIncluded(price.getKmIncluded());
                  autoClassRateInfoV2.setPerHourAmount(price.getPerHourAmount());
                  autoClassRateInfoV2.setPerMinuteWaitAmount(price.getPerMinuteWaitAmount());
                  autoClassRateInfoV2.setPrice(price.getPrice().getAmount().intValue());
                  autoClassRateInfoV2.setPriceHour(price.getPriceHour());
                  autoClassRateInfoV2.setPriceKm(price.getPriceKm());
                  autoClassRateInfoV2.setAutoExample(price.getAutoExample());
                  autoClassRateInfoV2.setDescription(price.getDescription());
                  autoClassRateInfoV2.setActivePicUrl(price.getActivePicUrl());
                  autoClassRateInfoV2.setNotActivePicUrl(price.getNotActivePicUrl());
                  autoClassRateInfoV2.setActive(price.isActive());
                  return autoClassRateInfoV2;
              }
        }
        return autoClassRateInfoV2;
    }




    /* для iphone версии 1.3.5*/
    public List<AutoClassRateInfoV2> getAutoClassRatesV2(Set<AutoClassPrice> prices, boolean isCorporate) {
        ArrayList<AutoClassRateInfoV2> result = new ArrayList<>();
        for (AutoClassPrice price : prices) {
            //if(price.isActive()){
                // тариф включен, значит добавляем его в коллекцию
                AutoClassRateInfoV2 autoClassRateInfoV2 = new AutoClassRateInfoV2();
                autoClassRateInfoV2.setAutoClassStr(ModelsUtils.autoTypeStr(price.getAutoClass().getValue()));
                autoClassRateInfoV2.setAutoClass(price.getAutoClass().getValue());
                autoClassRateInfoV2.setFreeWaitMinutes(price.getFreeWaitMinutes());
                autoClassRateInfoV2.setIntercity(price.getIntercity());
                autoClassRateInfoV2.setKmIncluded(price.getKmIncluded());
                autoClassRateInfoV2.setPerHourAmount(price.getPerHourAmount());
                autoClassRateInfoV2.setPerMinuteWaitAmount(price.getPerMinuteWaitAmount());
                autoClassRateInfoV2.setPrice(price.getPrice().getAmount().intValue());
                autoClassRateInfoV2.setPriceHour(price.getPriceHour());
                if(isCorporate){
                    autoClassRateInfoV2.setPriceKm(price.getPriceKmCorporate());
                }else{
                    autoClassRateInfoV2.setPriceKm(price.getPriceKm());
                }
                autoClassRateInfoV2.setAutoExample(price.getAutoExample());
                autoClassRateInfoV2.setDescription(price.getDescription());
                autoClassRateInfoV2.setActivePicUrl(price.getActivePicUrl());
                autoClassRateInfoV2.setNotActivePicUrl(price.getNotActivePicUrl());
                autoClassRateInfoV2.setActive(price.isActive());
                result.add(autoClassRateInfoV2);
            //}
        }

          result = sortList(result);

        return result;
    }


    public List<AutoClassRateInfoV3> getAutoClassRatesV3(Set<AutoClassPrice> prices, boolean isCorporate) {
        ArrayList<AutoClassRateInfoV3> result = new ArrayList<>();
        for (AutoClassPrice price : prices) {
            //if(price.isActive()){
            // тариф включен, значит добавляем его в коллекцию
            AutoClassRateInfoV3 autoClassRateInfoV3 = new AutoClassRateInfoV3();
            autoClassRateInfoV3.setAutoClassStr(ModelsUtils.autoTypeStr(price.getAutoClass().getValue()));
            autoClassRateInfoV3.setAutoClass(price.getAutoClass().getValue());
            autoClassRateInfoV3.setFreeWaitMinutes(price.getFreeWaitMinutes());
            autoClassRateInfoV3.setIntercity(price.getIntercity());
            autoClassRateInfoV3.setKmIncluded(price.getKmIncluded());
            autoClassRateInfoV3.setPerHourAmount(price.getPerHourAmount());
            autoClassRateInfoV3.setPerMinuteWaitAmount(price.getPerMinuteWaitAmount());
            autoClassRateInfoV3.setPrice(price.getPrice().getAmount().intValue());
            autoClassRateInfoV3.setPriceHour(price.getPriceHour());
            if(isCorporate){
                autoClassRateInfoV3.setPriceKm(price.getPriceKmCorporate());
            }else{
                autoClassRateInfoV3.setPriceKm(price.getPriceKm());
            }
            autoClassRateInfoV3.setAutoExample(price.getAutoExample());
            autoClassRateInfoV3.setDescription(price.getDescription());
            autoClassRateInfoV3.setActivePicUrl(price.getActivePicUrl());
            autoClassRateInfoV3.setNotActivePicUrl(price.getNotActivePicUrl());
            autoClassRateInfoV3.setActive(price.isActive());
            result.add(autoClassRateInfoV3);
            //}
        }

        result = sortListV3(result);

        return result;
    }



    private ArrayList sortList(ArrayList<AutoClassRateInfoV2> result){
        Collections.sort(result, new Comparator<AutoClassRateInfoV2>() {
            public int compare(AutoClassRateInfoV2 o1, AutoClassRateInfoV2 o2) {
                return o1.getPrice()-o2.getPrice();
            }
        });
           return result;
    }

    private ArrayList sortListV3(ArrayList<AutoClassRateInfoV3> result){
        Collections.sort(result, new Comparator<AutoClassRateInfoV3>() {
            public int compare(AutoClassRateInfoV3 o1, AutoClassRateInfoV3 o2) {
                return o1.getPrice()-o2.getPrice();
            }
        });
        return result;
    }



    public List<AutoClassRateInfo> getAutoClassRates(Set<AutoClassPrice> prices, boolean isCorporate) {
        ArrayList<AutoClassRateInfo> result = new ArrayList<>();
        for (AutoClassPrice price : prices) {
                //if(price.isActive()){
                    // тариф включен, значит добавляем его в коллекцию
            if(isCorporate){
                result.add(new AutoClassRateInfo(price.getAutoClass().getValue(), price.getPrice().getAmount().intValue(),
                        price.getKmIncluded(), price.getPriceKmCorporate(), price.getPriceHour(), price.getFreeWaitMinutes(),
                        price.getPerMinuteWaitAmount(), price.getIntercity()));
            }else{
                result.add(new AutoClassRateInfo(price.getAutoClass().getValue(), price.getPrice().getAmount().intValue(),
                        price.getKmIncluded(), price.getPriceKm(), price.getPriceHour(), price.getFreeWaitMinutes(),
                        price.getPerMinuteWaitAmount(), price.getIntercity()));
            }
                //}
        }
        return result;
    }



    // проверить этот метод на работоспособность
    public List<ServicePriceInfo> getServicePrices(Boolean isNewVersionAppClient) {
        Iterable<ru.trendtech.domain.ServicePrice> services = servicesRepository.findAll();
        if(isNewVersionAppClient!=null){
            if(isNewVersionAppClient){
                services = servicesRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "sort")));
            }
        }

        if (isNewVersionAppClient != null) {
            Iterator<ServicePrice> i = services.iterator();
            while (i.hasNext()) {
                ServicePrice servicePrice = i.next();
                if (isNewVersionAppClient) {
                    if (servicePrice.getService().name().equals(MissionService.SMALL_ANIMAL.name())) {
                        i.remove();
                    }
                } else {
                    if (servicePrice.getService().name().equals(MissionService.CONDITIONER.name()) || servicePrice.getService().name().equals(MissionService.SMALL_CHILDREN.name())) {
                        i.remove();
                    }
                }
            }
        }

        return ModelsUtils.toModel(services, isNewVersionAppClient); // false - new version app client
    }



//    public List<ServicePriceInfo> getServicePricesV2(boolean isNewVersionAppClient) {
//        Iterable<ru.trendtech.domain.ServicePrice> services = servicesRepository.findAll();
//        return ModelsUtils.toModel(services, isNewVersionAppClient);
//    }



    @Transactional
    public List<MissionRateInfo> findMissionRates(boolean isCorporate) {
        MissionRateInfo rateInfo = null;
        MissionRate missionRate = missionRatesRepository.findOne((long) 1);
        if (missionRate != null) {
            rateInfo = new MissionRateInfo();
            rateInfo.setId(missionRate.getId());
            rateInfo.setName(missionRate.getName());
            rateInfo.setFreeWaitingTime(missionRate.getFreeWaitingTime());
            rateInfo.setPriceMinimal(missionRate.getPriceMinimal().getAmount().doubleValue());
            rateInfo.setPriceMinute(missionRate.getPriceWaitingMinute().getAmount().doubleValue());
            rateInfo.setPriceStop(missionRate.getPriceStop().getAmount().doubleValue());
            rateInfo.setServicesPrices(getServicePrices(false));
//            rateInfo.setServicesPrices(getServicePrices(missionRate.getServicesPrices()));
            rateInfo.setAutoClassRateInfos(getAutoClassRates(missionRate.getAutoClassPrices(), isCorporate));
        }
        return Arrays.asList(rateInfo);
    }


    @Transactional
    public List<MissionRateInfoV2> findMissionRatesV2(boolean isCorporate) {
        MissionRateInfoV2 rateInfo = null;
        MissionRate missionRate = missionRatesRepository.findOne((long) 1);
        if (missionRate != null) {
            rateInfo = new MissionRateInfoV2();
            rateInfo.setId(missionRate.getId());
            rateInfo.setName(missionRate.getName());
            rateInfo.setFreeWaitingTime(missionRate.getFreeWaitingTime());
            rateInfo.setPriceMinimal(missionRate.getPriceMinimal().getAmount().doubleValue());
            rateInfo.setPriceMinute(missionRate.getPriceWaitingMinute().getAmount().doubleValue());
            rateInfo.setPriceStop(missionRate.getPriceStop().getAmount().doubleValue());
            rateInfo.setServicesPrices(getServicePrices(false));
            rateInfo.setAutoClassRateInfos(getAutoClassRatesV2(missionRate.getAutoClassPrices(), isCorporate));
        }
        return Arrays.asList(rateInfo);
    }




    @Transactional
    public Account createBonusAccount() {
        DateTime nowDateTime = timeService.nowDateTime();

        Account account = new Account();
        account.setState(Account.State.ACTIVE);
        account.setTimeCreated(nowDateTime);
        account.setTimeActivated(nowDateTime);
        account.setMoney(MoneyUtils.getBonuses(0));
        account.setKind(Account.Kind.INTERNAL);
        account.setDescription("Company bonuses account");
        account = accountRepository.save(account);

        return account;
    }

    private Account findBonusesAccount() {
        return accountRepository.findOne(BONUSES_ACCOUNT_ID);
    }

    private Account findCompanyAccount() {
        return accountRepository.findOne(COMPANY_ACCOUNT_ID);
    }




    public boolean lifeTimeBonusCard(long fromId, String channel, String promoText){
        DateTime dtOrg = new DateTime(DateTime.now());
        DateTime expirationDate = dtOrg.plusDays(1);
        boolean result = false;

                PromoCodes promoCodes =  promoCodeRepository.findByPromoCodeIgnoreCase(promoText);
                     if(promoCodes!=null){
                         promoCodes.setFromId(fromId);
                         promoCodes.setExpirationDate(expirationDate.getMillis());
                         promoCodes.setChannel(channel);
                         promoCodeRepository.save(promoCodes);
                           result = true;
                     }
          return result;
        /*
int seconds = (int) (milliseconds / 1000) % 60 ;
int minutes = (int) ((milliseconds / (1000*60)) % 60);
int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
         */
    }







    @Transactional
    public ActivateBonusCardResponse activateBonusCode(long clientId, String text) throws ParseException {
        int result = 0;
        ActivateBonusCardResponse response = new ActivateBonusCardResponse();
        PromoCodes promoCodes =  promoCodeRepository.findByPromoCodeIgnoreCase(text);
        // transferBonusesToClient(clientId, result);
        //f:add
        if(promoCodes!=null){
           response = transferBonusesToClient(clientId, promoCodes, response);
        }
        return response;
    }




    public int getCountAllActivatePromoCodeByAllDay(){
        int countActivateByAllDay = 0;
        countActivateByAllDay = clientActivatedPromoCodesRepository.findCountClientActivatedPromoCodes();
          return countActivateByAllDay;
    }




    public int getCountAllClient(){
        int countClient = 0;
        countClient = clientRepository.findCountClient();
           return countClient;
    }



    public int getCountAllActivatePromoCodeByDay(){
        // кол-во активаций за день
        int countActivateByDay = 0;

        DateTime s = new DateTime();
        DateTime e = new DateTime();
        DateTime d= e.plusDays(1);

        List<ClientActivatedPromoCodes> clientActivatedPromoCodesList = clientActivatedPromoCodesRepository.findByDateOfUsedBetweenAndPromoCodeIdNot(s.withTimeAtStartOfDay().getMillis(), d.withTimeAtStartOfDay().getMillis(), -1); //

        if(clientActivatedPromoCodesList!=null){
            countActivateByDay = clientActivatedPromoCodesList.size();
        }
        return countActivateByDay;
    }





    private int getCountClientActivatePromoCode(long clientId){
        List<ClientActivatedPromoCodes> clientActivatedPromoCodesList = clientActivatedPromoCodesRepository.findByClientId(clientId);
           if(clientActivatedPromoCodesList!=null){
               return clientActivatedPromoCodesList.size();
           }else{
               return 0;
           }
    }



    private int getCountActivatePromoCodeByDay(long clientId){
        int countActivateByDay = 0;

        DateTime s = new DateTime();
        DateTime e = new DateTime();

        DateTime d= e.plusDays(1);

        LOGGER.info("start = "+s.withTimeAtStartOfDay());
        LOGGER.info("end = "+d.withTimeAtStartOfDay());

        List<ClientActivatedPromoCodes> clientActivatedPromoCodesList = clientActivatedPromoCodesRepository.findByClientIdAndDateOfUsedBetween(clientId, DateTimeUtils.toDate(s.withTimeAtStartOfDay()), DateTimeUtils.toDate(d.withTimeAtStartOfDay()));

        if(clientActivatedPromoCodesList!=null){
            countActivateByDay = clientActivatedPromoCodesList.size();
        }
        return countActivateByDay;
    }





    @Transactional
    public ActivateBonusCardResponse transferBonusesToClient(long clientId, PromoCodes promoCode, ActivateBonusCardResponse response) throws ParseException {
        int result=0;

               // активация с проверкой срока жизни
                if(promoCode.getFromId()!=null){

                    Money money = Money.of(MoneyUtils.BONUSES_CURRENCY, promoCode.getAmount());
                    Client client = clientRepository.findOne(clientId);

                    long expirationDateMillis = promoCode.getExpirationDate();
                    DateTime expirationDate = new DateTime(expirationDateMillis);
                    DateTime nowDateTime = DateTimeUtils.nowNovosib_GMT6();

                    Minutes minutes = Minutes.minutesBetween(nowDateTime, expirationDate);

                    int usedCount = promoCode.getUsedCount(); // сколько раз активировали
                    LOGGER.info("Данный промокод использовали = "+usedCount);
                    int availableUsedCount = promoCode.getAvailableUsedCount();// сколько раз вообще его можно активировать
                    LOGGER.info("сколько раз вообще его можно активировать = "+availableUsedCount);

                    int diffBetweenUsedCountAndAvailableCount = availableUsedCount-usedCount;

                    // сколько можно использовать клиенту вообще
                    ClientAvailableActivatePromoCode clientAvailableActivatePromoCodeObj = clientAvailableActivatePromoCodeRepository.findByClientId(client.getId());
                       if(clientAvailableActivatePromoCodeObj!=null){
                           LOGGER.info("доступное кол-во активаций в день для клиента = "+clientAvailableActivatePromoCodeObj.getAvailableActivateCount());
                       }
                    // сколько уже активировал вообще
                    int clientCountActivatedPromo = getCountClientActivatePromoCode(clientId); //getCountActivatePromoCodeByDay(client.getId());
                    //LOGGER.info("сколько уже использовали сегодня = "+clientCountActivatedPromo);

                    LOGGER.info("Будет просрочен через: "+minutes.getMinutes()+" мин");
                    if(minutes.getMinutes()>0){

                            if (client != null) {
                               if(!promoCode.getFromId().equals(client.getId())){
                                     if(diffBetweenUsedCountAndAvailableCount>0){
                                         String propClientAvailableActivatePromoCode = commonService.getPropertyValue("client_available_activate_promo_code");
                                         int count = 0;

                                         if(clientAvailableActivatePromoCodeObj!=null){
                                                  count = clientAvailableActivatePromoCodeObj.getAvailableActivateCount();
                                         }else{
                                              if(propClientAvailableActivatePromoCode!=null){
                                                  count = Integer.parseInt(propClientAvailableActivatePromoCode);
                                              }
                                         }

                                         if(count!=0){
                                               if(clientCountActivatedPromo<count){
                                                   ClientActivatedPromoCodes clientActivatedPromo = clientActivatedPromoCodesRepository.findByPromoCodeIdAndClientId(promoCode.getId(), client.getId());

                                                     if(clientActivatedPromo!=null){
                                                         LOGGER.info("вы уже активировали данный промокод ранее");
                                                         //response.getErrorCodeHelper().setErrorCode(1);
                                                         //response.getErrorCodeHelper().setErrorMessage("Вы уже активировали данный промокод ранее");
                                                     }else{
                                                         // можем активировать еще
                                                         administrationService.operationWithBonusesClient(clientId, null, money.getAmount().intValue(), 3, null, "", null);

                                                         promoCode.setToId(clientId);
                                                         promoCode.setUsedCount(usedCount++);
                                                         promoCodeRepository.save(promoCode);

                                                         ClientActivatedPromoCodes clientActivatedPromoCodes  = new ClientActivatedPromoCodes();
                                                         clientActivatedPromoCodes.setClientId(clientId);
                                                         clientActivatedPromoCodes.setPromoCodeId(promoCode.getId());
                                                         clientActivatedPromoCodes.setDateOfUsed(DateTimeUtils.nowNovosib_GMT6().getMillis());
                                                         clientActivatedPromoCodesRepository.save(clientActivatedPromoCodes);
                                                         response.setAmount(promoCode.getAmount());

                                                         mongoDBServices.createEvent(2, ""+clientId, 3, 0, "billingActivate", "", "");
                                                         ///mongoDBServices.createEvent(2, "" + clientId, 3, "billingActivate", "", "", clientId, 0);
                                                     }
                                               }else{
                                                   /*
                                                     Превышен лимит предельно допустимого числа активаций промокодов
                                                   */
                                                   LOGGER.info("Превышен лимит предельно допустимого числа активаций промокодов");

                                                   /*
                                                   ClientActivatedPromoCodes clientActivatedPromo = clientActivatedPromoCodesRepository.findByPromoCodeIdAndClientId(promoCode.getId(), client.getId());
                                                   if(clientActivatedPromo!=null){
                                                       LOGGER.info("вы уже активировали данный промокод ранее");
                                                   }else{
                                                        if(promoCode.getPromoCode().equals("K8GBMJ")){
                                                           promoCode.setToId(clientId);
                                                           usedCount = usedCount+1;
                                                           promoCode.setUsedCount(usedCount);
                                                           promoCodeRepository.save(promoCode);
                                                           result = (int)promoCode.getAmount();

                                                           ClientActivatedPromoCodes clientActivatedPromoCodes  = new ClientActivatedPromoCodes();
                                                           clientActivatedPromoCodes.setClientId(clientId);
                                                           clientActivatedPromoCodes.setPromoCodeId(promoCode.getId());
                                                           clientActivatedPromoCodes.setDateOfUsed(DateTimeUtils.nowNovosib_GMT6().getMillis());

                                                           clientActivatedPromoCodesRepository.save(clientActivatedPromoCodes);

                                                           LOGGER.info("успешно активирован!");
                                                           response.setAmount(result);
                                                       }
                                                   }
                                                   */
                                                   //response.getErrorCodeHelper().setErrorCode(2);
                                                   //response.getErrorCodeHelper().setErrorMessage("Вы превысили предельно допустимое число активаций в сутки");
                                               }
                                         }else{
                                               LOGGER.info("Для клиента не задано предельно допустимое кол-во активаций промокодов");
                                             //response.getErrorCodeHelper().setErrorCode(3);
                                             //response.getErrorCodeHelper().setErrorMessage("Не задано предельно допустимое количество активаций промокодов");
                                         }
                                     }else{
                                         LOGGER.info("Кол-во активаций превышает предельно допустимое число");
                                         //response.getErrorCodeHelper().setErrorCode(4);
                                         //response.getErrorCodeHelper().setErrorMessage("Количество активаций данного промокода превышает предельно допустимое число");
                                     }
                               }else{
                                   // активация для самого себя запрещена!
                                   //response.getErrorCodeHelper().setErrorCode(5);
                                   //response.getErrorCodeHelper().setErrorMessage("Невозможно активировать данный промокод");
                                   LOGGER.info("активация для самого себя запрещена!");
                               }
                            }else{
                            LOGGER.info("клиент не найден!");
                                //response.getErrorCodeHelper().setErrorCode(6);
                                //response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
                            }
                    }else{
                        //response.getErrorCodeHelper().setErrorCode(7);
                        //response.getErrorCodeHelper().setErrorMessage("Данный промокод просрочен");
                        LOGGER.info("промокод просрочен!");
                    }
            }else{
                    LOGGER.info("Не задан fromId");
                    //response.getErrorCodeHelper().setErrorCode(8);
                    //response.getErrorCodeHelper().setErrorMessage("Не задан fromId");
            }
        return response;


//        Account bonusesAccount = findBonusesAccount();
//        Transactions transaction = new Transactions();
//        transaction.setCreatedDateTime(timeService.nowDateTime());
//        transaction.setFrom(bonusesAccount);
//        transaction.setTo(clientAccount);
//        transaction.setPaymentType(PaymentType.BONUSES);
//        transaction.setState(Transactions.State.BILLED);
//        transaction.setMoney(money);
//
//        transactionRepository.save(transaction);
    }



    private int getCountSentPromoCodeByClient(Client client){
         int countSentPromoByClient = 0;

        DateTime s = new DateTime();
        DateTime e = new DateTime();

        DateTime d= e.plusDays(1);

        LOGGER.info("start = "+s.withTimeAtStartOfDay());
        LOGGER.info("end = "+d.withTimeAtStartOfDay());

        List<PromoCodes> promoCodesList = promoCodeRepository.findByFromIdAndDateOfIssueBetween(client.getId(), DateTimeUtils.toDate(s.withTimeAtStartOfDay()), DateTimeUtils.toDate(d.withTimeAtStartOfDay()));

        if(promoCodesList!=null){
            countSentPromoByClient = promoCodesList.size();
        }
          return countSentPromoByClient;
    }





    private int getCountSentPromoCodeByDay(){
        int countSendByDay = 0;

        DateTime s = new DateTime();
        DateTime e = new DateTime();

        DateTime d= e.plusDays(1);

        LOGGER.info("start = "+s.withTimeAtStartOfDay());
        LOGGER.info("end = "+d.withTimeAtStartOfDay());

        List<PromoCodes> promoCodesList = promoCodeRepository.findByDateOfIssueBetween(DateTimeUtils.toDate(s.withTimeAtStartOfDay()), DateTimeUtils.toDate(d.withTimeAtStartOfDay()));

           if(promoCodesList!=null){
               countSendByDay = promoCodesList.size();
           }
          return countSendByDay;
    }



    @Transactional
    public MissionRateInfo getDriverRate(long driverId) {
        List<MissionRateInfo> missionRates = findMissionRates(false);
        return missionRates.get(0);
    }
}
