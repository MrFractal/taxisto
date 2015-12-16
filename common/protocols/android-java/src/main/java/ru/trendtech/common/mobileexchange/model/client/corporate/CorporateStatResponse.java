package ru.trendtech.common.mobileexchange.model.client.corporate;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.corporate.MissionInfoCorporate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 13.03.2015.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class CorporateStatResponse extends ErrorCodeHelper {
    private long lastPageNumber;
    private long totalItems;
    private List<MissionInfoCorporate> missionInfos = new ArrayList<MissionInfoCorporate>();
    private int sum;
    private int allSum;

    public int getAllSum() {
        return allSum;
    }

    public void setAllSum(int allSum) {
        this.allSum = allSum;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public List<MissionInfoCorporate> getMissionInfos() {
        return missionInfos;
    }

    public void setMissionInfos(List<MissionInfoCorporate> missionInfos) {
        this.missionInfos = missionInfos;
    }
}
