package ru.trendtech.domain;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import org.joda.time.DateTime;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by petr on 02.04.2015.
 */
@Entity
@Table(name = "driver_setting")
public class DriverSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "driver_id", unique = true, nullable = false)
    private Driver driver;

    @Column(name = "time_update")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfUpdate;

    @Column(name = "robot_active", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean robotActive;

    @Column(name = "default_radius")
    private int defaultRadius; // [1-15]

    @Column(name = "to_address")
    private String toAddress;

    @Column(name = "to_lat")
    private double toLat;

    @Column(name = "to_lon")
    private double toLon;

    /* включить цепочку заказов [предлагать водиле следующий заказ в радиусе] */
    @Column(name = "next_offer", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean nextOffer;

    @Column(name = "aeroport", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean aeroport;

    /* список доп услуг, которые может предоставить водитель [детское кресло, клетка для животных ...] */
    @Enumerated(value = EnumType.STRING)
    @ElementCollection(targetClass = MissionService.class)
    @CollectionTable(name = "driver_services", joinColumns = {@JoinColumn(name = "driver_setting_id"), })
    private List<MissionService> driverServices = new ArrayList<>();


    /* мясорубка и т.д. */
//    @OneToMany
//    @LazyCollection(LazyCollectionOption.FALSE)
//    private Set<AdditionalService> additionalServices = new HashSet<>();

//    @OneToMany
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @JoinColumn(name = "setting_id")
//    private Set<AdditionalService> additionalServices = new HashSet<>();

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "driver_additional_service",
    joinColumns = @JoinColumn(name = "driver_setting_id"),
    inverseJoinColumns = @JoinColumn(name = "addition_service_id"))
    private Set<AdditionalService> additionalServices = new HashSet<>();


    /* список доп услуг, которые может предоставить водитель [детское кресло, клетка для животных ...] */
    /*
    @ElementCollection
    @CollectionTable(name="driver_services") // use default join column name
    @AttributeOverrides({
            @AttributeOverride(name="driverId",
                    column=@Column(name="driver_id")),
            @AttributeOverride(name="missionService",
                    column=@Column(name="service"))
    })
    protected List<DriverServices> driverServices = new ArrayList<>();
    */


    /* выбор тарифа для водителя: если он выбирает только Комфорт и Бизнес, то он видит только эти заказы */
    @Enumerated(value = EnumType.STRING)
    @ElementCollection(targetClass = AutoClass.class)
    @CollectionTable(name = "driver_auto_class", joinColumns = {@JoinColumn(name = "driver_setting_id")}) // , insertable = true, updatable = true
    private List<AutoClass> driverAutoClass = new ArrayList<>();

    @Column(name = "sum_increase", nullable = false)
    private int sumIncrease = 0;

    @Column(name = "courier", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean courier;

    @Column(name = "card_number")
    private String cardNumber;

    @Columns(columns = {@Column(name = "deposite_amount_currency"), @Column(name = "deposite_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money depositeAmount = Money.zero(MoneyUtils.DEFAULT_CURRENCY); // // поле «Депозит» - сумма, на которую водитель готов принимать заказы, если необходимо что-то купить.

    @Column(name = "pedestrian", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean pedestrian;

    @Column(name = "driver_type")
    @Enumerated(value = EnumType.STRING)
    private DriverType driverType = DriverType.valueOf("NONE");

    public enum DriverType {
        NONE,
        DRIVER,
        COURIER,
        BOTH
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public void setDriverType(DriverType driverType) {
        this.driverType = driverType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Money getDepositeAmount() {
        return depositeAmount;
    }

    public void setDepositeAmount(Money depositeAmount) {
        this.depositeAmount = depositeAmount;
    }

    public boolean isPedestrian() {
        return pedestrian;
    }

    public void setPedestrian(boolean pedestrian) {
        this.pedestrian = pedestrian;
    }

    public boolean isCourier() {
        return courier;
    }

    public void setCourier(boolean courier) {
        this.courier = courier;
    }

    public int getSumIncrease() {
        return sumIncrease;
    }

    public void setSumIncrease(int sumIncrease) {
        this.sumIncrease = sumIncrease;
    }

    public Set<AdditionalService> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(Set<AdditionalService> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public DateTime getTimeOfUpdate() {
        return timeOfUpdate;
    }

    public void setTimeOfUpdate(DateTime timeOfUpdate) {
        this.timeOfUpdate = timeOfUpdate;
    }

    public boolean isRobotActive() {
        return robotActive;
    }

    public void setRobotActive(boolean robotActive) {
        this.robotActive = robotActive;
    }

    public int getDefaultRadius() {
        return defaultRadius;
    }

    public void setDefaultRadius(int defaultRadius) {
        this.defaultRadius = defaultRadius;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public double getToLat() {
        return toLat;
    }

    public void setToLat(double toLat) {
        this.toLat = toLat;
    }

    public double getToLon() {
        return toLon;
    }

    public void setToLon(double toLon) {
        this.toLon = toLon;
    }

    public boolean isNextOffer() {
        return nextOffer;
    }

    public void setNextOffer(boolean nextOffer) {
        this.nextOffer = nextOffer;
    }

    public boolean isAeroport() {
        return aeroport;
    }

    public void setAeroport(boolean aeroport) {
        this.aeroport = aeroport;
    }

    public List<MissionService> getDriverServices() {
        return driverServices;
    }

    public void setDriverServices(List<MissionService> driverServices) {
        this.driverServices = driverServices;
    }

    public List<AutoClass> getDriverAutoClass() {
        return driverAutoClass;
    }

    public void setDriverAutoClass(List<AutoClass> driverAutoClass) {
        this.driverAutoClass = driverAutoClass;
    }
}
