package ru.trendtech.domain.admin;

import ru.trendtech.common.mobileexchange.model.web.AdministratorRole;
import ru.trendtech.domain.Assistant;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.domain.Role;
import ru.trendtech.domain.WebUserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "web_users")
public class WebUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "user_role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AdministratorRole role;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "manager_phone")
    private String phoneOfManager;

    @Column(name = "state")
    private State state = State.OFFLINE;

    @Column(name = "token", length = 32)
    private String token;

    @Column(name = "company", length = 50)
    private String company;

    @Column(name = "sip_user", length = 25)
    private String sipUser;

    @Column(name = "sip_password", length = 25)
    private String sipPassword;

    @Column(name = "wa_phone", length = 11)
    private String waPhone;

    @Column(name = "wa_password", length = 32)
    private String waPassword;

    @Column(name = "administrative_status")
    @Enumerated(value = EnumType.STRING)
    private AdminStatus adminStatus;

    @OneToOne
    @JoinColumn(name = "assistant_id")
    private Assistant assistant;

    @Column(name = "current_state")
    @Enumerated(value = EnumType.STRING)
    private CurrentState currentState;

    @Column(name = "socket_id")
    private String socket_id;

    @Column(name = "taxopark_id")
    private Long taxoparkId;


    public Long getTaxoparkId() {
        return taxoparkId;
    }

    public void setTaxoparkId(Long taxoparkId) {
        this.taxoparkId = taxoparkId;
    }

    public String getSocket_id() {
        return socket_id;
    }

    public void setSocket_id(String socket_id) {
        this.socket_id = socket_id;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }

    public CurrentState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(CurrentState currentState) {
        this.currentState = currentState;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSipUser() {
        return sipUser;
    }

    public void setSipUser(String sipUser) {
        this.sipUser = sipUser;
    }

    public String getWaPhone() {
        return waPhone;
    }

    public void setWaPhone(String waPhone) {
        this.waPhone = waPhone;
    }

    public String getWaPassword() {
        return waPassword;
    }

    public void setWaPassword(String waPassword) {
        this.waPassword = waPassword;
    }


    public String getSipPassword() {
        return sipPassword;
    }

    public void setSipPassword(String sipPassword) {
        this.sipPassword = sipPassword;
    }

    public AdminStatus getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(AdminStatus adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public AdministratorRole getRole() {
        return role;
    }

    public void setRole(AdministratorRole role) {
        this.role = role;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneOfManager() {
        return phoneOfManager;
    }

    public void setPhoneOfManager(String phoneOfManager) {
        this.phoneOfManager = phoneOfManager;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State{
        ONLINE,
        AWAY,
        OFFLINE,
        ;
    }
}
