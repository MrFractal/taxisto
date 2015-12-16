package ru.trendtech.common.mobileexchange.model.client;

public class CardsInfoRequest {
    private long clientId;
    private CardInfo cardInfo;

    public CardsInfoRequest(){super();}

    public CardsInfoRequest(long clientId){this.clientId = clientId;}

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }
}
