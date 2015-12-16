package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 12.10.2014.
 */

// СПИСОК ПРОМОКОДОВ, КОТОРЫМИ ВОСПОЛЬЗОВАЛСЯ КЛИЕНТ
@Entity
@Table(name = "client_activated_promo_codes")
public class ClientActivatedPromoCodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "client_id", nullable = false)
    private long clientId;

    @Column(name = "promo_code_id", nullable = false)
    private long promoCodeId;

    @Column(name = "date_of_used")
    private Long dateOfUsed;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(long promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public Long getDateOfUsed() {
        return dateOfUsed;
    }

    public void setDateOfUsed(Long dateOfUsed) {
        this.dateOfUsed = dateOfUsed;
    }
}
