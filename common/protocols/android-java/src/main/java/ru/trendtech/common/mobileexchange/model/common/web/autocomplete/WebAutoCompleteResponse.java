package ru.trendtech.common.mobileexchange.model.common.web.autocomplete;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 27.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebAutoCompleteResponse extends ErrorCodeHelper {
    private List<WebAutocompleteInfo> webAutocompleteInfos = new ArrayList<WebAutocompleteInfo>();

    public List<WebAutocompleteInfo> getWebAutocompleteInfos() {
        return webAutocompleteInfos;
    }

    public void setWebAutocompleteInfos(List<WebAutocompleteInfo> webAutocompleteInfos) {
        this.webAutocompleteInfos = webAutocompleteInfos;
    }
}
