package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.client.CardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 06.02.14.
 *
 * return clients cards info
 *
 */
public class CardsResponse {
    private List<CardInfo> cards = new ArrayList<CardInfo>();

    public List<CardInfo> getCards() {
        return cards;
    }
}
