package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 02.02.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusyIsPaymentResponse extends ErrorCodeHelper{
    private boolean result=false;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
