package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ru.trendtech.domain.courier.Order;

import javax.persistence.*;

/**
 * Created by petr on 13.01.2015.
 */
@Entity
@Table(name = "client_cash_flow")
public class ClientCashFlow {
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
    @JoinColumn(name = "corporate_account_id")
    private Client corporateAccount;

    @Column(name = "operation", nullable = false)
    private int operation;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @OneToOne
    @JoinColumn(name = "c_order_id")
    private Order order;

    @Column(name = "sum", nullable = false)
    private int sum;

    @OneToOne
    @JoinColumn(name = "article_id")
    private ArticleAdjustments article;


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ArticleAdjustments getArticle() {
        return article;
    }

    public void setArticle(ArticleAdjustments article) {
        this.article = article;
    }

    public ClientCashFlow() {
    }

    public ClientCashFlow(Client client, Mission mission, int operation, int sum) {
        this.client = client;
        this.mission = mission;
        this.operation = operation;
        this.sum = sum;
    }

    public Client getCorporateAccount() {
        return corporateAccount;
    }

    public void setCorporateAccount(Client corporateAccount) {
        this.corporateAccount = corporateAccount;
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

    // id, date_time, client_id, operation, mission_id, sum
    /*
    operation:
0 - расчет за заказ (деньги нал)
1 - расчет за заказ (деньги карта)
2 - расчет за заказ (бонусы)
3 - приход бонусов (активация промо кода)
4 - корректировка
Ни одно поле не должно принимать значение NULL ни в каких случаях.
Если расчет осуществляется несколькими видами расчета, то и записей должно быть столько же. Например - нал + бонусы, безнал + бонусы.
Поле SUM всегда заполняется не суммой всего заказа, а именно суммой операции.
     */
}
