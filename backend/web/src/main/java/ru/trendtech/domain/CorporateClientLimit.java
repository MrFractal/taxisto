package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 05.03.2015.
 */
@Entity
@Table(name = "corporate_client_sum_limit")
public class CorporateClientLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

//    @OneToOne
//    @JoinColumn(name = "main_client_id", nullable = false)
//    private Client mainClient;

    @Column(name = "limit_amount")
    private int limitAmount;

    @Column(name = "update_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime updateTime;

    @Column(name = "period")
    @Enumerated(value = EnumType.STRING)
    private Period period;

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public enum Period {
        UNKNOWN(0),
        MONTH(1),
        WEEK(2);

        private final int value;

        private Period(int value) {
            this.value = value;
        }

        public static Period getByValue(int value) {
            Period result = UNKNOWN;
            for (Period item : values()) {
                if (item.getValue() == value) {
                    result = item;
                    break;
                }
            }
            return result;
        }

        public int getValue() {
            return value;
        }
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
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

//    public Client getMainClient() {
//        return mainClient;
//    }
//
//    public void setMainClient(Client mainClient) {
//        this.mainClient = mainClient;
//    }

    public DateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }
}
