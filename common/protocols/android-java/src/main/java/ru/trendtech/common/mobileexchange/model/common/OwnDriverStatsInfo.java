package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 09.02.2015.
 */
public class OwnDriverStatsInfo {
    /*
    Результат: логин, водитель (ФИО), отработано часов/смен, выполненные заказы (кол-во), выполненные заказы (сумма), бесплатный отдых, платный отдых, ЗП.
    */
    private long driverId;
    private String login;
    private String name;
    private int countCompletedMission;
    private int sumCompletedMission;
    private String freeRest;
    private String payRest;
    private String countWorkflow;
    private int salary;
    private long timeStartWork;

    public long getTimeStartWork() {
        return timeStartWork;
    }

    public void setTimeStartWork(long timeStartWork) {
        this.timeStartWork = timeStartWork;
    }

    public String getFreeRest() {
        return freeRest;
    }

    public void setFreeRest(String freeRest) {
        this.freeRest = freeRest;
    }

    public String getPayRest() {
        return payRest;
    }

    public void setPayRest(String payRest) {
        this.payRest = payRest;
    }

    public String getCountWorkflow() {
        return countWorkflow;
    }

    public void setCountWorkflow(String countWorkflow) {
        this.countWorkflow = countWorkflow;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getCountCompletedMission() {
        return countCompletedMission;
    }

    public void setCountCompletedMission(int countCompletedMission) {
        this.countCompletedMission = countCompletedMission;
    }

    public int getSumCompletedMission() {
        return sumCompletedMission;
    }

    public void setSumCompletedMission(int sumCompletedMission) {
        this.sumCompletedMission = sumCompletedMission;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
