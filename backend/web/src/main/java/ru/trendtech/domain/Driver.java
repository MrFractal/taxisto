package ru.trendtech.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.joda.time.LocalDate;
import ru.trendtech.domain.admin.DriverCarInfo;
import ru.trendtech.domain.billing.Account;
import ru.trendtech.domain.courier.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "driver")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone", nullable = false) // unique = true,
    private String phone;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "rating")
    private double rating;

    @Column(name = "rated_missions")
    private int ratedMissions;

    @Column(name = "rating_points")
    private int ratingPoints;

    @Column(name = "stops_count")
    private int stopsCount;

    @Column(name = "stops_time")
    private int stopsTime;

    @Column(name = "auto_model")
    private String autoModel;

    @Column(name = "auto_mumber")
    private String autoNumber;

    @Column(name = "auto_class")
    @Enumerated(value = EnumType.STRING)
    private AutoClass autoClass;

    @Column(name = "auto_color")
    private String autoColor;

    @Column(name = "auto_year")
    private int autoYear;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "declined_driver_id")
    private String declinedDrivers;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true, nullable = false, updatable = false)
    private Account account;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "photo_url_ver")
    private String photoUrlByVersion;

    @OneToOne
    @JoinColumn(name = "current_mission_id")
    private Mission currentMission;

    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private State state = State.OFFLINE;

    @OneToMany
    @JoinColumn(name = "booked_driver_id", insertable = true, updatable = true)
    private Set<Mission> bookedMissions = new HashSet<>();

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> photosCarsUrl = new ArrayList<>();

    @Column(name = "token", length = 32)
    private String token;

    @Column(name = "version_app")
    private String versionApp;

    @Column(name = "sms_code", unique = true)
    private String smsCode;

    @Column(name = "entrepreneur", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean entrepreneur=false;

    @Column(name = "wifi", columnDefinition = "BIT DEFAULT 1", length = 1)
    private boolean wifi;

    @OneToOne
    @JoinColumn(name = "taxopark_id")
    private TaxoparkPartners taxoparkPartners;

    @Column(name = "email", unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "assistant_id")
    private Assistant assistant;

    @Column(name = "type_x", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean typeX;

    @OneToOne
    @JoinColumn(name = "tablet_id")
    private Tablet tablet;

    @OneToOne
    @JoinColumn(name = "router_id")
    private Router router;

    // ----------- данные для анкеты водителя ------------ //

    // рост
    @Column(name = "growth")
    private int growth = 0;

    @Column(name = "weight")
    private int weight = 0;

    @Column(name = "family_status")
    private String familyStatus = "";

    @Column(name = "childrens",columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean childrens;

    @Column(name = "hobby")
    private String hobby = "";

    @Column(name = "dream")
    private String dream = "";

    @OneToMany
    @JoinColumn(name = "driver_id", updatable = true)
    private Set<DriverRequisite> driverRequisites = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "driver_setting_id", unique = true)
    private DriverSetting driverSetting;

    @OneToOne
    @JoinColumn(name = "current_order_id")
    private Order currentOrder;

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public DriverSetting getDriverSetting() {
        return driverSetting;
    }

    public void setDriverSetting(DriverSetting driverSetting) {
        this.driverSetting = driverSetting;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public Tablet getTablet() {
        return tablet;
    }

    public void setTablet(Tablet tablet) {
        this.tablet = tablet;
    }

    public String getPhotoUrlByVersion() {
        return photoUrlByVersion;
    }

    public void setPhotoUrlByVersion(String photoUrlByVersion) {
        this.photoUrlByVersion = photoUrlByVersion;
    }

    public boolean isTypeX() {
        return typeX;
    }

    public void setTypeX(boolean typeX) {
        this.typeX = typeX;
    }

    public Set<DriverRequisite> getDriverRequisites() {
        return driverRequisites;
    }

    public void setDriverRequisites(Set<DriverRequisite> driverRequisites) {
        this.driverRequisites = driverRequisites;
    }

    public String getDream() {
        return dream;
    }

    public void setDream(String dream) {
        this.dream = dream;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public boolean isChildrens() {
        return childrens;
    }

    public void setChildrens(boolean childrens) {
        this.childrens = childrens;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(String familyStatus) {
        this.familyStatus = familyStatus;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TaxoparkPartners getTaxoparkPartners() {
        return taxoparkPartners;
    }

    public void setTaxoparkPartners(TaxoparkPartners taxoparkPartners) {
        this.taxoparkPartners = taxoparkPartners;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isEntrepreneur() {
        return entrepreneur;
    }

    public void setEntrepreneur(boolean entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }

    public String getDeclinedDrivers() {
        return declinedDrivers;
    }

    public void setDeclinedDrivers(String declinedDrivers) {
        this.declinedDrivers = declinedDrivers;
    }

//    @ElementCollection(fetch = FetchType.EAGER)
//    private List<DriverCash> driverCash = new ArrayList<>();

//    public List<DriverCash> getDriverCash() {
//        return driverCash;
//    }
//
//    public void setDriverCash(List<DriverCash> driverCash) {
//        this.driverCash = driverCash;
//    }

    @OneToMany
    private Set<DeviceInfo> devices = new HashSet<>();

    @OneToMany
    private List<DriverCarInfo> cars = new ArrayList<>();

    @Column(name = "administrative_status")
    @Enumerated(value = EnumType.STRING)
    private AdministrativeStatus administrativeStatus = AdministrativeStatus.INACTIVE;


    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public int getAutoYear() {
        return autoYear;
    }

    public void setAutoYear(int autoYear) {
        this.autoYear = autoYear;
    }

    public int getRatingPoints() {
        return ratingPoints;
    }

    public void setRatingPoints(int ratingPoints) {
        this.ratingPoints = ratingPoints;
    }

    public Set<Mission> getBookedMissions() {
        return bookedMissions;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAutoModel() {
        return autoModel;
    }

    public void setAutoModel(String autoModel) {
        this.autoModel = autoModel;
    }

    public String getAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(String autoNumber) {
        this.autoNumber = autoNumber;
    }

    public String getAutoColor() {
        return autoColor;
    }

    public void setAutoColor(String autoColor) {
        this.autoColor = autoColor;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<String> getPhotosCarsUrl() {
        return photosCarsUrl;
    }

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

    public Mission getCurrentMission() {
        return currentMission;
    }

    public void setCurrentMission(Mission currentMission) {
        this.currentMission = currentMission;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public AutoClass getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(AutoClass autoClass) {
        this.autoClass = autoClass;
    }

    public int getStopsCount() {
        return stopsCount;
    }

    public void setStopsCount(int stopsCount) {
        this.stopsCount = stopsCount;
    }

    public int getStopsTime() {
        return stopsTime;
    }

    public void setStopsTime(int stopsTime) {
        this.stopsTime = stopsTime;
    }

    public Set<DeviceInfo> getDevices() {
        return devices;
    }

    public void setDevices(Set<DeviceInfo> devices) {
        this.devices = devices;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getRatedMissions() {
        return ratedMissions;
    }

    public void setRatedMissions(int ratedMissions) {
        this.ratedMissions = ratedMissions;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<DriverCarInfo> getCars() {
        return cars;
    }

    public void setCars(List<DriverCarInfo> cars) {
        this.cars = cars;
    }

    public AdministrativeStatus getAdministrativeStatus() {
        return administrativeStatus;
    }

    public void setAdministrativeStatus(AdministrativeStatus administrativeStatus) {
        this.administrativeStatus = administrativeStatus;
    }

    @Override
    public String toString() {
        return "";
        /*
                "Driver{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", rating=" + rating +
                ", stopsCount=" + stopsCount +
                ", stopsTime=" + stopsTime +
                ", autoModel='" + autoModel + '\'' +
                ", autoNumber='" + autoNumber + '\'' +
                ", autoClass=" + autoClass +
                ", autoColor='" + autoColor + '\'' +
                ", account=" + account +
                ", photoUrl='" + photoUrl + '\'' +
                ", currentMission=" + currentMission +
                ", state=" + state +
                ", photosCarsUrl=" + photosCarsUrl +
                ", devices=" + devices +
                '}';
        */
    }

    /*
      предлагаю добавить сюда статусы - водитель отдыхает, отдыхает платно, отдыхает в запрещенный период времени
    */

    public static enum State {
        OFFLINE,
        AVAILABLE,
        BUSY,
        ALARM,
//      REST,
//      PAY_REST
        ;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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





    public enum  AdministrativeStatus {
        INACTIVE,
        ACTIVE,
        BLOCKED,
        FIRED,
        ;
    }

}
