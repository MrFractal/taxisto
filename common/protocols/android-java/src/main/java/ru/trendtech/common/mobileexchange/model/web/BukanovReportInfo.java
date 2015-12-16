package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 06.08.2015.
 */
public class BukanovReportInfo {
    private int uniqueCount;
    private int allCount;
    private int outerCount;
    private int innerCount;
    private int doubleCount;
    private int countWithDriver;
    private int countWithDriverAndCompleted;
    private int countWithDriverAndCanceled;
    private int countWithDriverAndCanceledByClient;
    private int countWithDriverAndCanceledByDriver;
    private int countWithDriverAndCanceledByOperator;
    private int countWithDriverAndCanceledByServer;
    private int countWithoutDriver;
    private int countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes;
    private int countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch;
    private int countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes;
    private int countFromRemoteZone;
    private double mainPercent;

    public int getCountWithDriverAndCanceledByServer() {
        return countWithDriverAndCanceledByServer;
    }

    public void setCountWithDriverAndCanceledByServer(int countWithDriverAndCanceledByServer) {
        this.countWithDriverAndCanceledByServer = countWithDriverAndCanceledByServer;
    }

    public int getUniqueCount() {
        return uniqueCount;
    }

    public void setUniqueCount(int uniqueCount) {
        this.uniqueCount = uniqueCount;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getOuterCount() {
        return outerCount;
    }

    public void setOuterCount(int outerCount) {
        this.outerCount = outerCount;
    }

    public int getInnerCount() {
        return innerCount;
    }

    public void setInnerCount(int innerCount) {
        this.innerCount = innerCount;
    }

    public int getDoubleCount() {
        return doubleCount;
    }

    public void setDoubleCount(int doubleCount) {
        this.doubleCount = doubleCount;
    }

    public int getCountWithDriver() {
        return countWithDriver;
    }

    public void setCountWithDriver(int countWithDriver) {
        this.countWithDriver = countWithDriver;
    }

    public int getCountWithDriverAndCompleted() {
        return countWithDriverAndCompleted;
    }

    public void setCountWithDriverAndCompleted(int countWithDriverAndCompleted) {
        this.countWithDriverAndCompleted = countWithDriverAndCompleted;
    }

    public int getCountWithDriverAndCanceled() {
        return countWithDriverAndCanceled;
    }

    public void setCountWithDriverAndCanceled(int countWithDriverAndCanceled) {
        this.countWithDriverAndCanceled = countWithDriverAndCanceled;
    }

    public int getCountWithDriverAndCanceledByClient() {
        return countWithDriverAndCanceledByClient;
    }

    public void setCountWithDriverAndCanceledByClient(int countWithDriverAndCanceledByClient) {
        this.countWithDriverAndCanceledByClient = countWithDriverAndCanceledByClient;
    }

    public int getCountWithDriverAndCanceledByDriver() {
        return countWithDriverAndCanceledByDriver;
    }

    public void setCountWithDriverAndCanceledByDriver(int countWithDriverAndCanceledByDriver) {
        this.countWithDriverAndCanceledByDriver = countWithDriverAndCanceledByDriver;
    }

    public int getCountWithDriverAndCanceledByOperator() {
        return countWithDriverAndCanceledByOperator;
    }

    public void setCountWithDriverAndCanceledByOperator(int countWithDriverAndCanceledByOperator) {
        this.countWithDriverAndCanceledByOperator = countWithDriverAndCanceledByOperator;
    }

    public int getCountWithoutDriver() {
        return countWithoutDriver;
    }

    public void setCountWithoutDriver(int countWithoutDriver) {
        this.countWithoutDriver = countWithoutDriver;
    }

    public int getCountWithoutDriverAndCanceledWhenMissionAfterHalfMinutes() {
        return countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes;
    }

    public void setCountWithoutDriverAndCanceledWhenMissionAfterHalfMinutes(int countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes) {
        this.countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes = countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes;
    }

    public int getCountWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch() {
        return countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch;
    }

    public void setCountWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch(int countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch) {
        this.countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch = countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch;
    }

    public int getCountWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes() {
        return countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes;
    }

    public void setCountWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes(int countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes) {
        this.countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes = countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes;
    }

    public int getCountFromRemoteZone() {
        return countFromRemoteZone;
    }

    public void setCountFromRemoteZone(int countFromRemoteZone) {
        this.countFromRemoteZone = countFromRemoteZone;
    }

    public double getMainPercent() {
        return mainPercent;
    }

    public void setMainPercent(double mainPercent) {
        this.mainPercent = mainPercent;
    }
}
