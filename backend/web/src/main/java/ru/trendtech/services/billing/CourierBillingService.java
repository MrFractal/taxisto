package ru.trendtech.services.billing;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.courier.CourierClientCard;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderPayment;
import ru.trendtech.domain.courier.PaymentState;
import ru.trendtech.integration.PayOnlineHelper;
import ru.trendtech.integration.payonline.*;
import ru.trendtech.repositories.OrderPaymentRepository;
import ru.trendtech.repositories.courier.CourierClientCardRepository;
import ru.trendtech.services.client.ClientCourierService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.MoneyUtils;

import java.io.Reader;
import java.io.StringReader;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * Created by petr on 14.09.2015.
 */
@Service
@Transactional
public class CourierBillingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourierBillingService.class);
    @Value("${card.prefix}")
    private String CARD_PREFIX;
    @Autowired
    private CourierClientCardRepository courierClientCardRepository;
    @Autowired
    private OrderPaymentRepository orderPaymentRepository;
    @Autowired
    private ClientCourierService clientCourierService;



    private CourierClientCard buildCourierClientCard(Client client){
        CourierClientCard card = new CourierClientCard();
        card.setClient(client);
        card.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6());
        card.setPaymentState(PaymentState.WAIT_TO_HOLD);
        card = courierClientCardRepository.save(card);
        card.setGenerateNumber(card.getId()+":"+CARD_PREFIX);
        courierClientCardRepository.save(card);
        return card;
    }


    public String generatePaymentUrl(Client client){
        CourierClientCard card = buildCourierClientCard(client);
        PayOnlineRequest request = RequestPreparer.payment(card.getGenerateNumber(), 1, "RUB");
        LOGGER.info("request: " + request.toUrl());
        return request.toUrl();
    }






    public void parseFailPaymentAnswer(Map<String, String> parameters){
        if(CollectionUtils.isEmpty(parameters)){
            return;
        }

        LOGGER.info(" --- ParseFailPaymentAnswer requestParams: " + parameters);

        String transactionID = parameters.get("TransactionID");
        String orderId = parameters.get("OrderId");
        String amount = parameters.get("Amount");
        String securityKey = parameters.get("SecurityKey");
        String cardHolder = parameters.get("CardHolder");
        String cardNumber = parameters.get("CardNumber");
        String code = parameters.get("Code");
        String errorCode = parameters.get("ErrorCode");

        String mass[] = orderId.split(":");
        boolean initialCard = false;
        long resultId;
        if(!StringUtils.isEmpty(orderId)){
            initialCard = orderId.contains(CARD_PREFIX);
        }
        resultId = Long.parseLong(mass[0].toString());

        LOGGER.info("In parseFailPaymentAnswer: resultId = " + resultId + " initialCard = " + initialCard);

        CourierClientCard courierClientCard = null;
        OrderPayment orderPayment = null;
        if(initialCard){
            courierClientCard = courierClientCardRepository.findOne(resultId);
        } else {
            orderPayment = orderPaymentRepository.findOne(resultId);
        }

        LOGGER.info("ParseFailPaymentAnswer: courierClientCard = " + courierClientCard + " | orderPayment = " + orderPayment);

        if(courierClientCard != null){
            // ответ по операции по привязке карты
            // todo: втулить коды ошибок и взависимости от них ставить нужный стейт PaymentState. Здесь, скорее всего, придется еще и закомплитить заказ чтобы можно было корректно работать с ребилАнкор
            courierClientCard.setPaymentState(PaymentState.FAILED_HOLD);
            courierClientCard.setCardHolder(cardHolder);
            courierClientCard.setCardNumber(cardNumber);
            //courierClientCard.setRebillAnchor(rebillAnchor);
            double doublePrice = Double.parseDouble(amount);
            int intPrice = (int)(doublePrice * 100);
            courierClientCard.setAmount(intPrice);
            courierClientCard.setTransactionID(transactionID);
            courierClientCard.setCode(code);
            courierClientCard.setErrorCode(errorCode);
            courierClientCard.setSecurityKey(securityKey);
            courierClientCard.setActive(false);
            courierClientCardRepository.save(courierClientCard);
        } else {
            if(orderPayment != null){
                orderPayment.setPaymentState(PaymentState.FAILED_HOLD);
                orderPayment.setTransactionId(transactionID);
                orderPayment.setTimeOfLastUpdate(DateTimeUtils.nowNovosib_GMT6());
                orderPayment.setErrorCode(errorCode);
                orderPaymentRepository.save(orderPayment);
                // todo: событие нода оповещаюего клиента о неудачной транзакции
            } else {
                LOGGER.info("~~~parseFailPaymentAnswer orderPayment by orderId: " + orderId + " transactionId: " + transactionID + " not found !!!!!!");
            }
        }
    }






    public void parseSuccessfullyPaymentAnswer(Map<String, String> parameters){
        boolean initialCard = false;
        long resultId;
        String transactionID = parameters.get("TransactionID");
        String orderId = parameters.get("OrderId");
        String amount = parameters.get("Amount");
        String securityKey = parameters.get("SecurityKey");
        String rebillAnchor = parameters.get("RebillAnchor");
        String cardHolder = parameters.get("CardHolder");
        String cardNumber = parameters.get("CardNumber");
        String code = parameters.get("Code");
        String errorCode = parameters.get("ErrorCode");

        String resultSplit[] = orderId.split(":");

        if(!StringUtils.isEmpty(orderId)){
            initialCard = orderId.contains(CARD_PREFIX);
        }

        resultId = Long.parseLong(resultSplit[0]);

        LOGGER.info("resultId: " + resultId + " initialCard: " + initialCard + " transactionID: " + transactionID);

        CourierClientCard courierClientCard = null;
        OrderPayment orderPayment = null;
        if(initialCard){
            courierClientCard = courierClientCardRepository.findOne(resultId);
        } else {
            LOGGER.info("333333333333333333333333333333333333333333333333 ID = " + resultId);
            orderPayment = orderPaymentRepository.findOne(resultId);
            LOGGER.info("orderPayment 1 = " + orderPayment);
            OrderPayment orderPayment2 = orderPaymentRepository.getOrderPaymentById(resultId);
            LOGGER.info("orderPayment 2 = " + orderPayment2);

            if(orderPayment == null){
                orderPayment = orderPayment2;
            }
            //orderPayment = orderPaymentRepository.findByTransactionId(transactionID);
        }

        LOGGER.info("ParseSuccessfullyPaymentAnswer: courierClientCard = " + courierClientCard + " | orderPayment = " + orderPayment); //  + " code: " + code + " errorCode: " + errorCode

        if(courierClientCard != null) { //  && (EnumSet.of(PaymentState.GOOD_PAYMENT, PaymentState.FAILED_PAYMENT).contains(courierClientCard.getPaymentState()))
            // ответ по операции по привязке карты
            // todo: втулить коды ошибок и взависимости от них ставить нужный стейт PaymentState. Здесь, скорее всего, придется еще и закомплитить заказ чтобы можно было корректно работать с ребилАнкор

            courierClientCard.setPaymentState(PaymentState.HOLD);
            courierClientCard.setCardHolder(cardHolder);
            courierClientCard.setCardNumber(cardNumber);
            courierClientCard.setRebillAnchor(rebillAnchor);
            double doublePrice = Double.parseDouble(amount);
            int intPrice = (int)(doublePrice * 100);
            courierClientCard.setAmount(intPrice);
            courierClientCard.setTransactionID(transactionID);
            courierClientCard.setCode(code);
            courierClientCard.setErrorCode(errorCode);
            courierClientCard.setSecurityKey(securityKey);
            courierClientCard.setActive(true);
            courierClientCardRepository.save(courierClientCard);

            clientCourierService.setAllClientCardNotActive(courierClientCard.getClient());
            courierClientCard.setActive(true);
            courierClientCardRepository.save(courierClientCard);
        } else {
            if(orderPayment != null){
                // todo: поставить проверку на то, заказ перешел в холд или закомплитился
                orderPayment.setTransactionId(transactionID);
                orderPayment.setPaymentState(PaymentState.HOLD);
                orderPayment.setTimeOfLastUpdate(DateTimeUtils.nowNovosib_GMT6());
                orderPaymentRepository.save(orderPayment);

                ClientCourierService.NotifiedHelper notifiedHelper = clientCourierService.isRequireNotifiedClient(orderPayment.getOrder());
                if(notifiedHelper.isRequireStartSearch()){
                    /* если заказ без курьера - стартуем поиск */
                    clientCourierService.startSearch(orderPayment.getOrder());
                    LOGGER.info("START SEARCH AFTER parseSuccessfullyPaymentAnswer");
                }
            } else {
                LOGGER.info("~~~ parseSuccessfullyPaymentAnswer orderPayment by orderId: " + orderId + " transactionId: " + transactionID + " not found !!!!!!");
            }
        }
    }







    public void initCompleteHoldByOrder(Order order) {
        StringBuilder stringBuilder = new StringBuilder();
        List<OrderPayment> orderPayments = orderPaymentRepository.findByOrderAndPaymentState(order, PaymentState.HOLD);
        if(CollectionUtils.isEmpty(orderPayments)){
            throw new CustomException(1, "Не найдено ни одной транзакции в статусе HOLD");
        }
        // Status=PreAuthorized - HOLD
        int priceInFact = order.getPriceInFact().getAmount().intValue();

        int sumHold = 0;
        int sumRefund = 0;

        for(OrderPayment orderPayment: orderPayments){
            sumHold += orderPayment.getPriceAmount().getAmountMinorInt();
        }

        stringBuilder.append("--------------------------" + "\n");
        stringBuilder.append("Сумма всех холдов = " + sumHold + "\n");
        stringBuilder.append("Стоимость заказа по факту = " + priceInFact + "\n");

        if(sumHold == priceInFact){

            stringBuilder.append("Снято с клиента: " + priceInFact);

        } else if(sumHold > priceInFact){
            boolean isComplete = false;

            for(OrderPayment orderPayment: orderPayments){
                int holdAmount = orderPayment.getPriceAmount().getAmountMinorInt();
                if(isComplete){
                    sumRefund += holdAmount;
                    stringBuilder.append(" - Сумма к возврату: " + holdAmount + " № холда: " + orderPayment.getId() + "\n");
                } else {
                    if(priceInFact > holdAmount){
                        priceInFact = priceInFact - holdAmount;

                        finishCompleteHoldByOrder(orderPayment, holdAmount);

                        stringBuilder.append("Сумма холда: " + holdAmount + " Комплит суммы холда: " + holdAmount +" № холда = " + orderPayment.getId() + " Остаток: " + priceInFact +"\n");
                    } else {
                        sumRefund += (holdAmount - priceInFact);
                        isComplete = true;

                        finishCompleteHoldByOrder(orderPayment, priceInFact);

                        stringBuilder.append("Сумма холда: " + holdAmount + " Комплит суммы холда: " + priceInFact + " № холда = " + orderPayment.getId() + " Остаток: " + 0+"\n");
                        stringBuilder.append(" - Сумма к возврату: " + sumRefund + " № холда: " + orderPayment.getId() + "\n");
                    }
                }
            }
        } else {
            stringBuilder.append("Сумма холдов не может быть меньше суммы заказа - EXIT ");
        }
        stringBuilder.append("Общая сумма к возврату: " + sumRefund);

        /*
           todo: здесь видимо нужно пройтись по всем заказам и проверить на всех ли GOOD_PAYMENT, если да - выставляем статус удачной оплаты на заказе из Order
        */

        LOGGER.info(stringBuilder.toString());
    }






    private void finishCompleteHoldByOrder(OrderPayment orderPayment, int sumToComplete){
        LOGGER.info("finishCompleteHoldByOrder ************* OrderPayment ID: " + orderPayment.getId() + " sumToComplete = " + sumToComplete);
        boolean isOk = getOrderStatus(orderPayment.getTransactionId(), "Status=PreAuthorized");
        if(isOk){
            // комплит только в случае если заказ находится в статусе PreAuthorized
            String resultComplete = completeTransaction(sumToComplete / 100.0, orderPayment.getTransactionId());
            if(resultComplete.contains("Result=Ok")){
                LOGGER.info("Result=Ok");
                orderPayment.setPaymentState(PaymentState.GOOD_PAYMENT);
                orderPayment.setPriceAmountInFact(MoneyUtils.getMoney(sumToComplete / 100));
            } else {
                orderPayment.setPaymentState(PaymentState.FAILED_PAYMENT);
            }
            orderPaymentRepository.save(orderPayment);
        } else {
            LOGGER.info("OrderPayment ID: " + orderPayment.getId() + " НЕ НАХОДИТСЯ В СТАТУСЕ PreAuthorized");
        }
    }








    public PayOnlineHelper completeThreeDS(String transactionId, String pARes, String pd){
        PayOnlineHelper payOnlineHelper =  new PayOnlineHelper();
        OrderPayment orderPayment = orderPaymentRepository.findByTransactionId(transactionId);

        if(orderPayment == null){
            throw new CustomException(1, "По текущей транзакции не найдено ни одной записи");
        }
        if(StringUtils.isEmpty(orderPayment.getPd()) && !orderPayment.getPd().equals(pd)){
            throw new CustomException(2, "По текущей транзакции не найдено ни одной записи");
        }
        PayOnlineRequest request = RequestPreparer.completeThreeDS(transactionId, pARes, pd);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
            CloseableHttpResponse httpResponse = client.execute(httpPost);
            String stringResult = EntityUtils.toString(httpResponse.getEntity());
            LOGGER.info("completeThreeDS################## RESULT:  " + stringResult);

            if(!StringUtils.isEmpty(stringResult)){
                boolean containsString = org.apache.commons.lang3.StringUtils.containsIgnoreCase(stringResult, "Result=Ok");
                if(containsString){
                    orderPayment.setPaymentState(PaymentState.HOLD);
                } else {
                    orderPayment.setPaymentState(PaymentState.FAILED_HOLD);
                    payOnlineHelper.setErrorCode("-1");
                }
                orderPayment.setTimeOfLastUpdate(DateTimeUtils.nowNovosib_GMT6());
                orderPaymentRepository.save(orderPayment);
            }

            /*
               todo: обработка запроса, установление статуса заказа в HOLD, нотификация клиента
            */
        } catch (Exception e1) {
            e1.printStackTrace();
        }
            return payOnlineHelper;
    }




    public boolean getOrderStatus(String transactionId, String status) {
        boolean result = false;
        try {
            PayOnlineRequest request = RequestPreparer.search(transactionId, "");
            request = RequestPreparer.addContentType(request, true);
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(request.getUrl());
            httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
            CloseableHttpResponse response = client.execute(httpPost);
            String string = EntityUtils.toString(response.getEntity());

            if(string.contains(status)){
                result = true;
            }
            LOGGER.debug("return string: {}", string);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }


    /*
    Переход на страницу ASCUrl должен быть осуществлен методом POST с передачей параметров PaReq, MD и
    TermUrl:

    PaReq – значение параметра PaReq из ответа сервера PayOnline на вызов Auth.
    MD – номер транзакции и значение параметра PD из ответа сервера PayOnline на вызов Auth, разделенные
    точкой с запятой. Для примера выше MD будет равен
    “1015368;OXf4nrsM4Oi0N7TbFRrQZdbaFQ8M0Dc0WGZUOdBPZ3C2NXIrKlKObWBLTtzeknQY”
    TermUrl – адрес страницы на сервере мерчанта, которая будет обрабатывать результат авторизации
    плательщика на сайте банка-эмитента.
            Пример:
    <form method='post' action='https://dropit.3dsecure.net:9443/PIT/ACS'>
    <input type='hidden' name='PaReq'
    value='eJxVUdFuwjAM/BXEB+AkLYMhY6mjk+hDEdpg711r0W5rC2m6wb5+SSkwHiL5zvbZOeMm18zhK6et
    ZsKYmybZ8aDI5kMp5Nh78IQQaki4Dl74QPjNuinqiuRIjBTCBdpGneZJZQiT9PAUrchXyldjhB5i
    yToKSSrPt+QZYJWUTIYbUzJCBzCt28roE4mJj3AB2Oovyo3ZzwAu5Y5CuI1dty5qrMSxyChaBLu7
    Fz7/xJvt7+ojniO4CswSw6SElGIipwMpZmM5U48IHY9J6WaTGglh/3AGuHczgj7jEv8JtA5qrtIT
    TX2buiLk476u2LUgXGOE28KLpXMtNdYSHfMq36i37WdWHpbvQRHFay+Y2537AqdWWEuk3byTcwDB
    SUB/ImtKd0Qb3R33D+2QoeE=' />
    <input type='hidden' name='TermUrl' value='http://yoursite.com/3ds.aspx' />
    <input type='hidden' name='MD'
    value='1015368;OXf4nrsM4Oi0N7TbFRrQZdbaFQ8M0Dc0WGZUOdBPZ3C2NXIrKlKObWBLTtzeknQY' />
    <input type='submit' value='Submit' />
    </form>

hhh
            После того, как плательщик введет авторизационные данные на странице ASCUrl, он будет перенаправлен на
            страницу, которую вы укажете в TermUrl.
            На страницу TermUrl будут переданы параметры PARes и MD. MD будет содержать то же значение, которое
            передавалось в запросе к ASCUrl.
            Из значения MD необходимо восстановить параметры TransactionID и PD, которые были получены во время
            вызова Auth, после чего можно вызывать метод 3DS.
    */

    public PayOnlineHelper threedSecureInitialize(String acsurl, String pareq, String mD) {
        PayOnlineHelper payOnlineHelper =  new PayOnlineHelper();
        try{
        PayOnlineRequest request = RequestPreparer.secureInitialize(acsurl, pareq, mD);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
        CloseableHttpResponse httpResponse = client.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == 302) {
                LOGGER.info("1. httpResponse.getStatusLine().getStatusCode(): "+httpResponse.getStatusLine().getStatusCode());
                String redirectURL = httpResponse.getFirstHeader("Location").getValue();
                LOGGER.info("redirectURL: " + redirectURL);
                payOnlineHelper.setRedirectUrl(redirectURL);



                /* redirect
                request = RequestPreparer.secureInitialize(acsurl, pareq, mD);
                client = HttpClientBuilder.create().build();
                httpPost = new HttpPost(redirectURL);
                httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
                httpResponse = client.execute(httpPost);
                String str = EntityUtils.toString(httpResponse.getEntity());
                LOGGER.info("str = " + str);
                */

            } else {
                LOGGER.info("2. httpResponse.getStatusLine().getStatusCode(): "+httpResponse.getStatusLine().getStatusCode());

                String string = EntityUtils.toString(httpResponse.getEntity());
                payOnlineHelper.setThreedSecureHtmlPageConfirm(string);
                LOGGER.info("secureInitialize answer: "+string);
            }



        } catch (Exception e1) {
            e1.printStackTrace();
        }
            return payOnlineHelper;
    }




    public String completeTransaction(double amount, String transactionalId) {
        String result = "";
        try{
            PayOnlineRequest request = RequestPreparer.completeTransaction(transactionalId, amount); // order.getPriceExpected().getAmountMinor().doubleValue()
            //request = RequestPreparer.addContentType(request, true);
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(request.getUrl());
            httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
            CloseableHttpResponse httpResponse = client.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
         return result;
    }




    // TransactionId=51208773&Operation=Complete&Amount=100.00&Result=Ok&Message=Completed
    public PayOnlineHelper completeTransaction_OLD(double amount, String transactionalId) {
        PayOnlineHelper payOnlineHelper =  new PayOnlineHelper();
        Persister serializer = new Persister();
        Reader reader;
        String stringXml = "";
        try{
        PayOnlineRequest request = RequestPreparer.completeTransaction(transactionalId, amount); // order.getPriceExpected().getAmountMinor().doubleValue()
        //request = RequestPreparer.addContentType(request, true);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
        CloseableHttpResponse httpResponse = client.execute(httpPost);

        stringXml = EntityUtils.toString(httpResponse.getEntity());


            LOGGER.info("CompleteTransaction answer: " + stringXml);

        reader = new StringReader(stringXml);

        AuthResponse authResponse = serializer.read(AuthResponse.class, reader, false);
        if(authResponse != null){
            payOnlineHelper.setTransactionId(authResponse.getId());
            payOnlineHelper.setOperation(authResponse.getOperation());
            payOnlineHelper.setAmount(authResponse.getAmount());
            payOnlineHelper.setMessage(authResponse.getMessage());
            payOnlineHelper.setResult(authResponse.getResult());
        }
    }catch (Exception e){

        reader = new StringReader(stringXml);

        try {
            ErrorResponse errorResponse = serializer.read(ErrorResponse.class, reader, false);
            payOnlineHelper.setCode(errorResponse.getCode());
            payOnlineHelper.setMessage(errorResponse.getMessage());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
            return payOnlineHelper;
    }




    public PayOnlineHelper search(Order order) {
        PayOnlineHelper payOnlineHelper =  new PayOnlineHelper();
        Persister serializer = new Persister();
        Reader reader;
        String stringXml = null;
        try {
        PayOnlineRequest request = RequestPreparer.search("", order.getId().toString());
        request = RequestPreparer.addContentType(request, true);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
        CloseableHttpResponse httpResponse = client.execute(httpPost);

        stringXml = EntityUtils.toString(httpResponse.getEntity());

        reader = new StringReader(stringXml);

        AuthResponse authResponse = serializer.read(AuthResponse.class, reader, false);
        if(authResponse != null){
            payOnlineHelper.setTransactionId(authResponse.getId());
            payOnlineHelper.setCode(authResponse.getCode());
            payOnlineHelper.setOperation(authResponse.getOperation());
            payOnlineHelper.setDateTime(authResponse.getDateTime());
            payOnlineHelper.setStatus(authResponse.getStatus());
            payOnlineHelper.setAmount(authResponse.getAmount());
        }
        }catch (Exception e){

            reader = new StringReader(stringXml);

            try {
                ErrorResponse errorResponse = serializer.read(ErrorResponse.class, reader, false);
                payOnlineHelper.setCode(errorResponse.getCode());
                payOnlineHelper.setMessage(errorResponse.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return payOnlineHelper;
    }


 /*
       <transaction>
        <id>51208773</id>
        <amount>100.00</amount>
        <currency>RUB</currency>
        <orderId>6</orderId>
        <dateTime>2015-09-11 06:52:28</dateTime>
        <status>Pending</status>
       </transaction>
     */


    // создает новый заказ по средством rebillAnchor
    public PayOnlineHelper rebillInitialize(String orderId, double amount, String rebillAnchor){ // "16RCWIHGTUiuFgDQHBTEMnNUzcJtzmh2FlRccZGFn2g="
        PayOnlineHelper payOnlineHelper =  new PayOnlineHelper();
        Persister serializer = new Persister();
        Reader reader;
        String stringXml = null;
        try{
            LOGGER.info("REBILL AMOUNT: " + amount + " orderId: "+orderId);
            // todo: ставим для тестов 1 рубль

            PayOnlineRequest request = RequestPreparer.rebill(orderId, 1, "RUB", rebillAnchor); // amount
            request = RequestPreparer.addContentType(request, true);
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(request.getUrl());
            httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
            CloseableHttpResponse httpResponse = client.execute(httpPost);
            stringXml = EntityUtils.toString(httpResponse.getEntity());
            LOGGER.info("stringXml = "+stringXml);

            reader = new StringReader(stringXml);

            /*

<transaction>
  <id>55179066</id>
  <operation>Rebill</operation>
  <result>OK</result>
  <status>PreAuthorized</status>
  <code>200</code>
</transaction>






                 <transaction>
  <id>52178039</id>
  <operation>Rebill</operation>
  <result>Error</result>
  <status>Awaiting3DAuthentication</status>
  <code>6001</code>
  <errorCode>4</errorCode>
  <threedSecure>
    <pareq>eJxVUl1TwjAQ/CtMH52x+WgLlDnCFBmV8Qu1Iq8ljW3VpJi2Cv56k1oE33b3ks3dXmCyle+9T6GrolRjh7jY6QnFy7RQ2dh5is9Ph86EQZxrIWaPgjdaMLgRVZVkolekY2cRPYiP0TJbXg0W0Td+yeXFYkfVRRCvModBW2bQPcCMv0sB7alx0jxPVM0g4R/T+S3z+wHFfUAdBSn0fNapmHpDEmBMAP3KoBIp2NX59CSOVvPH+A5QqwAvG1XrHev7HqA9gUa/s7yuNyOEvLRypVwn6s3VDQJkS4AO3SwaiypjtS1S9qXq6/vwuZRb/Xa9jpXU9LUM51nE+RiQPQFpUgtGsWkvpEGPeCPqjQIzR6tDIm0Ppo7NZL8YNvaJ6KhwLIAJWps97EfYMxDbTamEvQLoD0MqKs6oyaUFgA7tn13aaHlt0iI+JaFHfRK6Qxr0gxB7wQB7lLZxt0esfWGiIgOCW39LAFkT1G0Sdcs36N+n+AHRAr0j</pareq>
    <acsurl>https://acs.alfabank.ru/acs/PAReq</acsurl>
    <pd>zC6vgK9bbX0S5WZr/Sq6jNONjIx5pSDaP+E82Mx9l9Pz5fFzdvXynn2t0Kd3k7LF</pd>
  </threedSecure>
</transaction>
             */

            AuthResponse authResponse = serializer.read(AuthResponse.class, reader, false);
            if(authResponse != null){
                payOnlineHelper.setTransactionId(authResponse.getId());
                payOnlineHelper.setOperation(authResponse.getOperation());
                payOnlineHelper.setAmount(authResponse.getAmount());
                payOnlineHelper.setMessage(authResponse.getMessage());
                payOnlineHelper.setCode(authResponse.getCode());
                payOnlineHelper.setErrorCode(authResponse.getErrorCode());
                payOnlineHelper.setStatus(authResponse.getStatus());
                if(authResponse.getThreedSecure() != null){
                    payOnlineHelper.setAcsurl(authResponse.getThreedSecure().getAcsUrl());
                    payOnlineHelper.setPareq(authResponse.getThreedSecure().getPaReq());
                    payOnlineHelper.setPd(authResponse.getThreedSecure().getPd());
                }
            }
        }catch (Exception e){

            reader = new StringReader(stringXml);

            try {
                ErrorResponse errorResponse = serializer.read(ErrorResponse.class, reader, false);
                payOnlineHelper.setCode(errorResponse.getCode());
                payOnlineHelper.setMessage(errorResponse.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return payOnlineHelper;
    }


    /*
2012 и 2015 - Страна IP адреса не определена.
2050/2053 –Превышена максимальная сумма транзакции
2051/2054 – Превышена максимальная сумма покупок с одной карты в сутки
2052/2055 - Превышена максимальная сумма покупок для Интернет-ресурса (причину клиенту не сообщаем)
2060 - Превышены лимиты для AccounId
2061/2065 –Превышено максимальное количество транзакций с одной карты
2062/2063 - Превышено максимальное количество попыток оплаты с одного IP
2064/2066 - Превышено максимальное количество покупок для Мерчанта (причину клиенту не сообщаем)
2100 -  Карта ранее использовалась с другим именем держателя
2101 -  E-mail  ранее был использован с картой под другим именем держателя
2110 -  Превышено максимальное количество попыток оплаты по одной банковской карте
2111 -  Превышено максимальное количество попыток оплаты с одного IP адреса
2116 -  IP компьютера плательщика не совпадает со страной выпуска карты
2117 -  Страна выпуска карты не совпадает со страной, указанной при заполнении формы
2124 - BIN банка эмитента не соответствует стране IP

3001 - Техническая ошибка на стороне банка

4001-4003 -  Ошибка произошла в результате неактивности аккаунта мерчанта, его неполного подключения
4004-4019 -  Клиент вводит некорректную информацию/опечатывается и т.п.: номер карты, имейл адрес, CVV2 и и проч

5204/5308/5334 – Платеж отклонен Банком-эквайером
5205 - Платеж отклонен банком эмитентом, так как на карте недостаточно средств
5301 - Платеж отклонен банком эмитентом, так как истек срок действия карты
5310 - Платеж отклонен банком эмитентом, так как превышен лимит по карте, установленный банком эмитентом
5411 - Платеж отклонен Банком эмитентом, так как неверно введен CVV2/CVC2

6001 - Ожидает ввода кода 3D-Secure
6002/6004 - Платеж отклонен банком из-за ошибки 3D Secure на стороне банка – эмитента
6003 – Пользователь ввел неверный код 3D-Secure
     */



    public static class PayOnlineHelperException{
        public static String wrapException(String code, String errorCode){
            String description = "";
            if(!StringUtils.isEmpty(errorCode)){
                description = errorCodeDescription(errorCode);
            }
            return description;
        }
    }




    private static String errorCodeDescription(String errorCode){
        String errorCodeDescription = "";
        switch (errorCode) {
            case "1": {
                errorCodeDescription = "возникла техническая ошибка, повторите\n" +
                        "    попытку через 10 мин";
                break;
            }
            case "2": {
                errorCodeDescription = "транзакция отклонена фильтрами, повторите\n" +
                        "    через СУТКИ но не более 5 попыток, после\n" +
                        "    требуется повторное прохождение плательщика\n" +
                        "    через форму";
                break;
            }
            case "3": {
                errorCodeDescription = "транзакция отклоняется банком-эмитентом.\n" +
                        "    Возможен повтор попыток не более пяти раз в\n" +
                        "    сутки в течение 3 дней";
                break;
            }
            case "4": {
                errorCodeDescription = "транзакция отклоняется банком-эмитентом.\n" +
                        "    Следует прекратить дальнейшие операции Rebill\n" +
                        "    с данным RebillAnchor";
                break;
            }
            default: break;
        }
          return errorCodeDescription;
    }




    public PayOnlineHelper createPreAuth(Order order, String ip, int sum){
        PayOnlineHelper payOnlineHelper =  new PayOnlineHelper();
        Persister serializer = new Persister();
        Reader reader;
        String stringXml = null;
        try {

            LOGGER.info("sum to hold: "+ sum);

            PayOnlineRequest request = RequestPreparer.auth(order.getId().toString(), sum, "RUB");

            //ClientCard clientCard = clientCardRepository.findByClientAndActive(order.getClient(), Boolean.TRUE);

            /* test */
            request = RequestPreparer.addCardInfoTest(request);
            request = RequestPreparer.addContentType(request, true);
            request = RequestPreparer.addEmail(request);
            request = RequestPreparer.addIp(request, "192.168.1.1"); // "192.168.1.1"

            /*
            Рабочий вариант
            todo: где взять cvc
            request = RequestPreparer.addCardInfo(request, clientCard.getCardholderName(), clientCard.getPan(), clientCard.getExpirationYear().toString(), clientCard.getExpirationMonth().toString(), "cvv");
            request = RequestPreparer.addContentType(request, true);
            request = RequestPreparer.addEmail(request);
            request = RequestPreparer.addIp(request, ip);
            */

            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(request.getUrl());
            httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
            CloseableHttpResponse httpResponse = client.execute(httpPost);

            stringXml = EntityUtils.toString(httpResponse.getEntity());

            LOGGER.info("stringXml normal: "+ stringXml);

            reader = new StringReader(stringXml);

            AuthResponse authResponse = serializer.read(AuthResponse.class, reader, false);
            if(authResponse != null){
                payOnlineHelper.setTransactionId(authResponse.getId());
                payOnlineHelper.setCode(authResponse.getCode());
                payOnlineHelper.setOperation(authResponse.getOperation());
                payOnlineHelper.setResult(authResponse.getResult());
            }
        }catch (Exception e){

            LOGGER.info("stringXml error: "+ stringXml);

            reader = new StringReader(stringXml);

            try {
                ErrorResponse errorResponse = serializer.read(ErrorResponse.class, reader, false);
                payOnlineHelper.setCode(errorResponse.getCode());
                payOnlineHelper.setMessage(errorResponse.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return payOnlineHelper;
    }



}
