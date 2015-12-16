package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ClientInfoARM;
import ru.trendtech.common.mobileexchange.model.common.DriverInfoARM;
import ru.trendtech.common.mobileexchange.model.common.RegionInfo;

/**
 * Created by petr on 09.06.2015.
 */
public class FantomStatInfo {
    /*
    Дата время события
2. id фантома
3. id обычного водителя
4. id заказа
5. сумма заказа
6. sum_increase
7. clientInfo_id
8. from_address
9. zone
10. cancel_by (если есть)
11. сумма за извинение
12. время висения на фантоме
     */
    private long missionId;
    private long timeOfAssigned;
    private DriverInfoARM fantomDriver;
    private DriverInfoARM driver;
    private int sumMission;
    private int sumIncrease;
    private ClientInfoARM clientInfoARM;
    private String fromAddress;
    private RegionInfo regionInfo;
    private String cancelBy;
    private int sorrySum;
    private int generalTimeSearch;

    public int getSorrySum() {
        return sorrySum;
    }

    public void setSorrySum(int sorrySum) {
        this.sorrySum = sorrySum;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public long getTimeOfAssigned() {
        return timeOfAssigned;
    }

    public void setTimeOfAssigned(long timeOfAssigned) {
        this.timeOfAssigned = timeOfAssigned;
    }

    public DriverInfoARM getFantomDriver() {
        return fantomDriver;
    }

    public void setFantomDriver(DriverInfoARM fantomDriver) {
        this.fantomDriver = fantomDriver;
    }

    public DriverInfoARM getDriver() {
        return driver;
    }

    public void setDriver(DriverInfoARM driver) {
        this.driver = driver;
    }

    public int getSumMission() {
        return sumMission;
    }

    public void setSumMission(int sumMission) {
        this.sumMission = sumMission;
    }

    public int getSumIncrease() {
        return sumIncrease;
    }

    public void setSumIncrease(int sumIncrease) {
        this.sumIncrease = sumIncrease;
    }

    public ClientInfoARM getClientInfoARM() {
        return clientInfoARM;
    }

    public void setClientInfoARM(ClientInfoARM clientInfoARM) {
        this.clientInfoARM = clientInfoARM;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public RegionInfo getRegionInfo() {
        return regionInfo;
    }

    public void setRegionInfo(RegionInfo regionInfo) {
        this.regionInfo = regionInfo;
    }

    public String getCancelBy() {
        return cancelBy;
    }

    public void setCancelBy(String cancelBy) {
        this.cancelBy = cancelBy;
    }

    public int getGeneralTimeSearch() {
        return generalTimeSearch;
    }

    public void setGeneralTimeSearch(int generalTimeSearch) {
        this.generalTimeSearch = generalTimeSearch;
    }
}
