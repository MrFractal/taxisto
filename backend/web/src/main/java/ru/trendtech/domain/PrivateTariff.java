package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 02.03.2015.
 */
@Entity
@Table(name = "private_tariff")
public class PrivateTariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    private Client client;

    @Column(name = "active", columnDefinition = "BIT DEFAULT 0", length = 1)
    private Boolean active;

    @Column(name = "tariff_name")
    private String tariffName;

    @Column(name = "is_activated", nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean isActivated;

    @OneToOne
    private PromoCodeExclusive promoExclusive;

    @Column(name = "activation_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime activationDate;

    @Column(name = "expiration_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime expirationDate;

    @Column(name = "free_wait_minutes", nullable = false, columnDefinition = "int default 0")
    private int freeWaitMinutes;

    public int getFreeWaitMinutes() {
        return freeWaitMinutes;
    }

    public void setFreeWaitMinutes(int freeWaitMinutes) {
        this.freeWaitMinutes = freeWaitMinutes;
    }

    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public DateTime getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(DateTime activationDate) {
        this.activationDate = activationDate;
    }

    public PromoCodeExclusive getPromoExclusive() {
        return promoExclusive;
    }

    public void setPromoExclusive(PromoCodeExclusive promoExclusive) {
        this.promoExclusive = promoExclusive;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
