package ru.trendtech.common.mobileexchange.model.client;

public class AutoCompleteRequest {
    private String text;
    private int id;

    public AutoCompleteRequest(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
