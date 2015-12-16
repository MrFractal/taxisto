package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 09.12.2014.
 */
public class PropertyUpdateRequest {
    private String propName;
    private String propValue;

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }
}
