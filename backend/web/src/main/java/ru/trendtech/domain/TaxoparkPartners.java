package ru.trendtech.domain;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ru.trendtech.utils.MoneyUtils;

import javax.persistence.*;

/**
 * Created by petr on 13.10.2014.
 */
@Entity
@Table(name = "taxopark_partners")
public class TaxoparkPartners {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name_taxopark", nullable = false)
    private String nameTaxopark;

    @Column(name = "office_address")
    private String officeAddress;

    @Column(name = "office_phone")
    private String officePhone;

    @Column(name = "responsibility_fio")
    private String responsibilityFio;

    @Column(name = "responsibility_phone")
    private String responsibilityPhone;

    @Column(name = "priority", nullable = false)
    private int priority=0;

    @Columns(columns = {@Column(name = "money_currency"), @Column(name = "money_amount")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmountAndCurrency")
    private Money moneyAmount;

    @Column(name = "increase_to_driver", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean increaseToDriver;

    public boolean isIncreaseToDriver() {
        return increaseToDriver;
    }

    public void setIncreaseToDriver(boolean increaseToDriver) {
        this.increaseToDriver = increaseToDriver;
    }

    public Money getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Money moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameTaxopark() {
        return nameTaxopark;
    }

    public void setNameTaxopark(String nameTaxopark) {
        this.nameTaxopark = nameTaxopark;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getResponsibilityFio() {
        return responsibilityFio;
    }

    public void setResponsibilityFio(String responsibilityFio) {
        this.responsibilityFio = responsibilityFio;
    }

    public String getResponsibilityPhone() {
        return responsibilityPhone;
    }

    public void setResponsibilityPhone(String responsibilityPhone) {
        this.responsibilityPhone = responsibilityPhone;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
