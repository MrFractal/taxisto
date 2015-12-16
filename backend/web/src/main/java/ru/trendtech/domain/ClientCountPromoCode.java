package ru.trendtech.domain;

/**
 * Created by petr on 12.10.2014.
 */

import javax.persistence.*;
// СКОЛЬКО И КОМУ ДОЗВОЛЕНО РАССЫЛАТЬ ПРОМОКОДЫ
@Entity
@Table(name = "client_count_promo_code")
public class ClientCountPromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "available_count", nullable = false)
    private int availableCount=0;

    @Column(name = "client_id", unique=true, nullable = false)
    private long clientId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}