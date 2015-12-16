package ru.trendtech.domain.courier;

import com.google.common.base.Objects;
import org.hibernate.annotations.*;
import org.joda.money.Money;
import org.joda.time.DateTime;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.utils.MoneyUtils;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by petr on 11.08.2015.
 */

@Entity
@Table(name = "c_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @Column(name = "time_starting", nullable = false)
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    private DateTime timeOfStarting;

    @Column(name = "time_requesting")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfRequesting;

    @Column(name = "time_starting_order_processing")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfStartingOrderProcessing;

    @Column(name = "time_assigning")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfAssigning;

    @Column(name = "time_finishing", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfFinishing; // время завершения заказа (задается пользователем)

    @Column(name = "time_finishing_expected")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfFinishingExpected; // ожидаемое время завершения

    @Column(name = "time_finishing_in_fact")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfFinishingInFact; // время завершения заказа по факту

    @Column(name = "time_ready_to_progress")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfReadyToProgress; // время когда водитель нажал на кнопку "поехал по магазинам"

    @Column(name = "time_ready_to_go")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfReadyToGo; // время когда водитель собрал весь товар, или нечто откуда нужно привезти, и готов ехать к клиенту

    @Column(name = "time_of_canceling")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfCanceling;

//    @Column(name = "expected_time_completing")
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    private DateTime expectedTimeOfCompleting;

    @Column(name = "order_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderType orderType;


    @Column(name = "payment_type")
    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType = PaymentType.UNKNOWN;


    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State state;


    @Column(name = "previous_state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State previousState = State.valueOf("NONE");

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne
    @JoinColumn(name = "web_user_id")
    private WebUser webUser;

    @Columns(columns = {@Column(name = "price_expected_currency"), @Column(name = "price_expected")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceExpected = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "price_in_fact_currency"), @Column(name = "price_in_fact")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceInFact = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    /* стоимость доставки: минималка+км+обработка */
    @Columns(columns = {@Column(name = "price_delivery_currency"), @Column(name = "price_delivery")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceDelivery = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    // оценочная стоимость(страховка товара)
    @Columns(columns = {@Column(name = "price_insurance_currency"), @Column(name = "price_insurance")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceOfInsurance = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    // сумма % страховки от оценочной стоимости товара
    @Columns(columns = {@Column(name = "price_percent_currency"), @Column(name = "price_percent_insurance")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceOfPercentInsurance = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    // стоимость товара - по факту
    @Columns(columns = {@Column(name = "price_items_currency"), @Column(name = "price_items")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceOfItems = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    // стоимость товара - ожидаемая
    @Columns(columns = {@Column(name = "price_items_expected_currency"), @Column(name = "price_items_expected")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceOfItemsExpected = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "additional_address_currency"), @Column(name = "price_additional_address")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceOfAdditionalAddress = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    /* % страховки на день формирования заказа (1%)*/
    @Column(name = "percent_insurance_on_day_of_order", nullable = false, columnDefinition = "int default 0")
    private int percentInsuranceOnDayOfOrder;

    @Column(name = "distance_expected_to_store", nullable = false, columnDefinition = "int default 0")
    private int distanceExpectedToStore; // ожидаемая длина дистанции по магазинам

    @Column(name = "distance_in_fact_to_store", nullable = false, columnDefinition = "int default 0")
    private int distanceInFactToStore; // фактическая длина дистанции по магазинам

    @Column(name = "distance_expected", nullable = false, columnDefinition = "int default 0")
    private int distanceExpected; // ожидаемая общая длинна дистанции (по магазинам + к клиенту)

    @Column(name = "distance_in_fact", nullable = false, columnDefinition = "int default 0")
    private int distanceInFact; // фактическая общая длинна дистанции (по магазинам + к клиенту)


    /*
    адреса магазинов будут лежать в OrderItemPrice
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "curier_order_store_address",
    joinColumns = @JoinColumn(name = "order_id"),
    inverseJoinColumns = @JoinColumn(name = "store_address_id"))
    private Set<StoreAddress> storeAddresses = new HashSet<>();
    */

    /*
        @OneToMany
        @LazyCollection(LazyCollectionOption.FALSE)
        @JoinColumn(name = "store_id", insertable = true, updatable = true)
        private Set<StoreAddress> storeAddresses = new HashSet<>();
    */


    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "c_order_id", insertable = true, updatable = true)
    @OrderBy("orderNumber ASC")
    private Set<OrderAddress> targetAddresses = new HashSet<>();


    /*
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "curier_order_address",
    joinColumns = @JoinColumn(name = "order_id"),
    inverseJoinColumns = @JoinColumn(name = "order_address_id"))
    private Set<OrderAddress> orderAddresses = new HashSet<>();
    */


    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "c_order_id", insertable = true, updatable = true)
    @OrderBy("orderNumber ASC")
    private Set<OrderItemPrice> orderItemPrices = new HashSet<>(); // список товаров и услуг


    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "c_order_id", insertable = true, updatable = true)
    @OrderBy("orderNumber ASC")
    private Set<ClientItem> clientItems = new HashSet<>(); // то что натыкал пользователь (не подтверждено диспетчером)


    /* % от суммы заказа на день формирования, результат прибавляем к общей стоимости за товар - ТОЛЬКО ЕСЛИ ЗАКАЗ КУПИТЬ И ПРИВЕЗТИ */
    @Column(name = "increase_percent", nullable = false, columnDefinition = "int default 0")
    private int increasePercent;

    /* % от стоимости покупки (наши затраты за обслуживание банком) на день формирования, результат прибавляем к общей стоимости за товар - ТОЛЬКО ЕСЛИ ЗАКАЗ КУПИТЬ И ПРИВЕЗТИ */
    @Column(name = "percent_order_processing", columnDefinition="Decimal(10,2) default '0.0'", nullable = false)
    private double percentOrderProcessing;

    @Column(name = "is_booked", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean isBooked;

    @Column(name = "expected_arrival_time", nullable = false, columnDefinition = "int default 0")
    private int expectedArrivalTime; // расчетное время прибытия на заказ

    @Column(name = "reason_cancel")
    private String reasonCancel;


    public Money getPriceOfAdditionalAddress() {
        return priceOfAdditionalAddress;
    }

    public void setPriceOfAdditionalAddress(Money priceOfAdditionalAddress) {
        this.priceOfAdditionalAddress = priceOfAdditionalAddress;
    }

    public Money getPriceOfItemsExpected() {
        return priceOfItemsExpected;
    }

    public void setPriceOfItemsExpected(Money priceOfItemsExpected) {
        this.priceOfItemsExpected = priceOfItemsExpected;
    }

    public double getPercentOrderProcessing() {
        return percentOrderProcessing;
    }

    public void setPercentOrderProcessing(double percentOrderProcessing) {
        this.percentOrderProcessing = percentOrderProcessing;
    }

    public DateTime getTimeOfCanceling() {
        return timeOfCanceling;
    }

    public void setTimeOfCanceling(DateTime timeOfCanceling) {
        this.timeOfCanceling = timeOfCanceling;
    }

    public DateTime getTimeOfFinishing() {
        return timeOfFinishing;
    }

    public void setTimeOfFinishing(DateTime timeOfFinishing) {
        this.timeOfFinishing = timeOfFinishing;
    }

    public Money getPriceOfPercentInsurance() {
        return priceOfPercentInsurance;
    }

    public void setPriceOfPercentInsurance(Money priceOfPercentInsurance) {
        this.priceOfPercentInsurance = priceOfPercentInsurance;
    }

    public State getPreviousState() {
        return previousState;
    }

    public void setPreviousState(State previousState) {
        this.previousState = previousState;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getReasonCancel() {
        return reasonCancel;
    }

    public void setReasonCancel(String reasonCancel) {
        this.reasonCancel = reasonCancel;
    }

    public int getExpectedArrivalTime() {
        return expectedArrivalTime;
    }

    public void setExpectedArrivalTime(int expectedArrivalTime) {
        this.expectedArrivalTime = expectedArrivalTime;
    }

    public DateTime getTimeOfReadyToProgress() {
        return timeOfReadyToProgress;
    }

    public void setTimeOfReadyToProgress(DateTime timeOfReadyToProgress) {
        this.timeOfReadyToProgress = timeOfReadyToProgress;
    }

    public Money getPriceOfItems() {
        return priceOfItems;
    }

    public void setPriceOfItems(Money priceOfItems) {
        this.priceOfItems = priceOfItems;
    }

    public int getPercentInsuranceOnDayOfOrder() {
        return percentInsuranceOnDayOfOrder;
    }

    public void setPercentInsuranceOnDayOfOrder(int percentInsuranceOnDayOfOrder) {
        this.percentInsuranceOnDayOfOrder = percentInsuranceOnDayOfOrder;
    }

    public Money getPriceOfInsurance() {
        return priceOfInsurance;
    }

    public void setPriceOfInsurance(Money priceOfInsurance) {
        this.priceOfInsurance = priceOfInsurance;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public int getIncreasePercent() {
        return increasePercent;
    }

    public void setIncreasePercent(int increasePercent) {
        this.increasePercent = increasePercent;
    }

    public Money getPriceDelivery() {
        return priceDelivery;
    }

    public void setPriceDelivery(Money priceDelivery) {
        this.priceDelivery = priceDelivery;
    }

    public DateTime getTimeOfStartingOrderProcessing() {
        return timeOfStartingOrderProcessing;
    }

    public void setTimeOfStartingOrderProcessing(DateTime timeOfStartingOrderProcessing) {
        this.timeOfStartingOrderProcessing = timeOfStartingOrderProcessing;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

    public Set<ClientItem> getClientItems() {
        return clientItems;
    }

    public void setClientItems(Set<ClientItem> clientItems) {
        this.clientItems = clientItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getTimeOfRequesting() {
        return timeOfRequesting;
    }

    public void setTimeOfRequesting(DateTime timeOfRequesting) {
        this.timeOfRequesting = timeOfRequesting;
    }

    public DateTime getTimeOfAssigning() {
        return timeOfAssigning;
    }

    public void setTimeOfAssigning(DateTime timeOfAssigning) {
        this.timeOfAssigning = timeOfAssigning;
    }

    public DateTime getTimeOfFinishingExpected() {
        return timeOfFinishingExpected;
    }

    public void setTimeOfFinishingExpected(DateTime timeOfFinishingExpected) {
        this.timeOfFinishingExpected = timeOfFinishingExpected;
    }

    public DateTime getTimeOfFinishingInFact() {
        return timeOfFinishingInFact;
    }

    public void setTimeOfFinishingInFact(DateTime timeOfFinishingInFact) {
        this.timeOfFinishingInFact = timeOfFinishingInFact;
    }

    public DateTime getTimeOfReadyToGo() {
        return timeOfReadyToGo;
    }

    public void setTimeOfReadyToGo(DateTime timeOfReadyToGo) {
        this.timeOfReadyToGo = timeOfReadyToGo;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Money getPriceExpected() {
        return priceExpected;
    }

    public void setPriceExpected(Money priceExpected) {
        this.priceExpected = priceExpected;
    }

    public Money getPriceInFact() {
        return priceInFact;
    }

    public void setPriceInFact(Money priceInFact) {
        this.priceInFact = priceInFact;
    }

    public int getDistanceExpectedToStore() {
        return distanceExpectedToStore;
    }

    public void setDistanceExpectedToStore(int distanceExpectedToStore) {
        this.distanceExpectedToStore = distanceExpectedToStore;
    }

    public int getDistanceInFactToStore() {
        return distanceInFactToStore;
    }

    public void setDistanceInFactToStore(int distanceInFactToStore) {
        this.distanceInFactToStore = distanceInFactToStore;
    }

    public int getDistanceExpected() {
        return distanceExpected;
    }

    public void setDistanceExpected(int distanceExpected) {
        this.distanceExpected = distanceExpected;
    }

    public int getDistanceInFact() {
        return distanceInFact;
    }

    public void setDistanceInFact(int distanceInFact) {
        this.distanceInFact = distanceInFact;
    }

    public Set<OrderAddress> getTargetAddresses() {
        return targetAddresses;
    }

    public void setTargetAddresses(Set<OrderAddress> targetAddresses) {
        this.targetAddresses = targetAddresses;
    }

    public Set<OrderItemPrice> getOrderItemPrices() {
        return orderItemPrices;
    }

    public void setOrderItemPrices(Set<OrderItemPrice> orderItemPrices) {
        this.orderItemPrices = orderItemPrices;
    }

    public enum State {
        NONE,
        NEW, // новый заказ
        IN_PROGRESS_BY_OPERATOR, // взят оператором для выполнения
        WAIT_TO_CONFIRM, // оператор обработал заказ и отправил клиенту на подтверждение (или изменилась стоимость заказа и мы требуем повторного подтверждения)
        CONFIRMED, // подтвержден
        TAKEN_BY_COURIER, // взят курьером
        IN_PROGRESS_BY_COURIER, // едет по магазинам или забирать
        GO_TO_CLIENT, // купил(забрал) и еду к клиенту
        WAIT_TO_PAYMENT,
        COMPLETED,
        CANCELED,
        ;
    }


    /*
       orders (id, type, price_delivery (expected, real))
    */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equal(getId(), order.getId()) &&
                Objects.equal(getTimeOfRequesting(), order.getTimeOfRequesting()) &&
                Objects.equal(getClient(), order.getClient());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getTimeOfRequesting(), getClient());
    }
}