package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import ru.trendtech.domain.billing.Account;
import ru.trendtech.domain.courier.Order;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
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

    @Column(name = "sms_code", unique = true)
    private String smsCode;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender = Gender.UNDEFINED;

    @Column(name = "registration_state")
    @Enumerated(value = EnumType.STRING)
    private RegistrationState registrationState = RegistrationState.NEW;

    @Column(name = "last_login_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastLoginTime;

    @Column(name = "registration_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime registrationTime;

    @Column(name = "blocking_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime blockingTime;

    @Column(name = "last_action_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastActionTime;

    @Column(name = "picture_url")
    private String picureUrl;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "birthday")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate birthday;

    @Column(name = "token", length = 32)
    private String token;

    @Column(name = "version_app", length = 25)
    private String versionApp="";

    @OneToOne
    @JoinColumn(name = "account_id", unique = true, nullable = false, updatable = false)
    private Account account;

    @OneToOne
    @JoinColumn(name = "main_client_id")
    private Client mainClient;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @OneToOne
    @JoinColumn(name = "multiple_mission_id")
    private MultipleMission multipleMission;

    @OneToMany
    @JoinColumn(name = "booked_client_id", insertable = true, updatable = true)
    private Set<Mission> bookedMissions = new HashSet<>();

    @OneToMany(fetch=FetchType.EAGER)
    private Set<DeviceInfo> devices = new HashSet<>();

    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private State administrativeState;

    @Column(name = "device_type")
    private String deviceType; // (APPLE, ANDROID_CLIENT)

    @Column(name = "phone_id")
    private String phoneId;

    @Column(name = "corp_login")
    private String corporateLogin;

    @Column(name = "corp_password")
    private String corporatePassword;

    @Column(name = "courier_activated", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean courierActivated;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "email_unsubscribe", columnDefinition = "BIT DEFAULT 0", length = 1, nullable = false)
    private boolean emailUnsubscribe;

    public boolean isEmailUnsubscribe() {
        return emailUnsubscribe;
    }

    public void setEmailUnsubscribe(boolean emailUnsubscribe) {
        this.emailUnsubscribe = emailUnsubscribe;
    }

    public boolean isCourierActivated() {
        return courierActivated;
    }

    public void setCourierActivated(boolean courierActivated) {
        this.courierActivated = courierActivated;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MultipleMission getMultipleMission() {
        return multipleMission;
    }

    public void setMultipleMission(MultipleMission multipleMission) {
        this.multipleMission = multipleMission;
    }

    public String getCorporateLogin() {
        return corporateLogin;
    }

    public void setCorporateLogin(String corporateLogin) {
        this.corporateLogin = corporateLogin;
    }

    public String getCorporatePassword() {
        return corporatePassword;
    }

    public void setCorporatePassword(String corporatePassword) {
        this.corporatePassword = corporatePassword;
    }

    public Client getMainClient() {
        return mainClient;
    }

    public void setMainClient(Client mainClient) {
        this.mainClient = mainClient;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }

    public Set<Mission> getBookedMissions() {
        return bookedMissions;
    }

    public void setBookedMissions(Set<Mission> bookedMissions) {
        this.bookedMissions = bookedMissions;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPicureUrl() {
        return picureUrl;
    }

    public void setPicureUrl(String picureUrl) {
        this.picureUrl = picureUrl;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public RegistrationState getRegistrationState() {
        return registrationState;
    }

    public void setRegistrationState(RegistrationState registrationState) {
        this.registrationState = registrationState;
    }

    public Set<DeviceInfo> getDevices() {
        return devices;
    }

    public void setDevices(Set<DeviceInfo> devices) {
        this.devices = devices;
    }

    public DateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(DateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public DateTime getLastActionTime() {
        return lastActionTime;
    }

    public void setLastActionTime(DateTime lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public State getAdministrativeState() {
        return administrativeState;
    }

    public void setAdministrativeState(State administrativeState) {
        this.administrativeState = administrativeState;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public DateTime getBlockingTime() {
        return blockingTime;
    }

    public void setBlockingTime(DateTime blockingTime) {
        this.blockingTime = blockingTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "firstName";//+ firstName;
//                "Client{" +
//                "id=" + id +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", email='" + email + '\'' +
//                ", phone='" + phone + '\'' +
//                ", password='" + password + '\'' +
//                ", country='" + country + '\'' +
//                ", city='" + city + '\'' +
//                ", smsCode='" + smsCode + '\'' +
//                ", gender=" + gender +
//                ", registrationState=" + registrationState +
//                ", lastLoginTime=" + lastLoginTime +
//                ", lastActionTime=" + lastActionTime +
//                ", picureUrl='" + picureUrl + '\'' +
//                ", rating=" + rating +
//                ", birthday=" + birthday +
//                //", cards='" + cards + '\'' +
//                ", account=" + account +
//                ", mission=" + mission +
//                ", devices=" + devices +
//                ", state=" + administrativeState +
//                '}';
    }

    public static enum Gender {
        MALE,
        FEMALE,
        UNDEFINED
    }

    public static enum State {
        ACTIVE(1),
        INACTIVE(2),
        BLOCKED(3),
        ;
        private int value;
        State(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }


    public static enum RegistrationState {
        NEW,
        CONFIRMED,
        TERMINAL_NEW,
        TERMINAL_CONFIRMED
    }

    public DateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(DateTime registrationTime) {
        this.registrationTime = registrationTime;
    }
}
