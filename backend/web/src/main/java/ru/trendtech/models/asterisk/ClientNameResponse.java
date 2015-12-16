package ru.trendtech.models.asterisk;

/**
 * Created by ivanenok on 4/13/2014.
 */
public class ClientNameResponse extends AsteriskResponse {
    private String name;

    public ClientNameResponse() {
    }

    public ClientNameResponse(AsteriskRequest request) {
        super(request);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
