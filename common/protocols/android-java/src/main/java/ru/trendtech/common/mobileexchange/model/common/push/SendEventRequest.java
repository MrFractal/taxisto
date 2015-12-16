package ru.trendtech.common.mobileexchange.model.common.push;

/**
 * Created by petr on 29.10.2015.
 */
public class SendEventRequest {
    private long missionId;
    private boolean result;
    private String answer;

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
