package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 12.10.2014.
 */


@Entity
@Table(name = "client_sum_promo_code")
public class ClientSumPromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "available_amount", nullable = false, unique = true)
    private int availableAmount;

    @Column(name = "client_id")
    private long clientId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
