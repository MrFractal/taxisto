package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import java.util.*;

/**
 * Created by petr on 27.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonTripHistoryResponse extends ErrorCodeHelper {
    private  LinkedHashMap<String, List<CommonTripHistoryInfo>> commonHistory = new  LinkedHashMap<String, List<CommonTripHistoryInfo>>();

    public LinkedHashMap<String, List<CommonTripHistoryInfo>> getCommonHistory() {
        return commonHistory;
    }

    public void setCommonHistory(LinkedHashMap<String, List<CommonTripHistoryInfo>> commonHistory) {
        this.commonHistory = commonHistory;
    }
}
