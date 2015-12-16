package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 21.04.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientMoneyAmountResponse extends ErrorCodeHelper {
    private int bonusesAmount;
    private int corporateAmount;
    private int courierAmount;

    public int getCourierAmount() {
        return courierAmount;
    }

    public void setCourierAmount(int courierAmount) {
        this.courierAmount = courierAmount;
    }

    public int getBonusesAmount() {
        return bonusesAmount;
    }

    public void setBonusesAmount(int bonusesAmount) {
        this.bonusesAmount = bonusesAmount;
    }

    public int getCorporateAmount() {
        return corporateAmount;
    }

    public void setCorporateAmount(int corporateAmount) {
        this.corporateAmount = corporateAmount;
    }
}
