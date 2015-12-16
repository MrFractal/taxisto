package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 08.08.14.
 */
public class ClientsListRequest {
    private String nameMask;
    private String phoneMask;
    private String emailMask;
    private long mainClientId;
    private Boolean onlyMainClient;
    private int numberPage;
    private int sizePage;
    private String security_token;
    private long clientId;
    private Boolean courierActivated;

    public Boolean getCourierActivated() {
        return courierActivated;
    }

    public void setCourierActivated(Boolean courierActivated) {
        this.courierActivated = courierActivated;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getMainClientId() {
        return mainClientId;
    }

    public void setMainClientId(long mainClientId) {
        this.mainClientId = mainClientId;
    }

    public Boolean getOnlyMainClient() {
        return onlyMainClient;
    }

    public void setOnlyMainClient(Boolean onlyMainClient) {
        this.onlyMainClient = onlyMainClient;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public int getNumberPage() {
        return numberPage;
    }

    public void setNumberPage(int numberPage) {
        this.numberPage = numberPage;
    }

    public int getSizePage() {
        return sizePage;
    }

    public void setSizePage(int sizePage) {
        this.sizePage = sizePage;
    }

//    public int getCountItems() {
//        return countItems;
//    }
//
//    public void setCountItems(int countItems) {
//        this.countItems = countItems;
//    }
//
//    public int getFromPosition() {
//        return fromPosition;
//    }
//
//    public void setFromPosition(int fromPosition) {
//        this.fromPosition = fromPosition;
//    }

    public String getNameMask() {
        return nameMask;
    }

    public void setNameMask(String nameMask) {
        this.nameMask = nameMask;
    }

    public String getPhoneMask() {
        return phoneMask;
    }

    public void setPhoneMask(String phoneMask) {
        this.phoneMask = phoneMask;
    }

    public String getEmailMask() {
        return emailMask;
    }

    public void setEmailMask(String emailMask) {
        this.emailMask = emailMask;
    }
}

