package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 11.03.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LowCosterDescResponse extends ErrorCodeHelper{
    private String htmlMessage;

    public String getHtmlMessage() {
        return htmlMessage;
    }

    public void setHtmlMessage(String htmlMessage) {
        this.htmlMessage = htmlMessage;
    }
}
