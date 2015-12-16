package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 13.08.14.
 */

public class MissionsListRequest {
    private int numberPage;
    private int sizePage;
    private long dateStart; //с какой даты фильтровать (по time_requesting)
    private long dateEnd; //до какой даты фильтровать
    private String nameMask; //Имя клиента
    private String phoneMask; //Телефон клиента
    private String carModelMask; //марка авто или часть
    private String carNumberMask; //гос номер авто или часть
    private String state;
    private long missionId;
    private long assistantId;
    private String typeOS;
    private String security_token;
    private long clientId;
    private long driverId;
    private int autoType;
    private Boolean onlyBooked;

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public Boolean isOnlyBooked() {
        return onlyBooked;
    }

    public void setOnlyBooked(Boolean onlyBooked) {
        this.onlyBooked = onlyBooked;
    }

    public int getAutoType() {
        return autoType;
    }

    public void setAutoType(int autoType) {
        this.autoType = autoType;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }


    public String getTypeOS() {
        return typeOS;
    }

    public void setTypeOS(String typeOS) {
        this.typeOS = typeOS;
    }

    public long getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(long assistantId) {
        this.assistantId = assistantId;
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

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
    }

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

    public String getCarModelMask() {
        return carModelMask;
    }

    public void setCarModelMask(String carModelMask) {
        this.carModelMask = carModelMask;
    }

    public String getCarNumberMask() {
        return carNumberMask;
    }

    public void setCarNumberMask(String carNumberMask) {
        this.carNumberMask = carNumberMask;
    }
}
