package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 27.04.2015.
 */
public class PeriodWorkRequest {
    private String security_token;
    private int numberPage;
    private int sizePage;

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
}
