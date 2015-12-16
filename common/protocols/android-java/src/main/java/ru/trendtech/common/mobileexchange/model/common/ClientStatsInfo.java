package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 10.10.2014.
 */
public class ClientStatsInfo {
    /*
1. Общее кол-во заказов
2. Кол-во отказов от заказа
3. Среднее кол-во заказов в месяц
4. Среднее кол-во заказов в месяц
5. Кол-во заказов за последний месяц
6. Общая сумма денег за заказы (только завершенные)
7. Средняя сумма денег в месяц (только завершенные)
8. Сумма денег за последний месяц (только завершенные)
9. Средний чек (только завершенные)
     */
    private int countMission; // общее кол-во заказов
    private int countCanceledMission;
    private double averageCountMissionMonth; // среднее кол-во заказов в месяц (общее кол-во/кол-во месяцев count(distinct date_format(time_requesting,'%Y-%m')))
    private int countMissionCurrentMonth; // кол-во заказов за текущий месяц (count(*)  x<time_requesting<y)
    private double summMissionMoneyCurrentMonth; // сумма за текущий месяц (в деньгах) (sum(price_in_fact_amount) where x<time_requesting<y)
    private double allSummMissionMoney; // общая сумма заказов (в деньгах) (SUM(price_in_fact_amount))
    private double allAverageSummMissionMoneyMonth; // средняя в месяц сумма (в деньгах) за весь период (sum(price_in_fact_amount)/count(distinct date_format(time_requesting,'%Y-%m')))
    private double averageSummMissionMoneyCurrentMonth; // средняя сумма (в деньгах) за текущий месяц (avg(sum) where x<time_requesting<y)
    private double averageCheckMoney; // средний чек вообще (в деньгах) sum(price_in_fact_amount)/count(*)
    private int countBlock; // кол-во блокировок.

    public int getCountCanceledMission() {
        return countCanceledMission;
    }

    public void setCountCanceledMission(int countCanceledMission) {
        this.countCanceledMission = countCanceledMission;
    }

    public int getCountMission() {
        return countMission;
    }

    public void setCountMission(int countMission) {
        this.countMission = countMission;
    }

    public double getAverageCountMissionMonth() {
        return averageCountMissionMonth;
    }

    public void setAverageCountMissionMonth(double averageCountMissionMonth) {
        this.averageCountMissionMonth = averageCountMissionMonth;
    }

    public int getCountMissionCurrentMonth() {
        return countMissionCurrentMonth;
    }

    public void setCountMissionCurrentMonth(int countMissionCurrentMonth) {
        this.countMissionCurrentMonth = countMissionCurrentMonth;
    }

    public double getSummMissionMoneyCurrentMonth() {
        return summMissionMoneyCurrentMonth;
    }

    public void setSummMissionMoneyCurrentMonth(double summMissionMoneyCurrentMonth) {
        this.summMissionMoneyCurrentMonth = summMissionMoneyCurrentMonth;
    }

    public double getAllSummMissionMoney() {
        return allSummMissionMoney;
    }

    public void setAllSummMissionMoney(double allSummMissionMoney) {
        this.allSummMissionMoney = allSummMissionMoney;
    }

    public double getAllAverageSummMissionMoneyMonth() {
        return allAverageSummMissionMoneyMonth;
    }

    public void setAllAverageSummMissionMoneyMonth(double allAverageSummMissionMoneyMonth) {
        this.allAverageSummMissionMoneyMonth = allAverageSummMissionMoneyMonth;
    }

    public double getAverageSummMissionMoneyCurrentMonth() {
        return averageSummMissionMoneyCurrentMonth;
    }

    public void setAverageSummMissionMoneyCurrentMonth(double averageSummMissionMoneyCurrentMonth) {
        this.averageSummMissionMoneyCurrentMonth = averageSummMissionMoneyCurrentMonth;
    }

    public double getAverageCheckMoney() {
        return averageCheckMoney;
    }

    public void setAverageCheckMoney(double averageCheckMoney) {
        this.averageCheckMoney = averageCheckMoney;
    }

    public int getCountBlock() {
        return countBlock;
    }

    public void setCountBlock(int countBlock) {
        this.countBlock = countBlock;
    }
}
