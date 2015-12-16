package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutocompleteInfo;

/**
 * Created by petr on 22.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressByGeoPointResponse extends ErrorCodeHelper {
    private WebAutocompleteInfo webAutocompleteInfo;

    public WebAutocompleteInfo getWebAutocompleteInfo() {
        return webAutocompleteInfo;
    }

    public void setWebAutocompleteInfo(WebAutocompleteInfo webAutocompleteInfo) {
        this.webAutocompleteInfo = webAutocompleteInfo;
    }
}
