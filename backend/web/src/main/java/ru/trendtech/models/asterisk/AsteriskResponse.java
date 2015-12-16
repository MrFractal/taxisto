package ru.trendtech.models.asterisk;

/**
 * Created by max on 12.04.2014.
 */
public class AsteriskResponse {
    private String requestId = "-1";

    public AsteriskResponse() {
    }

    public AsteriskResponse(AsteriskRequest request) {
        this.requestId = request.getRequestId();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
