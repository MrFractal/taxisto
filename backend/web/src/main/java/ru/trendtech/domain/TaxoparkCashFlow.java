package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 20.07.2015.
 */
@Entity
@Table(name = "taxopark_cash_flow")
public class TaxoparkCashFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @OneToOne
    @JoinColumn(name = "taxopark_id")
    private TaxoparkPartners taxopark;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Column(name = "operation", nullable = false)
    private int operation;

    @Column(name = "date_operation")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateOperation;

    @Column(name = "sum", nullable = false)
    private int sum;

    @OneToOne
    @JoinColumn(name = "article_id")
    private ArticleAdjustments article;

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

    public TaxoparkPartners getTaxopark() {
        return taxopark;
    }

    public void setTaxopark(TaxoparkPartners taxopark) {
        this.taxopark = taxopark;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public DateTime getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(DateTime dateOperation) {
        this.dateOperation = dateOperation;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
