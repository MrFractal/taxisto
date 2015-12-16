package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 02.03.2015.
 */
/* not use */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IsOwnDriverResponse extends ErrorCodeHelper {
    private boolean isOwnDriver = false;

    public boolean isOwnDriver() {
        return isOwnDriver;
    }

    public void setOwnDriver(boolean isOwnDriver) {
        this.isOwnDriver = isOwnDriver;
    }
}
