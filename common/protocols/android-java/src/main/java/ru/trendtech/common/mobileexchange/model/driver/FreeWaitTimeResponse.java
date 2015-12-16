package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 06.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FreeWaitTimeResponse extends ErrorCodeHelper {
    private int freeTime;
    private int freeTimeInFact;
    private int howLongWaitInFact;

    public int getHowLongWaitInFact() {
        return howLongWaitInFact;
    }

    public void setHowLongWaitInFact(int howLongWaitInFact) {
        this.howLongWaitInFact = howLongWaitInFact;
    }

    public int getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public int getFreeTimeInFact() {
        return freeTimeInFact;
    }

    public void setFreeTimeInFact(int freeTimeInFact) {
        this.freeTimeInFact = freeTimeInFact;
    }
}
