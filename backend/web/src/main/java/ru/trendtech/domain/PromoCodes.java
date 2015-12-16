package ru.trendtech.domain;

import javax.persistence.*;

@Entity
@Table(name = "promo_codes")
public class PromoCodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "promo_code", unique = true)
    private String promoCode;

    @Column(name = "amount")
    private int amount;

    @Column(name = "from_id")
    private Long fromId;

    @Column(name = "to_id")
    private Long toId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "expiration_date")
    private Long expirationDate;

    @Column(name = "channel", length = 32)
    private String channel;

//    @Column(name = "used", columnDefinition = "boolean default false", nullable = false)
//    private boolean used=false;

    @Column(name = "date_issue")
    private Long dateOfIssue; // дата выдачи

    /* пока убираю, т.к. это не одна дата, одна будет только в случае с whatsApp*/
//    @Column(name = "date_use")
//    private Long dateOfUse;

    @Column(name = "used_count")
    private int usedCount=0; // сколько раз использования

    @Column(name = "available_used_count")
    private int availableUsedCount=10; // сколько раз можно использовать


    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public int getAvailableUsedCount() {
        return availableUsedCount;
    }

    public void setAvailableUsedCount(int availableUsedCount) {
        this.availableUsedCount = availableUsedCount;
    }

    public Long getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Long dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}
