package ru.trendtech.common.mobileexchange.model.courier.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 02.11.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTargetAddressStateRequest extends CommonRequest {
    private long targetAddressId;
    private int targetAddressState;

    public long getTargetAddressId() {
        return targetAddressId;
    }

    public void setTargetAddressId(long targetAddressId) {
        this.targetAddressId = targetAddressId;
    }

    public int getTargetAddressState() {
        return targetAddressState;
    }

    public void setTargetAddressState(int targetAddressState) {
        this.targetAddressState = targetAddressState;
    }
}
