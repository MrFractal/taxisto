package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 26.08.14.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckTakeBookedResponse {
    private int take; // 0 - может взять бронь, 1 - уже есть бронь в течении часа до или после, 2 - не хватает денег в кошельке.
    private long missionBookedId = 0; // 0 если нет броней в течении часа,
    private Long missionId; // id брони которая мешает взять заказ, если их 2, то 1 по времени.


    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    public long getMissionBookedId() {
        return missionBookedId;
    }

    public void setMissionBookedId(long missionBookedId) {
        this.missionBookedId = missionBookedId;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }
}
