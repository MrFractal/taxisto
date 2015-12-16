package ru.trendtech.models.asterisk;

/**
 * Created by ivanenok on 4/13/2014.
 */
public class TripCostResponse extends AsteriskResponse {
    private float cost;
    private String from;
    private String to;

    public TripCostResponse() {
    }

    public TripCostResponse(AsteriskRequest request) {
        super(request);
    }


    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
