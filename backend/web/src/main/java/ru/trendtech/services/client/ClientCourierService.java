package ru.trendtech.services.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.BindingCardResponse;
import ru.trendtech.common.mobileexchange.model.common.CourierCalculatePriceResponse;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.courier.*;
import ru.trendtech.common.mobileexchange.model.courier.client.*;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.ClientCashFlow;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverLocation;
import ru.trendtech.domain.billing.Account;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.domain.courier.*;
import ru.trendtech.integration.PayOnlineHelper;
import ru.trendtech.integration.payonline.PayOnlineRequest;
import ru.trendtech.integration.payonline.RequestPreparer;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.*;
import ru.trendtech.repositories.billing.AccountRepository;
import ru.trendtech.repositories.courier.*;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.billing.BillingService;
import ru.trendtech.services.billing.CourierBillingService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.courier.OrderService;
import ru.trendtech.services.driver.search.FindDriversService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by petr on 25.08.2015.
 */
@Service
@Transactional
public class ClientCourierService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCourierService.class);
    private static String THREED_SECURE_CODE = "6001";
    private static String TERM_URL = "http://dev.taxisto.ru/payment/payonline/termInit";
    @Value("${order.prefix}")
    private String ORDER_PREFIX;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private OrderAddressRepository orderAddressRepository;
    @Autowired
    private OrderItemPriceRepository orderItemPriceRepository;
    @Autowired
    private ActivationQueueRepository activationQueueRepository;
    @Autowired
    private ClientItemRepository clientItemRepository;
    @Autowired
    private CustomWindowRepository customWindowRepository;
    @Autowired
    private CustomWindowUsesRepository customWindowUsesRepository;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;
    @Autowired
    private OrderPaymentRepository orderPaymentRepository;
    @Autowired
    private CourierBillingService courierBillingService;
    @Autowired
    private EstimateCourierRepository estimateCourierRepository;
    @Autowired
    private CourierClientCardRepository courierClientCardRepository;
    @Autowired
    private FindDriversService findDriversService;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ClientCashFlowRepository clientCashFlowRepository;
    @Autowired
    private OrderStateHistoryRepository orderStateHistoryRepository;
    @Autowired
    private DefaultPriceRepository defaultPriceRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private AccountRepository accountRepository;





    public void updateBonuses(Client client, int bonusAmount){
        Money plusBonus = MoneyUtils.getBonuses(bonusAmount / 100);
        Account account = client.getAccount();
        Money currentBonuses = account.getCourierBonuses();
        currentBonuses.plus(plusBonus);
        accountRepository.save(account);
    }




    public BindingCardResponse bindingClientCard(Client client){
        BindingCardResponse response = new BindingCardResponse();
        String url = courierBillingService.generatePaymentUrl(client);
        response.setUrl(url);
        return response;
    }


    public ClientCardResponse card(long clientId){
        ClientCardResponse response = new ClientCardResponse();
        List<CourierClientCard> clientCards = courierClientCardRepository.findByClientAndIsDeleteAndCardNumberIsNotNullAndRebillAnchorIsNotNullAndPaymentState(clientRepository.findOne(clientId), Boolean.FALSE, PaymentState.HOLD);
        if(!CollectionUtils.isEmpty(clientCards)){
            for(CourierClientCard card: clientCards){
                 response.getCourierClientCardInfos().add(ModelsUtils.toModel(card));
            }
        }
        return response;
    }



    public void setAllClientCardNotActive(Client client){
        List<CourierClientCard> courierClientCards = courierClientCardRepository.findByClientAndActive(client, Boolean.TRUE);
        if(!courierClientCards.isEmpty()){
            for(CourierClientCard clientCard: courierClientCards){
                clientCard.setActive(false);
                courierClientCardRepository.save(clientCard);
            }
        }
    }


    public DeleteCardResponse updateCard(long clientId, long cardId, boolean delete, boolean active){
        Client client = clientRepository.findOne(clientId);
        CourierClientCard card = courierClientCardRepository.findOne(cardId);
        if(card == null){
            throw new CustomException(1, "Карта не найдена");
        }
            DeleteCardResponse response = new DeleteCardResponse();
            setAllClientCardNotActive(client);
            card.setIsDelete(delete);
            if(delete){
                card.setActive(Boolean.FALSE);
            } else{
                card.setActive(active);
            }
            courierClientCardRepository.save(card);
            return response;
    }



    private String paymentOrderUrl(Client client){
        String url = "";
        BindingCardResponse response = new BindingCardResponse();
        //courierClientCardRepository
        return url;
    }




    public OrderPayment createOrderPayment(Order order, CourierClientCard courierClientCard, int holdAmount){
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setClient(order.getClient());
        orderPayment.setOrder(order);
        orderPayment.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6());
        orderPayment.setPaymentState(PaymentState.WAIT_TO_HOLD);
        orderPayment.setPriceAmount(MoneyUtils.getMoney(holdAmount / 100));
        orderPayment.setCourierClientCard(courierClientCard);
        orderPayment = orderPaymentRepository.save(orderPayment);
        orderPayment.setGenerateNumber(orderPayment.getId() + ":" + order.getId() + ":" + ORDER_PREFIX);
        orderPaymentRepository.save(orderPayment);
        return orderPayment;
    }


    /*
    1 - возникла техническая ошибка, повторите
    попытку через 10 мин.
    2 - транзакция отклонена фильтрами, повторите
    через СУТКИ но не более 5 попыток, после
    требуется повторное прохождение плательщика
    через форму;
    3 - транзакция отклоняется банком-эмитентом.
    Возможен повтор попыток не более пяти раз в
    сутки в течение 3 дней;
    4 - транзакция отклоняется банком-эмитентом.
    Следует прекратить дальнейшие операции Rebill
    с данным RebillAnchor.
    */

    public OrderPayment fillOrderPayment(OrderPayment orderPayment, PayOnlineHelper payOnlineHelper){
        String description = CourierBillingService.PayOnlineHelperException.wrapException(payOnlineHelper.getCode(), payOnlineHelper.getErrorCode());
        if (payOnlineHelper.getCode().equals(THREED_SECURE_CODE)) {
            orderPayment.setPaymentState(PaymentState.REQUIRED_THREED_SECURE);
        }
        orderPayment.setErrorCode(payOnlineHelper.getCode());
        orderPayment.setMessage(payOnlineHelper.getMessage() + " " + description);
        orderPayment.setTransactionId(payOnlineHelper.getTransactionId());
        orderPayment.setAcsurl(payOnlineHelper.getAcsurl());
        orderPayment.setPareq(payOnlineHelper.getPareq());
        orderPayment.setMd(payOnlineHelper.getTransactionId() + "," + payOnlineHelper.getPd());
        return orderPayment;
    }




    private CustomWindowInfo getCustomWindowInfoForThreedSecureHtmlPageConfirm(PayOnlineHelper payOnlineHelper, OrderPayment orderPayment){
        CustomWindowInfo customWindowInfo = ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.REQUIRED_THREED_SECURE));
        Document doc = Jsoup.parse(payOnlineHelper.getThreedSecureHtmlPageConfirm());
        if(doc != null){
            Elements f = doc.getElementsByAttributeStarting("action");
            LOGGER.info("payOnlineHelper.getAcsurl(): " + payOnlineHelper.getAcsurl());
            if(f != null && !StringUtils.isEmpty(payOnlineHelper.getAcsurl())){
                f.attr("action", payOnlineHelper.getAcsurl());
            }
        }
        //customWindowInfo.setHtmlText(payOnlineHelper.getThreedSecureHtmlPageConfirm());
        return customWindowInfo;
    }




    private String clobToString(Clob data) {
        StringBuilder sb = new StringBuilder();
        try {
            Reader reader = data.getCharacterStream();
            BufferedReader br = new BufferedReader(reader);

            String line;
            while(null != (line = br.readLine())) {
                sb.append(line);
            }
            br.close();
        } catch (SQLException e) {
            // handle this exception
        } catch (IOException e) {
            // handle this exception
        }


        /*
                Session session = entityManager.unwrap(Session.class);
                Clob clob = Hibernate.getLobCreator(session).createClob(payOnlineHelper.getThreedSecureHtmlPageConfirm());
                orderPayment.setHtmlPageForConfirmedThreedSecure(clob);
                orderPaymentRepository.save(orderPayment);
         */


        return sb.toString();
    }




