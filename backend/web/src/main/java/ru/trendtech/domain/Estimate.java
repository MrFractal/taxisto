package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import javax.persistence.*;

/**
 * Created by petr on 19.01.2015.
 */
@Entity
@Table(name = "estimate")
public class Estimate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "general")
    private int general = 0;

    @Column(name = "cleanliness")
    private int cleanlinessInCar = 0;

    @Column(name = "waiting_time", columnDefinition = "int default 0")
    private int waitingTime = 0;

    @Column(name = "driver_courtesy")
    private int driverCourtesy = 0;

    @Column(name = "application_convenience")
    private int applicationConvenience = 0;

    @Column(name = "wifi_quality")
    private int wifiQuality = 0;

    @Column(name = "estimate_comment")
    private String estimateComment;

    @Column(name = "estimate_date", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime estimateDate;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "visible", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean visible;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGeneral() {
        return general;
    }

    public void setGeneral(int general) {
        this.general = general;
    }

    public int getCleanlinessInCar() {
        return cleanlinessInCar;
    }

    public void setCleanlinessInCar(int cleanlinessInCar) {
        this.cleanlinessInCar = cleanlinessInCar;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getDriverCourtesy() {
        return driverCourtesy;
    }

    public void setDriverCourtesy(int driverCourtesy) {
        this.driverCourtesy = driverCourtesy;
    }

    public int getApplicationConvenience() {
        return applicationConvenience;
    }

    public void setApplicationConvenience(int applicationConvenience) {
        this.applicationConvenience = applicationConvenience;
    }

    public int getWifiQuality() {
        return wifiQuality;
    }

    public void setWifiQuality(int wifiQuality) {
        this.wifiQuality = wifiQuality;
    }

    public String getEstimateComment() {
        return estimateComment;
    }

    public void setEstimateComment(String estimateComment) {
        this.estimateComment = estimateComment;
    }

    public DateTime getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(DateTime estimateDate) {
        this.estimateDate = estimateDate;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
