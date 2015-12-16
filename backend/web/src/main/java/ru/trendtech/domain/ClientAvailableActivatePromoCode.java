
package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 12.10.2014.
 */

// СКОЛЬКО И КОМУ ДОЗВОЛЕНО АКТИВИРОВАТЬ ПРОМОКОДЫ
@Entity
@Table(name = "client_available_activate_promo_code")
public class ClientAvailableActivatePromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "available_activate_count", nullable = false)
    private int availableActivateCount=1;

    @Column(name = "client_id", unique=true, nullable = false)
    private long clientId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAvailableActivateCount() {
        return availableActivateCount;
    }

    public void setAvailableActivateCount(int availableActivateCount) {
        this.availableActivateCount = availableActivateCount;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}