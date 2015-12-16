package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 11.02.2015.
 */
public class GlobalDriverStatsRequest {
    private long registrationStart;
    private long registrationEnd;
    private long taxoparkId;
    private long assistantId;

    public long getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(long registrationStart) {
        this.registrationStart = registrationStart;
    }

    public long getRegistrationEnd() {
        return registrationEnd;
    }

    public void setRegistrationEnd(long registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public long getTaxoparkId() {
        return taxoparkId;
    }

    public void setTaxoparkId(long taxoparkId) {
        this.taxoparkId = taxoparkId;
    }

    public long getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(long assistantId) {
        this.assistantId = assistantId;
    }
}
