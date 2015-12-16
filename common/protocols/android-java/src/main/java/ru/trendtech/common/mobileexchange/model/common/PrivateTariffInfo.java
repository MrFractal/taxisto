package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 19.05.2015.
 */
public class PrivateTariffInfo {
    private ClientInfoARM clientInfoARM;
    private boolean active;
    private boolean activated;
    private String promo;
    private long expirationDate;
    private long activationDate;
    private int tariffId;
    private int freeWaitMinutesByTariff; // индивидуаьное время ожидания для тарифа для конкретного корп. клиента

    public int getFreeWaitMinutesByTariff() {
        return freeWaitMinutesByTariff;
    }

    public void setFreeWaitMinutesByTariff(int freeWaitMinutesByTariff) {
        this.freeWaitMinutesByTariff = freeWaitMinutesByTariff;
    }

    public int getTariffId() {
        return tariffId;
    }

    public void setTariffId(int tariffId) {
        this.tariffId = tariffId;
    }

    public ClientInfoARM getClientInfoARM() {
        return clientInfoARM;
    }

    public void setClientInfoARM(ClientInfoARM clientInfoARM) {
        this.clientInfoARM = clientInfoARM;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(long activationDate) {
        this.activationDate = activationDate;
    }
}
