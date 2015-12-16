package ru.trendtech.services.promo;

import java.util.List;

/**
 * Created by petr on 30.10.2015.
 */
public interface PromoCodeServiceI {

    void generatePromoCode(int count, int amount, String channel, long fromClientId, int capacity, int availableUseCount);

    void activatePromo(long requesterId, long promoId, String value);

    List<PromoCode> getListPromo();
}
