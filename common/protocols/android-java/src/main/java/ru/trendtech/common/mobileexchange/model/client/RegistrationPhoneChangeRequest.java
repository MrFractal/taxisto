package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 07.08.14.
 */


public class RegistrationPhoneChangeRequest {

    private String login;
    private String password;
    private String phone;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
