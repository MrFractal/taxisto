package ru.trendtech.common.mobileexchange.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.AdditionalServiceInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 07.07.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalServiceResponse extends ErrorCodeHelper {
    private List<AdditionalServiceInfo> additionalServiceInfos = new ArrayList<AdditionalServiceInfo>();

    public List<AdditionalServiceInfo> getAdditionalServiceInfos() {
        return additionalServiceInfos;
    }

    public void setAdditionalServiceInfos(List<AdditionalServiceInfo> additionalServiceInfos) {
        this.additionalServiceInfos = additionalServiceInfos;
    }
}