//    private PayOnlineHelper startRebill(OrderPayment orderPayment, CourierClientCard courierClientCard){
//        PayOnlineHelper payOnlineHelper =  courierBillingService.rebillInitialize(orderPayment.getGenerateNumber(), orderPayment.getPriceAmount().getAmountMajorInt(), courierClientCard.getRebillAnchor());   // courierBillingService.createPreAuth(order, ipAddress, orderPayment.getPriceAmount().getAmountMajorInt());
//
//        if(StringUtils.isEmpty(payOnlineHelper.getTransactionId())){
//            orderPayment.setPaymentState(PaymentState.FAILED_HOLD);
//            orderPaymentRepository.save(orderPayment);
//            throw new CustomException(1, "При попытке захолдировать сумму произошла ошибка: " + payOnlineHelper.getMessage());
//        }
//
//        LOGGER.info("payOnlineHelper: " + payOnlineHelper.getMessage() + " Status: " + payOnlineHelper.getStatus() + " Code: " + payOnlineHelper.getCode() + "ascUrl: " +payOnlineHelper.getAcsurl());
//
//        orderPayment.setErrorCode(payOnlineHelper.getCode());
//        orderPayment.setMessage(payOnlineHelper.getMessage());
//        orderPayment.setTransactionId(payOnlineHelper.getTransactionId());
//
//        if(payOnlineHelper.getCode().equals(THREED_SECURE_CODE)) {
//            // код 6001 - требуется прохождение 3dsecure
//            LOGGER.info("THREED_SECURE_CODE");
//
//            String MD = payOnlineHelper.getTransactionId() + "," + payOnlineHelper.getPd();
//            LOGGER.info("MD: " + MD + " ascUrl: " + payOnlineHelper.getAcsurl());
//
//            //orderPayment = fillOrderPayment(orderPayment, payOnlineHelper, true);
//            orderPayment.setPaymentState(PaymentState.REQUIRED_THREED_SECURE);
//            orderPayment.setAcsurl(payOnlineHelper.getAcsurl());
//            orderPayment.setPareq(payOnlineHelper.getPareq());
//            orderPayment.setMd(payOnlineHelper.getTransactionId() + "," + payOnlineHelper.getPd());
//            orderPaymentRepository.save(orderPayment);
//
//            payOnlineHelper = courierBillingService.threedSecureInitialize(payOnlineHelper.getAcsurl(), payOnlineHelper.getPareq(), MD);
//
//            Session session = entityManager.unwrap(Session.class);
//            Clob clob = Hibernate.getLobCreator(session).createClob(payOnlineHelper.getThreedSecureHtmlPageConfirm());
//            orderPayment.setHtmlPageForConfirmedThreedSecure(clob);
//            orderPaymentRepository.save(orderPayment);
//        }
//
//    }




    public ConfirmedOrderResponse confirmedOrder(Order order, String ipAddress){
        ConfirmedOrderResponse response = new ConfirmedOrderResponse();
        List<OrderPayment> orderPayments = orderPaymentRepository.findByOrderOrderByTimeOfRequestingDesc(order, new PageRequest(0, 1));
        int newPriceAmount = order.getPriceExpected().getAmountMajorInt();
        int previousSum = 0;
        int difference;

        CourierClientCard courierClientCard;
        List<CourierClientCard> courierClientCards = courierClientCardRepository.findByClientAndActive(order.getClient(), Boolean.TRUE, new PageRequest(0, 1));

        if(CollectionUtils.isEmpty(courierClientCards)){
            throw new CustomException(1, "Нет активной карты");
        }

        courierClientCard = courierClientCards.get(0);

        /*
           здесь нужно поставить проверку на то, произошла ли оплата за рублевый заказа - completed
        */
        //if(StringUtils.isEmpty(courierClientCard.getRebillAnchor())){
        //    throw new CustomException(2, "Не найден RebillAnchor");
        //}

        OrderPayment orderPayment = null;
        if(!CollectionUtils.isEmpty(orderPayments)){
            orderPayment = orderPayments.get(0);
            previousSum = orderPayment.getPriceAmount() != null ? orderPayment.getPriceAmount().getAmountMajorInt() : 0;

            if(orderPayment.getPaymentState().equals(PaymentState.WAIT_TO_HOLD) || orderPayment.getPaymentState().equals(PaymentState.REQUIRED_THREED_SECURE)){
                //throw new CustomException(1, String.format("Невозможно подвердить заказ. Предыдущая транзакция на сумму %s - открыта", orderPayment.getPriceAmount().getAmount().intValue()));


            }
        } else {
               orderPayment = createOrderPayment(order, courierClientCard, 0);
        }

        /*
        if(orderPayment == null){
            orderPayment = createOrderPayment(order, courierClientCard);
        } else {
            if(orderPayment.getPaymentState().equals(PaymentState.WAIT_TO_HOLD)){
                throw new CustomException(1, String.format("Невозможно подвердить заказ. Не захолдирована предыдущая транзакция на сумму %s ", orderPayment.getPriceAmount().getAmount().intValue()));
            }
        }
        */

        difference = previousSum - newPriceAmount;
        boolean sumUp = false;
        if(difference < 0){
            sumUp = true;
        }

        if(sumUp){
            LOGGER.info("sumUp = true");
            orderPayment = createOrderPayment(order, courierClientCard, 0);
        } else {
            LOGGER.info("sumUp = false");
            if(orderPayment == null) {
                orderPayment = createOrderPayment(order, courierClientCard, 0);
            } else {
                orderPayment.setTimeOfLastUpdate(DateTimeUtils.nowNovosib_GMT6());
            }
        }

        //orderPayment = orderPaymentRepository.save(orderPayment);
        //orderPayment.setGenerateNumber(orderPayment.getId()+":"+order.getId());
        //orderPaymentRepository.save(orderPayment);

        // значит были изменения по стоимости
        //PriceChanges priceChanges = buildPriceChange(orderPayment, difference);
        //priceChangesRepository.save(priceChanges);


        if(orderPayment.getPaymentState().equals(PaymentState.WAIT_TO_HOLD)){
            // делаем запрос на rebill
            PayOnlineHelper payOnlineHelper =  courierBillingService.rebillInitialize(orderPayment.getGenerateNumber(), orderPayment.getPriceAmount().getAmountMajorInt(), courierClientCard.getRebillAnchor());   // courierBillingService.createPreAuth(order, ipAddress, orderPayment.getPriceAmount().getAmountMajorInt());

            if(StringUtils.isEmpty(payOnlineHelper.getTransactionId())){
                orderPayment.setPaymentState(PaymentState.FAILED_HOLD);
                orderPaymentRepository.save(orderPayment);
                throw new CustomException(1, "При попытке захолдировать сумму произошла ошибка: " + payOnlineHelper.getMessage());
            }

            LOGGER.info("payOnlineHelper: " + payOnlineHelper.getMessage() + " Status: " + payOnlineHelper.getStatus() + " Code: " + payOnlineHelper.getCode() + "ascUrl: " +payOnlineHelper.getAcsurl());

            orderPayment.setErrorCode(payOnlineHelper.getCode());
            orderPayment.setMessage(payOnlineHelper.getMessage());
            orderPayment.setTransactionId(payOnlineHelper.getTransactionId());

            if(payOnlineHelper.getCode().equals(THREED_SECURE_CODE)){
                // код 6001 - требуется прохождение 3dsecure
                LOGGER.info("THREED_SECURE_CODE");

                String MD = payOnlineHelper.getTransactionId() + "," + payOnlineHelper.getPd();
                LOGGER.info("MD: " + MD + " ascUrl: " + payOnlineHelper.getAcsurl());

                //orderPayment = fillOrderPayment(orderPayment, payOnlineHelper, true);
                orderPayment.setPaymentState(PaymentState.REQUIRED_THREED_SECURE);
                orderPayment.setAcsurl(payOnlineHelper.getAcsurl());
                orderPayment.setPareq(payOnlineHelper.getPareq());
                orderPayment.setMd(payOnlineHelper.getTransactionId() + "," + payOnlineHelper.getPd());
                orderPaymentRepository.save(orderPayment);

                payOnlineHelper = courierBillingService.threedSecureInitialize(payOnlineHelper.getAcsurl(), payOnlineHelper.getPareq(), MD);

                response.setCustomWindowInfo(getCustomWindowInfoForThreedSecureHtmlPageConfirm(payOnlineHelper, orderPayment));

            } else {
                orderPayment.setPaymentState(PaymentState.HOLD);
                orderPaymentRepository.save(orderPayment);

                /* подтверждаем заказ */
                if(order.getDriver() == null){
                    orderService.updateOrderState(order, Order.State.CONFIRMED);
                } else {
                    // todo: убираем пока, т.к. заказ с назначенным водилой мог находится еще и в статусе TAKEN_BY_COURIER. Необходимо сохранить статус заказа до момента когда он стал WAIT_TO_CONFIRM ...
                    //commonService.updateOrderState(order, Order.State.IN_PROGRESS_BY_COURIER);
                }
                CustomWindowInfo customWindowInfo = ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.ORDER_CONFIRMED));
                //customWindowInfo.setHtmlText(clobToString(orderPayment.getHtmlPageForConfirmedThreedSecure()));
                response.setCustomWindowInfo(customWindowInfo);
            }

        } else if(orderPayment.getPaymentState().equals(PaymentState.REQUIRED_THREED_SECURE)){
            // отправляем html код страницы с вводом sms кода

            LOGGER.info("IN orderPayment.getPaymentState().equals(PaymentState.REQUIRED_THREED_SECURE): acsUrl = " + orderPayment.getAcsurl()+" orderPayment.getPareq() = " + orderPayment.getPareq() + " orderPayment.getMd() = " + orderPayment.getMd());

            // todo: здесь нужно будет проверить - нужно ли отправлять еще раз запрос, или достаточно будет взять html код с предыдущего запроса и отдать пользователю???
            PayOnlineHelper payOnlineHelper = courierBillingService.threedSecureInitialize(orderPayment.getAcsurl(), orderPayment.getPareq(), orderPayment.getMd());
            response.setCustomWindowInfo(getCustomWindowInfoForThreedSecureHtmlPageConfirm(payOnlineHelper, orderPayment));
        }


        return response;

        /*

        if(orderPayment.getPaymentState().equals(PaymentState.WAIT_TO_HOLD)){
            //здесь делаю запрос на оплату по урлу ребил - я так понимаю тут 3d secure уже не проверяется, судя по доке
            PayOnlineHelper payOnlineHelper =  courierBillingService.rebillInitialize(orderPayment.getGenerateNumber(), orderPayment.getPriceAmount().getAmountMajorInt(), courierClientCard.getRebillAnchor());   // courierBillingService.createPreAuth(order, ipAddress, orderPayment.getPriceAmount().getAmountMajorInt());

            if(StringUtils.isEmpty(payOnlineHelper.getTransactionId())){
                orderPayment.setPaymentState(PaymentState.FAILED_HOLD);
                orderPaymentRepository.save(orderPayment);
                throw new CustomException(1, "При попытке захолдировать сумму произошла ошибка: " + payOnlineHelper.getMessage());
            }


            LOGGER.info("payOnlineHelper: " + payOnlineHelper.getMessage() + " " + payOnlineHelper.getStatus() + " " + payOnlineHelper.getCode());

            if(payOnlineHelper.getCode().equals(THREED_SECURE_CODE)){
                LOGGER.info("THREED_SECURE_CODE");

                // redirect на ASCUrl
                // PaReq, MD и
                // TermUrl

                String MD = payOnlineHelper.getTransactionId() +","+ payOnlineHelper.getPd();

                LOGGER.info("MD: "+MD);

                // требуется 3dsecure подтверждение
                orderPayment.setPaymentState(PaymentState.REQUIRED_THREED_SECURE);
                orderPayment.setErrorCode(payOnlineHelper.getCode());
                orderPayment.setMessage(payOnlineHelper.getMessage());
                orderPayment.setTransactionId(payOnlineHelper.getTransactionId());
                orderPayment.setAcsurl(payOnlineHelper.getAcsurl());
                orderPayment.setPareq(payOnlineHelper.getPareq());
                orderPayment.setMd(MD);
                orderPaymentRepository.save(orderPayment);


                courierBillingService.threedSecureInitialize(payOnlineHelper.getAcsurl(), payOnlineHelper.getPareq(), MD);fff


            } else {
                orderPayment.setErrorCode(payOnlineHelper.getCode());
                orderPayment.setMessage(payOnlineHelper.getMessage());
                orderPayment.setTransactionId(payOnlineHelper.getTransactionId());
                orderPayment.setPaymentState(PaymentState.HOLD);
                orderPaymentRepository.save(orderPayment);


                // подтверждаем заказ
                if(order.getDriver() == null){
                    commonService.updateOrderState(order, Order.State.CONFIRMED);
                } else {
                    // todo: убираем пока, т.к. заказ с назначенным водилой мог находится еще и в статусе TAKEN_BY_COURIER. Необходимо сохранить статус заказа до момента когда он стал WAIT_TO_CONFIRM ...
                    //commonService.updateOrderState(order, Order.State.IN_PROGRESS_BY_COURIER);
                }
                response.setCustomWindowInfo(ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.ORDER_CONFIRMED)));
            }

            */




            /*
            Получив код 6001, необходимо направить плательщика на страницу, адрес которой указан в параметре ASCUrl.
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




            После того, как плательщик введет авторизационные данные на странице ASCUrl, он будет перенаправлен на
            страницу, которую вы укажете в TermUrl.
            На страницу TermUrl будут переданы параметры PARes и MD. MD будет содержать то же значение, которое
            передавалось в запросе к ASCUrl.
            Из значения MD необходимо восстановить параметры TransactionID и PD, которые были получены во время
            вызова Auth, после чего можно вызывать метод 3DS.
            */
        //}

        //return response;
    }



    // PayOnlineHelper
    public void threedSecureInitialize(long paymentId){
        //Order order = orderRepository.findOne(orderId);
        //if(order == null){
        //    throw new CustomException(1, "Заказ не найден");
        //}
        //List<OrderPayment> orderPayments = orderPaymentRepository.findByOrderAndPaymentStateOrderByTimeOfRequestingDesc(order, PaymentState.REQUIRED_THREED_SECURE, new PageRequest(0, 1));
        //if(!CollectionUtils.isEmpty(orderPayments)){
        //    throw new CustomException(2, "Нет данных для подтверждения");
        //}

        OrderPayment orderPayment =  orderPaymentRepository.findOne(paymentId) ;//orderPayments.get(0);

        LOGGER.info("orderPayment: MD = " + orderPayment.getMd() + " ASCURL = " + orderPayment.getAcsurl());

        PayOnlineHelper payOnlineHelper = courierBillingService.threedSecureInitialize(orderPayment.getAcsurl(), orderPayment.getPareq(), orderPayment.getMd());

    }




     public ConfirmedOrderResponse rebillStart(OrderPayment orderPayment, String rebillAnchor){
         ConfirmedOrderResponse response = new ConfirmedOrderResponse();

         PayOnlineHelper payOnlineHelper = courierBillingService.rebillInitialize(orderPayment.getGenerateNumber(), orderPayment.getPriceAmount().getAmountMajorInt(), rebillAnchor);

         if (StringUtils.isEmpty(payOnlineHelper.getTransactionId())) {
             orderPayment.setPaymentState(PaymentState.FAILED_HOLD);
             orderPaymentRepository.save(orderPayment);
             throw new CustomException(1, "При попытке захолдировать сумму произошла ошибка: " + payOnlineHelper.getMessage());
         }

         String description = CourierBillingService.PayOnlineHelperException.wrapException(payOnlineHelper.getCode(), payOnlineHelper.getErrorCode());

         orderPayment.setErrorCode(payOnlineHelper.getCode());
         orderPayment.setMessage(payOnlineHelper.getMessage() + " " + description);
         orderPayment.setTransactionId(payOnlineHelper.getTransactionId());
         orderPaymentRepository.save(orderPayment);

         LOGGER.info("payOnlineHelper: " + payOnlineHelper.getMessage() + " Status: " + payOnlineHelper.getStatus() + " Code: " + payOnlineHelper.getCode() + "ascUrl: " + payOnlineHelper.getAcsurl());

         if (payOnlineHelper.getCode().equals(THREED_SECURE_CODE)) {
             // код 6001 - требуется прохождение 3dsecure
             orderPayment.setPaymentState(PaymentState.REQUIRED_THREED_SECURE);
             orderPayment.setPd(payOnlineHelper.getPd());
             orderPayment.setAcsurl(payOnlineHelper.getAcsurl());
             orderPayment.setPareq(payOnlineHelper.getPareq());
             orderPayment.setMd(payOnlineHelper.getTransactionId() + ";" + payOnlineHelper.getPd());
             orderPaymentRepository.save(orderPayment);

             response.setCustomWindowInfo(ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.REQUIRED_THREED_SECURE)));
             response.setAcsUrl(orderPayment.getAcsurl());
             response.setPaReq(orderPayment.getPareq());
             response.setPd("");
             response.setMd(orderPayment.getMd());
             response.setTermUrl(TERM_URL);
         } else {
             orderPayment.setPaymentState(PaymentState.WAIT_TO_HOLD);
             orderPaymentRepository.save(orderPayment);
             //response.setCustomWindowInfo(ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.WAIT_HOLD))); // WAIT_HOLD
         }
            return response;
    }




    /* подтверждаем заказ
    if (orderPayment.getOrder().getDriver() == null) {
        //commonService.updateOrderState(orderPayment.getOrder(), Order.State.CONFIRMED);
    } else {
        // todo: убираем пока, т.к. заказ с назначенным водилой мог находится еще и в статусе TAKEN_BY_COURIER. Необходимо сохранить статус заказа до момента когда он стал WAIT_TO_CONFIRM ...
        //commonService.updateOrderState(order, Order.State.IN_PROGRESS_BY_COURIER);
    }
    */

    //customWindowInfo = ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.ORDER_CONFIRMED));
    //response.setHtmlText(clobToString(orderPayment.getHtmlPageForConfirmedThreedSecure()));
    //response.setCustomWindowInfo(customWindowInfo);

    //Session session = entityManager.unwrap(Session.class);
    //Clob clob = Hibernate.getLobCreator(session).createClob(payOnlineHelper.getThreedSecureHtmlPageConfirm());
    //orderPayment.setHtmlPageForConfirmedThreedSecure(clob);


    public RepeatThreeDSecureResponse repeatThreeDS(long orderPaymentId){
        RepeatThreeDSecureResponse response = new RepeatThreeDSecureResponse();
        OrderPayment orderPayment = orderPaymentRepository.findOne(15L);
        PayOnlineHelper payOnlineHelper = courierBillingService.threedSecureInitialize(orderPayment.getAcsurl(), orderPayment.getPareq(), orderPayment.getMd());
        if(payOnlineHelper != null){
            LOGGER.info("redirect: " + payOnlineHelper.getRedirectUrl() + " trans: " + payOnlineHelper.getTransactionId() + " | " + payOnlineHelper.getAcsurl());
            response.setRedirectUrl(payOnlineHelper.getRedirectUrl());
        }
        return response;
    }




    /*
      if(order.getDriver() == null){
            // запускаем поиск только если не был назначен курьер
            showWindowConfirmed = true;
            findDriversService.findCourier(order);
        } else {
            List<Order.State> courierStates = Arrays.asList(Order.State.IN_PROGRESS_BY_COURIER, Order.State.TAKEN_BY_COURIER, Order.State.GO_TO_CLIENT);
            List<OrderStateHistory> orderStateHistories = orderStateHistoryRepository.findByOrderAndStateInOrderByTimeOfChangeDesc(order, courierStates, new PageRequest(0, 1));
            if(!CollectionUtils.isEmpty(orderStateHistories)){
                order.setState(orderStateHistories.get(0).getState());
                orderRepository.save(order);
            } else {
                showWindowConfirmed = true;
            }
        }
    */
    public ConfirmedOrderResponse confirmedOrderWithCash(Order order, String ipAddress) {
        ConfirmedOrderResponse response = new ConfirmedOrderResponse();

        NotifiedHelper notifiedHelper = isRequireNotifiedClient(order);

        if(notifiedHelper.isRequireNotifiedUser()){
            //order.setState(Order.State.CONFIRMED);
            //orderRepository.save(order);
            response.setCustomWindowInfo(ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.ORDER_CONFIRMED)));
        }
        //commonService.saveChangeState(order);
        if(notifiedHelper.isRequireStartSearch()) {
            startSearch(order);
        }
            return response;
    }




    public static class NotifiedHelper {
        boolean requireNotifiedUser = false;
        boolean requireStartSearch = false;

        public boolean isRequireNotifiedUser() {
            return requireNotifiedUser;
        }

        public void setRequireNotifiedUser(boolean requireNotifiedUser) {
            this.requireNotifiedUser = requireNotifiedUser;
        }

        public boolean isRequireStartSearch() {
            return requireStartSearch;
        }

        public void setRequireStartSearch(boolean requireStartSearch) {
            this.requireStartSearch = requireStartSearch;
        }
    }




    /* проверка на то, требуется ли запуск поиска курьера и оповещения клиента */
    public NotifiedHelper isRequireNotifiedClient(Order order){
        NotifiedHelper notifiedHelper = new NotifiedHelper();
        if(!EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            if(order.getDriver() == null){
                // запускаем поиск только если не был назначен курьер
                notifiedHelper.setRequireNotifiedUser(true);
                notifiedHelper.setRequireStartSearch(true);
                orderService.updateOrderState(order, Order.State.CONFIRMED);
            } else {
                List<Order.State> courierStates = Arrays.asList(Order.State.IN_PROGRESS_BY_COURIER, Order.State.TAKEN_BY_COURIER, Order.State.GO_TO_CLIENT);
                List<OrderStateHistory> orderStateHistories = orderStateHistoryRepository.findByOrderAndStateInOrderByTimeOfChangeDesc(order, courierStates, new PageRequest(0, 1));
                if(!CollectionUtils.isEmpty(orderStateHistories)){
                    orderService.updateOrderState(order, orderStateHistories.get(0).getState());
                } else {
                    //requireNotifiedUser = true;
                    notifiedHelper.setRequireNotifiedUser(true);
                    orderService.updateOrderState(order, Order.State.CONFIRMED);
                }
            }
        }
              return notifiedHelper;
    }







    public ConfirmedOrderResponse confirmedOrderWithCard(Order order, String ipAddress) {
        ConfirmedOrderResponse response = new ConfirmedOrderResponse();
        List<OrderPayment> orderPayments = orderPaymentRepository.findByOrder(order);
        int newPriceAmount = order.getPriceExpected().getAmountMinorInt();
        int previousSum = 0;
        int difference = 0;
        CourierClientCard courierClientCard;

        List<CourierClientCard> courierClientCards = courierClientCardRepository.findByClientAndActive(order.getClient(), Boolean.TRUE, new PageRequest(0, 1));

        if (CollectionUtils.isEmpty(courierClientCards)) {
            throw new CustomException(1, "Нет активной карты");
        }

        courierClientCard = courierClientCards.get(0);

        /*
           здесь нужно поставить проверку на то, произошла ли оплата за рублевый заказа - completed
        */
        //if(StringUtils.isEmpty(courierClientCard.getRebillAnchor())){
        //    throw new CustomException(2, "Не найден RebillAnchor");
        //}


        // todo: сделать запрос в payonline на получения акутального статуса заказа

        OrderPayment orderPayment = null;
        if (!CollectionUtils.isEmpty(orderPayments)) {

            List<PaymentState> paymentStateList =  Arrays.asList(PaymentState.FAILED_HOLD, PaymentState.FAILED_PAYMENT, PaymentState.REQUIRED_THREED_SECURE);

            orderPayments = orderPaymentRepository.findByOrderAndPaymentStateIn(order, paymentStateList);
            if(!CollectionUtils.isEmpty(orderPayments)){
                throw new CustomException(2, "Не удалось подтвердить заказ. т.к. по  заказу существуют незавершенные операции");
            }

            /*
            orderPayments = orderPaymentRepository.findByOrderAndPaymentState(order, PaymentState.REQUIRED_THREED_SECURE);
            if(!CollectionUtils.isEmpty(orderPayments)){
                orderPayment = orderPayments.get(0);
                // есть заказы со статусом - требуется 3dsecure авторизация
                CustomWindowInfo customWindowInfo = ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.REQUIRED_THREED_SECURE));
                response.setCustomWindowInfo(customWindowInfo);
                response.setPaReq(orderPayment.getPareq());
                response.setMd(orderPayment.getMd());
                response.setAcsUrl(orderPayment.getAcsurl());
                response.setRedirectUrl(orderPayment.getRedirectUrl());
                return response;
            }
            */


            orderPayments = orderPaymentRepository.findByOrderAndPaymentState(order, PaymentState.HOLD);
            int sumAllHold = 0;
            for(OrderPayment op: orderPayments){
                sumAllHold +=  op.getPriceAmount().getAmountMinorInt();
            }

            difference = sumAllHold - newPriceAmount;

            if(difference < 0){
                // сумма по всем холдам меньше, чем новая сумма - значит создаем новый холд
                LOGGER.info("сумма по всем холдам меньше, чем новая сумма - значит создаем новый холд на сумму: "+ (sumAllHold - newPriceAmount) + " sumAllHold: " + sumAllHold + " newPriceAmount: "+newPriceAmount);
                orderPayment = createOrderPayment(order, courierClientCard, Math.abs(difference));
                response = rebillStart(orderPayment, courierClientCard.getRebillAnchor());
            } else {
                LOGGER.info("сумма по всем холдам больше, чем новая сумма - значит НЕ создаем новый холд. "+ (sumAllHold - newPriceAmount) + " sumAllHold: " + sumAllHold + " newPriceAmount: "+newPriceAmount);
                order.setState(Order.State.CONFIRMED);
                orderRepository.save(order);

                ClientCourierService.NotifiedHelper notifiedHelper = isRequireNotifiedClient(orderPayment.getOrder());
                if(notifiedHelper.isRequireStartSearch()){
                    /* если заказ без курьера - стартуем поиск */
                    startSearch(orderPayment.getOrder());
                    LOGGER.info("START SEARCH AFTER parseSuccessfullyPaymentAnswer");
                }
                // todo: запуск поиска курьера, если
            }

        } else {
            orderPayment = createOrderPayment(order, courierClientCard, order.getPriceExpected().getAmountMinorInt());
            response = rebillStart(orderPayment, courierClientCard.getRebillAnchor());
            // todo: здесь впилить вызов метода threedSecureInitialize в котором я получу урл на который необходимо переадресовать пользователя (ЕСЛИ ПРИШЕЛ 302, а если нет????)
        }
            //nodeJsNotificationsService.courierOrderConfirm(order);
            //findDriversService.findCourier(order);
            return response;
    }
    /*
            difference = previousSum - newPriceAmount;
            boolean sumUp = false;
            if (difference < 0) {
                sumUp = true;
            }
            if (sumUp) {
                LOGGER.info("sumUp = true");
                orderPayment = createOrderPayment(order, courierClientCard);
            } else {
                LOGGER.info("sumUp = false");
                if (orderPayment == null) {
                    orderPayment = createOrderPayment(order, courierClientCard);
                } else {
                    orderPayment.setTimeOfLastUpdate(DateTimeUtils.nowNovosib_GMT6());
                }
            }
    */






    public EstimateCourierResponse estimateCourier(Order order, int general){
        EstimateCourierResponse response = new EstimateCourierResponse();
        EstimateCourier estimateCourier = estimateCourierRepository.findByOrder(order);
        if(estimateCourier != null){
            throw new CustomException(4, "Вы уже оставили оценку");
        }
        estimateCourier = new EstimateCourier();
        estimateCourier.setClient(order.getClient());
        estimateCourier.setDriver(order.getDriver());
        estimateCourier.setEstimateDate(DateTimeUtils.nowNovosib_GMT6());
        estimateCourier.setGeneral(general);
        estimateCourier.setOrder(order);
        estimateCourierRepository.save(estimateCourier);
        //CustomWindow customWindow = customWindowRepository.findByTypeWindow(TypeWindow.COURIER_ESTIMATED);
        //response.setCustomWindowInfo(ModelsUtils.toModel(customWindow));
        return response;
    }





    public OrderCancelResponse orderCancel(Client client, Order order, boolean approve){
        OrderCancelResponse response = new OrderCancelResponse();
        CustomWindowInfo customWindowInfo;
        Driver driver = order.getDriver();
        int fineAmount = 0;

        DefaultPrice defaultPrice = defaultPriceRepository.findByActiveAndOrderType(Boolean.TRUE, order.getOrderType());

        if(driver != null){
            //if(order.getOrderType().equals(OrderType.TAKE_AND_DELIVER)){
                fineAmount = defaultPrice.getFineAmount().getAmount().intValue();
            //} else if(order.getOrderType().equals(OrderType.BUY_AND_DELIVER)){
                //fineAmount = defaultPrice.getFineAmount().getAmount().intValue();
            //}
            // если водитель все купил, снимаем полную сумму стоимости заказа
            if(order.getState().equals(Order.State.GO_TO_CLIENT) && !order.getOrderType().equals(OrderType.TAKE_AND_DELIVER)){
                fineAmount = order.getPriceExpected().getAmount().intValue();
            }
        }

        List<Order.State> courierState = Arrays.asList(Order.State.GO_TO_CLIENT, Order.State.IN_PROGRESS_BY_COURIER);
        List<OrderStateHistory> orderStateHistories = orderStateHistoryRepository.findByOrderAndStateInOrderByTimeOfChangeDesc(order, courierState);
        if(CollectionUtils.isEmpty(orderStateHistories)){
            fineAmount = 0;
        }

        if(approve){
            // подвердить отмену
            cancelOrderByClient(order, fineAmount);
        } else {
            // не подтвердил отмену
            if (fineAmount != 0) {
                customWindowInfo = ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.FINE_CANCELING));
                customWindowInfo.setContentText(String.format(customWindowInfo.getContentText(), fineAmount));
                response.setCustomWindowInfo(customWindowInfo);
            } else {
                //cancelOrderByClient(order, fineAmount);
                customWindowInfo = ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.ORDER_CANCELING));
                response.setCustomWindowInfo(customWindowInfo);
            }
        }
        return response;
    }



    private void cancelOrderByClient(Order order, int fineAmount){
        order.setReasonCancel("Отменен клиентом");
        order.setState(Order.State.CANCELED);
        order.setTimeOfCanceling(DateTimeUtils.nowNovosib_GMT6());
        orderRepository.save(order);

        orderService.saveChangeState(order);

        Client client = order.getClient();
        client.setOrder(null);
        clientRepository.save(client);


        if(fineAmount != 0){
            // делаем запись в ClientCashFlow, отменяем заказ на водиле

            fineClient(order, fineAmount);

            Driver driver = order.getDriver();
            if(driver != null && driver.getCurrentOrder() != null && driver.getCurrentOrder().getId().equals(order.getId())){
                DriverLocation driverLocation = locationRepository.findByDriverId(driver.getId());
                driverLocation.setOrder(null);
                locationRepository.save(driverLocation);

                driver.setCurrentOrder(null);
                driverRepository.save(driver);
            }
        }
            nodeJsNotificationsService.courierClientCancelOrder(order);
    }




    private void fineClient(Order order, int fineAmount){
        ClientCashFlow clientCashFlow = new ClientCashFlow();
        clientCashFlow.setOrder(order);
        clientCashFlow.setClient(order.getClient());
        clientCashFlow.setDateOperation(DateTimeUtils.nowNovosib_GMT6());
        clientCashFlow.setOperation(22);
        clientCashFlow.setSum(fineAmount * 100);
        clientCashFlowRepository.save(clientCashFlow);
    }



    public ConfirmedCustomWindowResponse confirmedCustomWindow(Client client, String typeWindow){
        ConfirmedCustomWindowResponse response = new ConfirmedCustomWindowResponse();
        CustomWindow customWindow = customWindowRepository.findByTypeWindow(TypeWindow.valueOf(typeWindow));
        if(customWindow == null){
            throw new CustomException(2, "Тип окна не найден");
        }
            CustomWindowUses uses = customWindowUsesRepository.findByCustomWindowAndClient(customWindow, client);
            if(uses != null && uses.isShowed()){
                throw new CustomException(1, "Уже зафиксировано!");
            }
            if(uses == null){
                uses = new CustomWindowUses();
                uses.setIsShowed(true);
                uses.setActionType(CustomWindowUses.ActionType.SHOWN_AND_APPROVE);
                uses.setClient(client);
                uses.setCustomWindow(customWindow);
            } else {
                uses.setIsShowed(Boolean.TRUE);
            }
            customWindowUsesRepository.save(uses);
            return response;
    }




    public CourierClientSystemConfigurationResponse getConfiguration(Client client){
        CourierClientSystemConfigurationResponse response = new CourierClientSystemConfigurationResponse();
        Order orderInSearch = getOrderInSearch(client);
        if(orderInSearch != null) {
            OrderInfo orderInfo = ModelsUtils.toModel(orderInSearch, commonService.generalTimeLate(orderInSearch.getId()));
            response.setOrderInfo(orderInfo);
        }
            response.setPercentInsurance(Integer.parseInt(commonService.getPropertyValue("percent_insurance")));
            response.setBookedRangeMin(Integer.parseInt(commonService.getPropertyValue("bookedRangeMin")));
            response.setBookedRangeMax(Integer.parseInt(commonService.getPropertyValue("bookedRangeMax")));
            return response;
    }

    /*
        List<Order> orders = orderRepository.findByClientAndStateAndIsBookedAndDriverIsNull(client, Order.State.CONFIRMED, Boolean.FALSE);
        if(!CollectionUtils.isEmpty(orders)){
            OrderInfo orderInfo = ModelsUtils.toModel(orders.get(0), commonService.generalTimeLate(orders.get(0).getId()));
            response.setOrderInfo(orderInfo);
        }
    */
    /*
     todo: впилить проверку на возраст в случае если в заказе присутствует алкоголь!!!
     if(birthday != null && DateTimeUtils.getCountYearsOld(birthday) < 18){
            throw new CustomException(1, "");
        }
    */



    private CustomWindowInfo operatorIsBusy(ActivationQueue activationQueue){
        CustomWindowInfo customWindowInfo = null;
        List<ActivationQueue> activationQueues = activationQueueRepository.findByTimeOfActivationIsNullOrderByTimeOfRequestAsc();
        if(!CollectionUtils.isEmpty(activationQueues)){
            int clientNumberInQueue = activationQueues.indexOf(activationQueue) + 1;
            CustomWindow customWindow = customWindowRepository.findByTypeWindow(TypeWindow.OPERATOR_IS_BUSY);
            customWindowInfo = ModelsUtils.toModel(customWindow);
            customWindowInfo.setContentText(String.format(customWindowInfo.getContentText(), clientNumberInQueue));
        }
            return customWindowInfo;
    }



    public CustomWindowInfo checkYearsOld(Client client){
        CustomWindowInfo customWindowInfo = null;
        LocalDate birthday = client.getBirthday();
        if(birthday != null){
            Period p = new Period(new DateTime(birthday.getYear(), 1, 1, 0, 0, 0, 0), DateTimeUtils.nowNovosib_GMT6(), PeriodType.years());
            if(p.getYears() < 18){
                customWindowInfo = ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.TOO_YOUNG));
            }
        } else {
            throw new CustomException(1, "Для использования сервиса необходимо указать дату рождения.");
        }
        return customWindowInfo;
    }






    public CanUseServiceResponse canUseService(long clientId){
        Client client = clientRepository.findOne(clientId);
        CanUseServiceResponse response = new CanUseServiceResponse();



        /*
        CustomWindowInfo serviceUnavailable = commonService.checkCourierServiceRestrictionTime(DateTimeUtils.nowNovosib_GMT6());
        if(serviceUnavailable != null){
            response.setCustomWindowInfo(serviceUnavailable);
            return response;
        }


        CustomWindowInfo checkYearsOld = checkYearsOld(client);
        if(checkYearsOld != null){
            response.setCustomWindowInfo(checkYearsOld);
            return response;
        }
        */


        if(getOrderInSearch(client) != null){
            throw new CustomException(1, "У вас есть текущий заказ с поиском курьера");
        }

        ActivationQueue activationQueue = activationQueueRepository.findByClient(client);
        if(activationQueue == null){
            /* окошко: для активации сервиса требуется сделать запрос */
            response.setCustomWindowInfo(ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.REQUIRED_REQUEST)));
            return response;
        } else if(activationQueue.getTimeOfActivation() != null){
            CustomWindow customWindow = customWindowRepository.findByTypeWindow(TypeWindow.ACTIVATION_SERVICE_CONGRATULATION);
            CustomWindowUses uses = customWindowUsesRepository.findByCustomWindowAndClient(customWindow, client);
            if(uses == null || !uses.isShowed()){
                response.setCustomWindowInfo(ModelsUtils.toModel(customWindow));
            }
        } else {
            response.setCustomWindowInfo(operatorIsBusy(activationQueue));
        }
        return response;
    }

    /*
            List<ActivationQueue> activationQueues = activationQueueRepository.findByTimeOfActivationIsNullOrderByTimeOfRequestAsc();
            if(!CollectionUtils.isEmpty(activationQueues)){
                int clientNumberInQueue = activationQueues.indexOf(activationQueue) + 1;

                LOGGER.info("clientNumberInQueue: "+clientNumberInQueue);

                CustomWindow customWindow = customWindowRepository.findByTypeWindow(TypeWindow.OPERATOR_IS_BUSY);
                CustomWindowInfo customWindowInfo = ModelsUtils.toModel(customWindow);
                customWindowInfo.setContentText(String.format(customWindowInfo.getContentText(), clientNumberInQueue));
                response.setCustomWindowInfo(customWindowInfo);
            }
            */





    /* запрос на активацию сервиса */
    public ActivationCourierServiceResponse activationCourierService(Client client){
        /*
        LocalDate birthday = client.getBirthday();
        if(birthday == null){
            throw new CustomException(1, "Для использования сервиса необходимо указать дату рождения");
        }
        */

        ActivationCourierServiceResponse response = new ActivationCourierServiceResponse();
        ActivationQueue activationQueue = activationQueueRepository.findByClient(client);
        if(activationQueue == null){
            activationQueue = new ActivationQueue();
            activationQueue.setClient(client);
            activationQueue.setTimeOfRequest(DateTimeUtils.nowNovosib_GMT6());
            activationQueueRepository.save(activationQueue);
        }
        if(activationQueue.getTimeOfActivation() == null){
            response.setCustomWindowInfo(operatorIsBusy(activationQueue));
        } else {
            throw new CustomException(1, "Активация пройдена");
        }
           return response;
    }

    /*
        if(activationQueue.getTimeOfActivation() != null){
            // оператор активировал данного клиента
            response.setCustomWindowInfo(ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.ACTIVATION_SERVICE_CONGRATULATION)));
        } else {
            List<ActivationQueue> activationQueues = activationQueueRepository.findByTimeOfActivationIsNullOrderByTimeOfRequestAsc();
            if(!CollectionUtils.isEmpty(activationQueues)){
                int clientNumberInQueue = activationQueues.indexOf(activationQueue) + 1;

                LOGGER.info("clientNumberInQueue: "+clientNumberInQueue);

                CustomWindow customWindow = customWindowRepository.findByTypeWindow(TypeWindow.OPERATOR_IS_BUSY);
                CustomWindowInfo customWindowInfo = ModelsUtils.toModel(customWindow);
                customWindowInfo.setContentText(String.format(customWindowInfo.getContentText(), clientNumberInQueue));
                response.setCustomWindowInfo(customWindowInfo);
            }
        }
        */





    public ItemHistoryResponse itemHistory(long clientId){
        ItemHistoryResponse response = new ItemHistoryResponse();
        List<OrderItemPrice> orderItemPrices = orderItemPriceRepository.findOrderItemPriceHistory(clientId);
        if(!CollectionUtils.isEmpty(orderItemPrices)){
            for(OrderItemPrice orderItemPrice :orderItemPrices){
                response.getItemInfos().add(ModelsUtils.toModel(orderItemPrice.getItemPrice().getItem()));
            }
        }
        return response;
    }



    public OrderItemPriceHistoryResponse orderItemPriceHistory(long clientId){
        OrderItemPriceHistoryResponse response = new OrderItemPriceHistoryResponse();
        List<OrderItemPrice> orderItemPrices = orderItemPriceRepository.findOrderItemPriceHistory(clientId);
        if(!CollectionUtils.isEmpty(orderItemPrices)){
            for(OrderItemPrice orderItemPrice :orderItemPrices){
                response.getOrderItemPriceInfos().add(ModelsUtils.toModel(orderItemPrice));
            }
        }
            return response;
    }





    /*
    private OrderPayment createOrderPaymentAndHold_test(Order order, String ipAddress, int priceAmount, int previousPriceAmount, OrderPayment orderPayment){
        if(orderPayment == null){
            orderPayment = commonService.createOrderPayment(OrderPayment.PaymentState.WAIT_TO_HOLD, order, null, priceAmount, null);
        }

        PayOnlineHelper payOnlineHelper = courierBillingService.createPreAuth(order, ipAddress, priceAmount);

        LOGGER.info("payOnlineHelper: " + payOnlineHelper.getMessage() + " " + payOnlineHelper.getStatus() + " " + payOnlineHelper.getCode());

        if(StringUtils.isEmpty(payOnlineHelper.getTransactionId())){
            throw new CustomException(1, "При попытке захолдировать сумму произошла ошибка: " + payOnlineHelper.getMessage());
        }
        OrderPayment orderPayment = commonService.createOrderPayment(OrderPayment.PaymentState.HOLD, order, payOnlineHelper, priceAmount, previousPriceAmount);
        orderPaymentRepository.save(orderPayment);
        return orderPayment;
    }
    */


    /*
    private OrderPayment createOrderPaymentAndHold(Order order, String ipAddress, int priceAmount, int previousPriceAmount){
        PayOnlineHelper payOnlineHelper = courierBillingService.createPreAuth(order, ipAddress, priceAmount);

        LOGGER.info("payOnlineHelper: " + payOnlineHelper.getMessage() + " " + payOnlineHelper.getStatus() + " " + payOnlineHelper.getCode());

        if(StringUtils.isEmpty(payOnlineHelper.getTransactionId())){
            throw new CustomException(1, "При попытке захолдировать сумму произошла ошибка: " + payOnlineHelper.getMessage());
        }
            OrderPayment orderPayment = commonService.createOrderPayment(OrderPayment.PaymentState.HOLD, order, payOnlineHelper, priceAmount, previousPriceAmount);
            orderPaymentRepository.save(orderPayment);
            return orderPayment;
    }
*/




