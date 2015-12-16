package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 18.12.2014.
 */
public class TurboIncreaseDriverRequest {
    private TurboIncreaseDriverInfo turboIncreaseDriverInfo;

    public TurboIncreaseDriverInfo getTurboIncreaseDriverInfo() {
        return turboIncreaseDriverInfo;
    }

    public void setTurboIncreaseDriverInfo(TurboIncreaseDriverInfo turboIncreaseDriverInfo) {
        this.turboIncreaseDriverInfo = turboIncreaseDriverInfo;
    }
}
