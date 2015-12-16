package ru.trendtech.common.mobileexchange.model.client;

import java.util.ArrayList;
import java.util.List;

public class CardsInfoResponse {
    private List<CardInfo> cardInfo = new ArrayList<CardInfo>();

    public List<CardInfo> getCardInfo() {
        return cardInfo;
    }
}
