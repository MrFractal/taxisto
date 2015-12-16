package ru.trendtech.common.mobileexchange.model.common.web.autocomplete;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

/**
 * Created by petr on 27.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebAutoCompleteRequest extends CommonRequest{
    private String addressMask;

    public String getAddressMask() {
        return addressMask;
    }

    public void setAddressMask(String addressMask) {
        this.addressMask = addressMask;
    }
}
