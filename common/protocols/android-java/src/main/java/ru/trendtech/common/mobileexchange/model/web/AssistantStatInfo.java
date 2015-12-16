package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 17.11.2014.
 */
public class AssistantStatInfo {
    /*
    ассистент,
    количество отмененных заказов,
    сумма отмененных заказов,
    количество выполненных заказов,
    сумма выполненных заказов,
    */
    private long day;
    private AssistantInfo assistantInfo;
    private int countCanceledMiss;
    private double sumCanceledMiss;
    private int countCompletedMiss;
    private double sumCompletedMiss;


    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public AssistantInfo getAssistantInfo() {
        return assistantInfo;
    }

    public void setAssistantInfo(AssistantInfo assistantInfo) {
        this.assistantInfo = assistantInfo;
    }

    public int getCountCanceledMiss() {
        return countCanceledMiss;
    }

    public void setCountCanceledMiss(int countCanceledMiss) {
        this.countCanceledMiss = countCanceledMiss;
    }

    public int getCountCompletedMiss() {
        return countCompletedMiss;
    }

    public void setCountCompletedMiss(int countCompletedMiss) {
        this.countCompletedMiss = countCompletedMiss;
    }

    public double getSumCanceledMiss() {
        return sumCanceledMiss;
    }

    public void setSumCanceledMiss(double sumCanceledMiss) {
        this.sumCanceledMiss = sumCanceledMiss;
    }


    public double getSumCompletedMiss() {
        return sumCompletedMiss;
    }

    public void setSumCompletedMiss(double sumCompletedMiss) {
        this.sumCompletedMiss = sumCompletedMiss;
    }
}
