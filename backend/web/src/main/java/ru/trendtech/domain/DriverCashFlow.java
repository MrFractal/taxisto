package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ru.trendtech.domain.courier.Order;

import javax.persistence.*;


@Entity
@Table(name = "driver_cash_flow")
public class DriverCashFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @OneToOne
    @JoinColumn(name = "c_order_id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "period_work_id")
    private DriverPeriodWork driverPeriodWork;

    @Column(name = "operation", nullable = false)
    //@Enumerated(value = EnumType.STRING)
    private int operation;

    @Column(name = "date_operation")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime date_operation;

    @Column(name = "sum")
    private int sum;


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public DriverPeriodWork getDriverPeriodWork() {
        return driverPeriodWork;
    }

    public void setDriverPeriodWork(DriverPeriodWork driverPeriodWork) {
        this.driverPeriodWork = driverPeriodWork;
    }

    public DateTime getDate_operation() {
        return date_operation;
    }

    public void setDate_operation(DateTime date_operation) {
        this.date_operation = date_operation;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public DriverCashFlow(){

    }

    public DriverCashFlow(Driver driver, Mission mission, int operation, int sum, DriverPeriodWork driverPeriodWork) {

        this.driver = driver;
        this.mission = mission;
        this.operation = operation;
        this.date_operation = date_operation;
        this.sum = sum;
        this.driverPeriodWork = driverPeriodWork;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public DateTime getTimeOfStarting() {
        return date_operation;
    }

    public void setTimeOfStarting(DateTime timeOfStarting) {
        this.date_operation = timeOfStarting;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
