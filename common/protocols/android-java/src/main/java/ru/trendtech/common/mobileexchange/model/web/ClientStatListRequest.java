package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 17.10.2014.
 */
public class ClientStatListRequest {
    private String nameMask;
    private String phoneMask;
    private String adminStatus;
    private int countMissionStart;
    private int countMissionEnd;
    private long registrationDateStart;
    private long registrationDateEnd;
    private int countCanceledStart;
    private int countCanceledEnd;
    private int numberPage;
    private int sizePage;
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


    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public int getCountMissionStart() {
        return countMissionStart;
    }

    public void setCountMissionStart(int countMissionStart) {
        this.countMissionStart = countMissionStart;
    }

    public int getCountMissionEnd() {
        return countMissionEnd;
    }

    public void setCountMissionEnd(int countMissionEnd) {
        this.countMissionEnd = countMissionEnd;
    }

    public long getRegistrationDateStart() {
        return registrationDateStart;
    }

    public void setRegistrationDateStart(long registrationDateStart) {
        this.registrationDateStart = registrationDateStart;
    }

    public long getRegistrationDateEnd() {
        return registrationDateEnd;
    }

    public void setRegistrationDateEnd(long registrationDateEnd) {
        this.registrationDateEnd = registrationDateEnd;
    }

    public int getCountCanceledStart() {
        return countCanceledStart;
    }

    public void setCountCanceledStart(int countCanceledStart) {
        this.countCanceledStart = countCanceledStart;
    }

    public int getCountCanceledEnd() {
        return countCanceledEnd;
    }

    public void setCountCanceledEnd(int countCanceledEnd) {
        this.countCanceledEnd = countCanceledEnd;
    }
}
