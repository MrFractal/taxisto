package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class StringsListResponse extends ErrorCodeHelper{
    private List<String> values = new ArrayList<String>();

    public List<String> getValues() {
        return values;
    }
}
