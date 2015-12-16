package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 10.03.2015.
 */

@Entity
@Table(name = "promo_code_exclusive")
public class PromoCodeExclusive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "promo_code", unique = true, nullable = false)
    private String promoCode;

    @Column(name = "used_count", nullable = false)
    private int usedCount=0; // сколько раз использования

    @Column(name = "available_used_count" , nullable = false)
    private int availableUsedCount=0; // сколько раз можно использовать

    @Column(name = "date_issue")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateOfIssue;

    /*
       количество дней жизни акционног предложения с момента его активации
       т.е.: пользователь активирует промокод, включается акция (например, тариф бонус), которая будет
       активна в течении lifetimeDaysAfterActivation после активации данного промокода
    */
    @Column(name = "lifetime_days_after_activation", nullable = false, columnDefinition = "int default 0")
    private int lifetimeDaysAfterActivation;

    public int getLifetimeDaysAfterActivation() {
        return lifetimeDaysAfterActivation;
    }

    public void setLifetimeDaysAfterActivation(int lifetimeDaysAfterActivation) {
        this.lifetimeDaysAfterActivation = lifetimeDaysAfterActivation;
    }

    public DateTime getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(DateTime dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public int getAvailableUsedCount() {
        return availableUsedCount;
    }

    public void setAvailableUsedCount(int availableUsedCount) {
        this.availableUsedCount = availableUsedCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }
}
