package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 09.10.2014.
 */


@Entity
@Table(name = "sms_message")
public class SMSMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "error_message_smsc")
    private String errorMessageSMS_C;

    @Column(name = "error_message_sms_aero")
    private String errorMessageSMS_Aero;

    @Column(name = "sms_text")
    private String smsText;

    @Column(name = "count_try")
    private int countTry;

    @Column(name = "sms_aero__mess_id")
    private String smsAeroMessId;

    @Column(name = "time_create")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfCreate;

    @Column(name = "time_delivery")
    private long timeOfDelivery;

    @Column(name = "delivery_by")
    private String deliveryBy;


    public String getSmsAeroMessId() {
        return smsAeroMessId;
    }

    public void setSmsAeroMessId(String smsAeroMessId) {
        this.smsAeroMessId = smsAeroMessId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getErrorMessageSMS_C() {
        return errorMessageSMS_C;
    }

    public void setErrorMessageSMS_C(String errorMessageSMS_C) {
        this.errorMessageSMS_C = errorMessageSMS_C;
    }

    public String getErrorMessageSMS_Aero() {
        return errorMessageSMS_Aero;
    }

    public void setErrorMessageSMS_Aero(String errorMessageSMS_Aero) {
        this.errorMessageSMS_Aero = errorMessageSMS_Aero;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public int getCountTry() {
        return countTry;
    }

    public void setCountTry(int countTry) {
        this.countTry = countTry;
    }

    public DateTime getTimeOfCreate() {
        return timeOfCreate;
    }

    public void setTimeOfCreate(DateTime timeOfCreate) {
        this.timeOfCreate = timeOfCreate;
    }

    public long getTimeOfDelivery() {
        return timeOfDelivery;
    }

    public void setTimeOfDelivery(long timeOfDelivery) {
        this.timeOfDelivery = timeOfDelivery;
    }

    public String getDeliveryBy() {
        return deliveryBy;
    }

    public void setDeliveryBy(String deliveryBy) {
        this.deliveryBy = deliveryBy;
    }
}
