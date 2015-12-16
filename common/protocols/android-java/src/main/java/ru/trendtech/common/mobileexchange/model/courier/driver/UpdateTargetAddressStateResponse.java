package ru.trendtech.common.mobileexchange.model.courier.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 02.11.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTargetAddressStateResponse extends ErrorCodeHelper {
    private int newTargetAddressState;

    public int getNewTargetAddressState() {
        return newTargetAddressState;
    }

    public void setNewTargetAddressState(int newTargetAddressState) {
        this.newTargetAddressState = newTargetAddressState;
    }
}
