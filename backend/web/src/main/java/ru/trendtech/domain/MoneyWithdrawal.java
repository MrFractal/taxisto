package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 07.10.2014.
 */

@Entity
@Table(name = "money_withdrawal")
public class MoneyWithdrawal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sms_code")
    private String smsCode;

    @OneToOne
    @JoinColumn(name = "driver_id", unique = true)
    private Driver driver;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
