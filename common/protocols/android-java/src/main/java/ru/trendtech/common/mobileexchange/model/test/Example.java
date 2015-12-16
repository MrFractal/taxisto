package ru.trendtech.common.mobileexchange.model.test;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Created by petr on 24.03.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Example {
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
