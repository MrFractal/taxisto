package ru.trendtech.common.mobileexchange.model.common.billing.paymobile;

/**
 * Created by petr on 06.02.2015.
 */
public class RegisterOrderClientWithTipRequest {
    private long clientId;
    private long missionId;
    private boolean answer;
    private String security_token;
    private long tipPercentId;
    private int sumWithoutTipPercent;


    public int getSumWithoutTipPercent() {
        return sumWithoutTipPercent;
    }

    public void setSumWithoutTipPercent(int sumWithoutTipPercent) {
        this.sumWithoutTipPercent = sumWithoutTipPercent;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getTipPercentId() {
        return tipPercentId;
    }

    public void setTipPercentId(long tipPercentId) {
        this.tipPercentId = tipPercentId;
    }
}
