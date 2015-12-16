package ru.trendtech.common.mobileexchange.model.client;

public class CheckGisResponse {
    private AddressGis addressGis;
    private int action;             //1 - key home, 2 - key street, 3 - continue trip, 4 - start mission
    private int idParent;

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public AddressGis getAddressGis() {
        return addressGis;
    }

    public void setAddressGis(AddressGis addressGis) {
        this.addressGis = addressGis;
    }
}
