package ru.trendtech.common.mobileexchange.model.courier.web;

/**
 * Created by petr on 25.08.2015.
 */
public class StoreRequest extends CommonRequest {
    private String mask;
    private long storeId;
    private int numberPage;
    private int sizePage;

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
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