//    private OrderPayment createOrderPayment(Order order, String ipAddress, int priceAmount, int previousPriceAmount){
//        OrderPayment orderPayment = commonService.createOrderPayment(OrderPayment.PaymentState.PRICE_DOWN, order, null, priceAmount, previousPriceAmount);
//        orderPaymentRepository.save(orderPayment);
//        return orderPayment;
//    }
//










    /*
            if(newPriceAmount > previousSum){
                // увеличение стоимости заказа
                createOrderPaymentAndHold(order, ipAddress, newPriceAmount , previousSum);

            } else if(newPriceAmount < previousSum){
                // уменьшение стоимости
                // createOrderPayment(order, ipAddress, priceAmount , previousSum);
            }
        } else {
            // создаем запись и холдируем сумму
            createOrderPaymentAndHold(order, ipAddress, order.getPriceExpected().getAmountMajorInt() , 0);
     */



    public OrderHistoryResponse orderHistory(boolean current, Client client){
        OrderHistoryResponse response = new OrderHistoryResponse();
        List<Order.State> states = new ArrayList<>();
        if(current){
            states.add(Order.State.NEW);
            states.add(Order.State.CONFIRMED);
            states.add(Order.State.WAIT_TO_CONFIRM);
            states.add(Order.State.TAKEN_BY_COURIER);
            states.add(Order.State.IN_PROGRESS_BY_OPERATOR);
            states.add(Order.State.IN_PROGRESS_BY_COURIER);
            states.add(Order.State.GO_TO_CLIENT);
            states.add(Order.State.CANCELED);
        } else {
            states.add(Order.State.COMPLETED);
        }
        List<Order> orderList = orderRepository.findByClientAndStateInAndOrderTypeNotOrderByTimeOfRequestingDesc(client, states, OrderType.OTHER, new org.springframework.data.domain.PageRequest(0,50)); // findByClientAndStateInAndOrderTypeNotOrderByTimeOfRequestingDesc
        if(!CollectionUtils.isEmpty(orderList)){
             for(Order order : orderList){
                 OrderInfo orderInfo = ModelsUtils.toModel(order, commonService.generalTimeLate(order.getId()));
                 orderInfo.setCurrentCourierLocation(commonService.getCurrentDriverLocation(order.getDriver()));
                 if(order.getDriver() != null){
                     DriverInfo driverInfo = administrationService.fillPhotoDriverAndCars(orderInfo.getDriverInfo(), order.getDriver(), true);
                     driverInfo.setPassword(null);
                     orderInfo.setDriverInfo(driverInfo);
                 }
                 response.getOrderInfos().add(orderInfo);
             }
        }
        return response;
    }



    public void startSearch(Order order){
        findDriversService.findCourier(order);
    }



    private Order getOrderInSearch(Client client){
        List<Order> currentOrder = orderRepository.findByClientAndStateAndIsBookedAndDriverIsNull(client, Order.State.CONFIRMED, Boolean.FALSE);
        Order orderInSearch = null;
        if(!CollectionUtils.isEmpty(currentOrder)){
            orderInSearch = currentOrder.get(0) ;
        }
            return orderInSearch;
    }









    public DateTime buildFinishTime(String timeOfFinishingStr){
        LOGGER.info("timeOfFinishingStr: " + timeOfFinishingStr);
        DateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTime timeOfFinish;
        try {
            Date date = form.parse(timeOfFinishingStr);
            timeOfFinish = DateTimeUtils.toDateTime(date.getTime());
        } catch (ParseException e) {
            throw new CustomException(2, "Неверный формат времени");
        }
            return timeOfFinish;
    }







    public CreateOrderResponse createOrder(OrderInfo orderInfo, Client client) {
        CreateOrderResponse response = new CreateOrderResponse();

        DateTime timeOfFinish = buildFinishTime(orderInfo.getTimeOfFinishing());

        boolean isBuyAndDeliver = OrderType.getByValue(orderInfo.getOrderType()).equals(OrderType.BUY_AND_DELIVER);
        boolean isTakeAndDeliver = OrderType.getByValue(orderInfo.getOrderType()).equals(OrderType.TAKE_AND_DELIVER);
        boolean isOther = OrderType.getByValue(orderInfo.getOrderType()).equals(OrderType.OTHER);

        /* проверка на время доступности работы сервиса */
        if(isBuyAndDeliver || isOther) {
            CustomWindowInfo serviceUnavailable = commonService.checkCourierServiceRestrictionTime(timeOfFinish);
            if (serviceUnavailable != null) {
                response.setCustomWindowInfo(serviceUnavailable);
                return response;
            }
        }

        /*
        CustomWindowInfo checkYearsOld = checkYearsOld(client);
        if(checkYearsOld != null){
            response.setCustomWindowInfo(checkYearsOld);
            return response;
        }
        */

        if(getOrderInSearch(client) != null){
            throw new CustomException(1, "У вас есть текущий заказ с поиском курьера");
        }

        ActivationQueue activationQueue = activationQueueRepository.findByClient(client);
        if(activationQueue == null || activationQueue.getTimeOfActivation() == null){
            response.setCustomWindowInfo(ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.REQUIRED_REQUEST)));
            return response;
        }

        if(commonService.checkAlcohol(orderInfo, timeOfFinish)){
            response.setCustomWindowInfo(ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.ALCOHOL_STOP)));
            return response;
        }

        Order order = new Order();
        order.setOrderType(OrderType.getByValue(orderInfo.getOrderType()));

        // todo: позже добавить карты и т.д.
        order.setPaymentType(PaymentType.CASH);
        order.setState(Order.State.NEW);
        order.setIsBooked(orderInfo.isBooked());
        order.setTimeOfFinishing(timeOfFinish);
        order.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6());
        order.setClient(client);
        orderRepository.save(order);

        response.setOrderId(order.getId());

        orderService.saveChangeState(order);

        // адреса откуда, куда привезти
        List<OrderAddressInfo> orderAddressInfos = orderInfo.getTargetAddressesInfo();
        List<String> targetAddress = new ArrayList<>();
        int orderNumber = 1;
        if (!CollectionUtils.isEmpty(orderAddressInfos)) {
            for (OrderAddressInfo info : orderAddressInfos) {
                OrderAddress orderAddress = ModelsUtils.fromModel(info, order, null, orderNumber);
                if(orderNumber != 1){
                    orderAddress.setTo(true);
                }
                orderAddressRepository.save(orderAddress);
                order.getTargetAddresses().add(orderAddress);
                targetAddress.add(info.getAddress());
                orderNumber++;
            }
        }
        orderNumber = 1;
        // список того, что натыкал пользователь (до того обработки заказа диспетчером)
        List<ClientItemInfo> clientItemInfos = orderInfo.getClientItemInfos();
        if (!CollectionUtils.isEmpty(clientItemInfos)) {

            for(ClientItemInfo clientItemInfo : clientItemInfos){
                Item itemSelected = itemRepository.findOne(clientItemInfo.getItemInfo().getId());
                ClientItem clientItem = ModelsUtils.fromModel(clientItemInfo, order, itemSelected, orderNumber);
                clientItemRepository.save(clientItem);
                order.getClientItems().add(clientItem);
                orderNumber++;
            }
        }

        if(orderInfo.getCommentInfo() != null) {
            Comment comment = ModelsUtils.fromModel(orderInfo.getCommentInfo(), order);
            commentRepository.save(comment);
        }

        clientRepository.save(client);

        int increasePercent = 0;
        int percentInsurance = 0;
        float percentOrderProcessing = 0.0f;

        if(isTakeAndDeliver){
            order.setState(Order.State.CONFIRMED);
            orderRepository.save(order);

            percentInsurance = Integer.parseInt(commonService.getPropertyValue("percent_insurance"));

            int distanceExpected = commonService.calculateDistanceWithTimeDurationGeneral(orderInfo).getDistance();
            order.setDistanceExpected(distanceExpected);

            CourierCalculatePriceResponse calculatePriceResponse = commonService.calculatePrice(orderInfo);

            order.setPriceOfInsurance(MoneyUtils.getMoney(calculatePriceResponse.getPriceOfInsurance() / 100));
            order.setPriceDelivery(MoneyUtils.getMoney(calculatePriceResponse.getPriceDelivery() / 100));
            order.setPriceExpected(MoneyUtils.getMoney(calculatePriceResponse.getCommonPrice() / 100));
            order.setPriceInFact(MoneyUtils.getMoney(calculatePriceResponse.getCommonPrice() / 100));
            order.setPriceOfItems(MoneyUtils.getMoney(calculatePriceResponse.getPriceItems() / 100));
            order.setPriceOfItemsExpected(MoneyUtils.getMoney(calculatePriceResponse.getPriceItems() / 100));
            order.setPriceOfPercentInsurance(MoneyUtils.getMoney(calculatePriceResponse.getPriceByPercentInsurance() / 100));
            order.setPriceOfAdditionalAddress(MoneyUtils.getMoney(calculatePriceResponse.getPriceByAdditionalAddress() / 100));

        } else if(isBuyAndDeliver){
            increasePercent = Integer.parseInt(commonService.getPropertyValue("increase_percent"));
            percentOrderProcessing = Float.parseFloat(commonService.getPropertyValue("percent_comission_order_processing"));
        }

        order.setPercentInsuranceOnDayOfOrder(percentInsurance);
        order.setIncreasePercent(increasePercent);
        order.setPercentOrderProcessing(percentOrderProcessing);

        order = orderRepository.save(order);

        if(!isTakeAndDeliver){
             nodeJsNotificationsService.courierNewOrderDispatcher(order);
        }
        return response;
    }




}
