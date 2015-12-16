package ru.trendtech.common.mobileexchange.model.web;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by petr on 24.10.2014.
 */

/*
Отзыв или комментарий.
2. По дате
3. По водителю (Ф.И., ID)
4. По клиенту (Ф.И., Телефон, емейл, ID)

 */

public class EstimateInfoRequest {
    private int numberPage;
    private int sizePage;
    private List<QueryDetails> queryDetailsList = new ArrayList<QueryDetails>();
    private String security_token;

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

    public List<QueryDetails> getQueryDetailsList() {
        return queryDetailsList;
    }

    public void setQueryDetailsList(List<QueryDetails> queryDetailsList) {
        this.queryDetailsList = queryDetailsList;
    }
}
