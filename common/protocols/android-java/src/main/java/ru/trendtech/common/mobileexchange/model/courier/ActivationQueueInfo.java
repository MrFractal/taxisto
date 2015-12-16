package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ClientInfoARM;

/**
 * Created by petr on 04.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivationQueueInfo {
    private Long id;
    private ClientInfoARM clientInfoARM;
    private long timeOfRequest;
    private long timeOfActivation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientInfoARM getClientInfoARM() {
        return clientInfoARM;
    }

    public void setClientInfoARM(ClientInfoARM clientInfoARM) {
        this.clientInfoARM = clientInfoARM;
    }

    public long getTimeOfRequest() {
        return timeOfRequest;
    }

    public void setTimeOfRequest(long timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public long getTimeOfActivation() {
        return timeOfActivation;
    }

    public void setTimeOfActivation(long timeOfActivation) {
        this.timeOfActivation = timeOfActivation;
    }
}
