package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 07.04.2015.
 */
@Entity
@Table(name = "corporate_client_cash_flow")
public class CorporateClientCashFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_operation", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateOperation;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne
    @JoinColumn(name = "main_client_id")
    private Client mainClient;

    @Column(name = "operation", nullable = false)
    private int operation;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @OneToOne
    @JoinColumn(name = "article_id")
    private ArticleAdjustments article;

    @Column(name = "sum", nullable = false)
    private int sum;

    @Column(name = "comment")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArticleAdjustments getArticle() {
        return article;
    }

    public void setArticle(ArticleAdjustments article) {
        this.article = article;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(DateTime dateOperation) {
        this.dateOperation = dateOperation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getMainClient() {
        return mainClient;
    }

    public void setMainClient(Client mainClient) {
        this.mainClient = mainClient;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
