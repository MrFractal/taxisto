package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ru.trendtech.domain.admin.WebUser;

import javax.persistence.*;

/**
 * Created by petr on 16.01.2015.
 */
@Entity
@Table(name = "client_corrections")
public class ClientCorrections {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "cash_flow_id")
    private ClientCashFlow clientCashFlow;

    @OneToOne
    @JoinColumn(name = "user_id")
    private WebUser webUser;

    @Column(name = "comments")
    private String comments;

    @Column(name = "bonuses_before")
    private int bonusesBefore;

    @Column(name = "bonuses_after")
    private int bonusesAfter;

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientCashFlow getClientCashFlow() {
        return clientCashFlow;
    }

    public void setClientCashFlow(ClientCashFlow clientCashFlow) {
        this.clientCashFlow = clientCashFlow;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getBonusesBefore() {
        return bonusesBefore;
    }

    public void setBonusesBefore(int bonusesBefore) {
        this.bonusesBefore = bonusesBefore;
    }

    public int getBonusesAfter() {
        return bonusesAfter;
    }

    public void setBonusesAfter(int bonusesAfter) {
        this.bonusesAfter = bonusesAfter;
    }
}
