package ru.trendtech.models.asterisk;

/**
 * Created by ivanenok on 4/13/2014.
 */
public class DetailedBillingResponse extends AsteriskResponse {
    private String details;

    public DetailedBillingResponse(AsteriskRequest request) {
        super(request);
    }

    public DetailedBillingResponse() {
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
