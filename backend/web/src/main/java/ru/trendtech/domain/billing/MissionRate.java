package ru.trendtech.domain.billing;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mission_rate")
public class MissionRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "free_waiting_time")
    private int freeWaitingTime;

    @Columns(columns = {@Column(name = "price_waiting_minute_currency"), @Column(name = "price_waiting_minute_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceWaitingMinute = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "mission_fee_currency"), @Column(name = "mission_fee_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money missionFee = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "price_minimal_currency"), @Column(name = "price_minimal_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceMinimal = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Columns(columns = {@Column(name = "price_stop_currency"), @Column(name = "price_stop_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money priceStop = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @ElementCollection
    @CollectionTable(name = "auto_class_prices", joinColumns = {@JoinColumn(name = "mission_rate_id")})
    private Set<AutoClassPrice> autoClassPrices = new HashSet<>();


//
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "services_prices", joinColumns = {@JoinColumn(name = "mission_rate_id")})
//    private Set<ServicePrice> servicesPrices = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFreeWaitingTime() {
        return freeWaitingTime;
    }

    public void setFreeWaitingTime(int freeWaitingTime) {
        this.freeWaitingTime = freeWaitingTime;
    }

    public Money getPriceWaitingMinute() {
        return priceWaitingMinute;
    }

    public void setPriceWaitingMinute(Money priceMinute) {
        this.priceWaitingMinute = priceMinute;
    }

    public Money getPriceMinimal() {
        return priceMinimal;
    }

    public void setPriceMinimal(Money priceMinimal) {
        this.priceMinimal = priceMinimal;
    }

    public Money getPriceStop() {
        return priceStop;
    }

    public void setPriceStop(Money priceStop) {
        this.priceStop = priceStop;
    }

    public Money getMissionFee() {
        return missionFee;
    }

    public void setMissionFee(Money missionFee) {
        this.missionFee = missionFee;
    }


    //  f: remove
    public Set<AutoClassPrice> getAutoClassPrices() {
        return autoClassPrices;
    }

    public void setAutoClassPrices(Set<AutoClassPrice> autoClassPrices) {
       this.autoClassPrices = autoClassPrices;
    }




//    public Set<ServicePrice> getServicesPrices() {
//        return servicesPrices;
//    }
//
//    public void setServicesPrices(Set<ServicePrice> servicesPrices) {
//        this.servicesPrices = servicesPrices;
//    }




//f: remove
    public Money getPriceForAuto(AutoClass autoClass) {
        Money result = null;
        for (AutoClassPrice autoClassRate : autoClassPrices) {
            if (autoClass.equals(autoClassRate.getAutoClass())) {
                result = autoClassRate.getPrice();
                break;
            }
        }
        return result;
    }



//    public Money getPriceForService(MissionService service) {
//        Money result = null;
//        for (ServicePrice servicesRate : servicesPrices) {
//            if (service.equals(servicesRate.getService())) {
//                result = servicesRate.getPrice();
//                break;
//            }
//        }
//        return result;
//    }

}
