package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ru.trendtech.domain.admin.WebUser;

import javax.persistence.*;

/**
 * Created by petr on 16.01.2015.
 */

@Entity
@Table(name = "driver_corrections")
public class DriverCorrections {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "cash_flow_id")
    private DriverCashFlow driverCashFlow;

    @OneToOne
    @JoinColumn(name = "user_id")
    private WebUser webUser;

    @Column(name = "comments")
    private String comments;

    @Column(name = "balance_before")
    private int balanceBefore;

    @Column(name = "balance_after")
    private int balanceAfter;

    @OneToOne
    private ArticleAdjustments articleAdjustments;


    public ArticleAdjustments getArticleAdjustments() {
        return articleAdjustments;
    }

    public void setArticleAdjustments(ArticleAdjustments articleAdjustments) {
        this.articleAdjustments = articleAdjustments;
    }

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

    public DriverCashFlow getDriverCashFlow() {
        return driverCashFlow;
    }

    public void setDriverCashFlow(DriverCashFlow driverCashFlow) {
        this.driverCashFlow = driverCashFlow;
    }

    public int getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(int balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public int getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(int balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
