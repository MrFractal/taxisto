package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 12.05.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimersDataRequest extends CommonRequest{
    private List<TimersDataInfo> timersDataInfos = new ArrayList<TimersDataInfo>();

    public List<TimersDataInfo> getTimersDataInfos() {
        return timersDataInfos;
    }

    public void setTimersDataInfos(List<TimersDataInfo> timersDataInfos) {
        this.timersDataInfos = timersDataInfos;
    }
}
