package ru.trendtech.domain.billing;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class AutoClassPrice {
    @Column(name = "auto_class")
    @Enumerated(value = EnumType.STRING)
    private AutoClass autoClass;

    @Columns(columns = {@Column(name = "per_km_currency"), @Column(name = "price")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmountAndCurrency")
    private Money price = Money.zero(MoneyUtils.DEFAULT_CURRENCY);

    @Column(name = "price_km")
    private int priceKm;

    @Column(name = "price_km_corp", columnDefinition = "int default 0")
    private int priceKmCorporate;

    @Column(name = "price_hour")
    private int priceHour;

    @Column(name = "km_included")
    private int kmIncluded;

    @Column(name = "free_wait_minutes")
    private int freeWaitMinutes;

    @Column(name = "per_minute_wait_amount")
    private int perMinuteWaitAmount;

    @Column(name = "per_hour_amount")
    private int perHourAmount;

    @Column(name = "intercity")
    private int intercity;

    @Column(name = "auto_example")
    private String autoExample;

    @Column(name = "active_pic_url")
    private String activePicUrl;

    @Column(name = "not_active_pic_url")
    private String notActivePicUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "active", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean active;

    public int getPriceKmCorporate() {
        return priceKmCorporate;
    }

    public void setPriceKmCorporate(int priceKmCorporate) {
        this.priceKmCorporate = priceKmCorporate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAutoExample() {
        return autoExample;
    }

    public void setAutoExample(String autoExample) {
        this.autoExample = autoExample;
    }

    public String getActivePicUrl() {
        return activePicUrl;
    }

    public void setActivePicUrl(String activePicUrl) {
        this.activePicUrl = activePicUrl;
    }

    public String getNotActivePicUrl() {
        return notActivePicUrl;
    }

    public void setNotActivePicUrl(String notActivePicUrl) {
        this.notActivePicUrl = notActivePicUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriceKm() {
        return priceKm;
    }

    public void setPriceKm(int priceKm) {
        this.priceKm = priceKm;
    }

    public int getPriceHour() {
        return priceHour;
    }

    public void setPriceHour(int priceHour) {
        this.priceHour = priceHour;
    }

    public int getIntercity() {
        return intercity;
    }

    public void setIntercity(int intercity) {
        this.intercity = intercity;
    }

    public AutoClass getAutoClass() {
        return autoClass;
    }

    public int getKmIncluded() {
        return kmIncluded;
    }

    public void setKmIncluded(int kmIncluded) {
        this.kmIncluded = kmIncluded;
    }

    public int getFreeWaitMinutes() {
        return freeWaitMinutes;
    }

    public void setFreeWaitMinutes(int freeWaitMinutes) {
        this.freeWaitMinutes = freeWaitMinutes;
    }

    public int getPerMinuteWaitAmount() {
        return perMinuteWaitAmount;
    }

    public void setPerMinuteWaitAmount(int perMinuteWaitAmount) {
        this.perMinuteWaitAmount = perMinuteWaitAmount;
    }

    public int getPerHourAmount() {
        return perHourAmount;
    }

    public void setPerHourAmount(int perHourAmount) {
        this.perHourAmount = perHourAmount;
    }

    public void setAutoClass(AutoClass autoClass) {
        this.autoClass = autoClass;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AutoClassPrice that = (AutoClassPrice) o;

        if (autoClass != that.autoClass) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return autoClass.hashCode();
    }
}
