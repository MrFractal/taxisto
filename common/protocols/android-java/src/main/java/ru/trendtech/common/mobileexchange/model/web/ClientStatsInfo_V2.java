package ru.trendtech.common.mobileexchange.model.web;


/**
 * Created by petr on 17.10.2014.
 */
public class ClientStatsInfo_V2 {
   private long id;
   private String firstName;
   private String LastName;
   private String phone;
   private String administrativeState;
   private String email;
   private long registrationDate;
   private int countCompletedMission;
   private int countCanceledMission;
   private int countSentPromoCodes;
   private int amountBalance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdministrativeState() {
        return administrativeState;
    }

    public void setAdministrativeState(String administrativeState) {
        this.administrativeState = administrativeState;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getCountCompletedMission() {
        return countCompletedMission;
    }

    public void setCountCompletedMission(int countCompletedMission) {
        this.countCompletedMission = countCompletedMission;
    }

    public int getCountCanceledMission() {
        return countCanceledMission;
    }

    public void setCountCanceledMission(int countCanceledMission) {
        this.countCanceledMission = countCanceledMission;
    }

    public int getCountSentPromoCodes() {
        return countSentPromoCodes;
    }

    public void setCountSentPromoCodes(int countSentPromoCodes) {
        this.countSentPromoCodes = countSentPromoCodes;
    }

    public int getAmountBalance() {
        return amountBalance;
    }

    public void setAmountBalance(int amountBalance) {
        this.amountBalance = amountBalance;
    }
}
