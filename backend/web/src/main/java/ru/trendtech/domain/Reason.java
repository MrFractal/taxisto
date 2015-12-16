package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 12.05.2015.
 */
@Entity
@Table(name = "reason")
public class Reason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT default ``")
    private String reason;

    @Column(name = "fine", nullable = false, columnDefinition = "int default 0")
    private int fine = 0;

    @Column(name = "client_bonus", nullable = false, columnDefinition = "int default 0")
    private int clientBonus = 0;

    @Column(name = "to_driver" , nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean toDriver;


    public boolean isToDriver() {
        return toDriver;
    }

    public void setToDriver(boolean toDriver) {
        this.toDriver = toDriver;
    }

    public int getClientBonus() {
        return clientBonus;
    }

    public void setClientBonus(int clientBonus) {
        this.clientBonus = clientBonus;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}
