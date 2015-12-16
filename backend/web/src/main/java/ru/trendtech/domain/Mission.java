package ru.trendtech.domain;

import com.google.common.base.Objects;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import org.joda.time.DateTime;
import ru.trendtech.domain.billing.MissionRate;
import ru.trendtech.domain.billing.PaymentType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mission")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time_starting")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    //@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
    private DateTime timeOfStarting;

    @Column(name = "time_requesting")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfRequesting;

    @Column(name = "time_assigning")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfAssigning;

    @Column(name = "time_arriving")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfArriving;

    @Column(name = "time_seating")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfSeating; // means of this field  ??????

    @Column(name = "time_finishing")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfFinishing;

    @Column(name = "time_ready_to_go")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfReadyToGo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "expected_arrival_times", joinColumns = {@JoinColumn(name = "mission_id")})
    private List<Integer> expectedArrivalTimes = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "from_address")),
            @AttributeOverride(name = "latitude", column = @Column(name = "from_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "from_long")),
            @AttributeOverride(name = "city", column = @Column(name = "from_city")),
            @AttributeOverride(name = "region", column = @Column(name = "from_region"))
    })
    private Location locationFrom = new Location();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "to_address")),
            @AttributeOverride(name = "latitude", column = @Column(name = "to_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "to_long")),
            @AttributeOverride(name = "city", column = @Column(name = "to_city")),
            @AttributeOverride(name = "region", column = @Column(name = "to_region"))
    })
    private Location locationTo = new Location();

    @Column(name = "payment_type")
    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType = PaymentType.UNKNOWN;

    @Column(name = "comments")
    private String comments;

    @Embedded
    private Score score = new Score();

    @Embedded
    private Statistics statistics = new Statistics();

    @OneToOne
    private Driver driverInfo;

    @ManyToOne(optional = false)
    private Client clientInfo;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column(name = "payment_state_card", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStateCard paymentStateCard = PaymentStateCard.NONE;

    @Column(name = "booking_state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BookingState bookingState = BookingState.NONE;

    @Column(name = "auto_class", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AutoClass autoClass;

    @OneToOne
    private MissionRate missionRate;

    @Column(name = "porch_number")
    private int porchNumber;

    @Column(name = "count_notified", columnDefinition = "int default 0")
    private int countNotified = 0;

    @Column(name = "late_driver_booked_min" , columnDefinition = "int default 0")
    private int lateDriverBookedMin=0;

    @OneToOne
    @JoinColumn(name = "booked_driver_id")
    private Driver bookedDriverId;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "mission_id", insertable = true, updatable = true)
    private List<MissionAddresses> missionAddresses = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "terminal_id")
    private Terminal terminal;

    @Column(name = "status_delete" , nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean statusDelete;

    @Column(name = "test_order" , nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean testOrder;

    @Column(name = "is_late", columnDefinition = "BIT DEFAULT 0", length = 1)
    private Boolean isLate=false;

    @Column(name = "is_booked", columnDefinition = "BIT DEFAULT 0", length = 1)
    private Boolean isBooked;

    @Column(name = "coast")
    private String coast;

    @OneToOne
    @JoinColumn(name = "multiple_id")
    private MultipleMission multipleMission;

    @OneToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "time_is_after", nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean timeIsAfter;

    @OneToOne
    @JoinColumn(name = "taxopark_id")
    private TaxoparkPartners taxopark;

    public TaxoparkPartners getTaxopark() {
        return taxopark;
    }

    public void setTaxopark(TaxoparkPartners taxopark) {
        this.taxopark = taxopark;
    }

    public boolean isTimeIsAfter() {
        return timeIsAfter;
    }

    public void setTimeIsAfter(boolean timeIsAfter) {
        this.timeIsAfter = timeIsAfter;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getCoast() {
        return coast;
    }

    public void setCoast(String coast) {
        this.coast = coast;
    }

    public MultipleMission getMultipleMission() {
        return multipleMission;
    }

    public void setMultipleMission(MultipleMission multipleMission) {
        this.multipleMission = multipleMission;
    }

    public Boolean isBooked() {
        return isBooked;
    }

    public void setIsBooked(Boolean isBooked) {
        this.isBooked = isBooked;
    }

    public Boolean getIsLate() {
        return isLate;
    }

    public void setIsLate(Boolean isLate) {
        this.isLate = isLate;
    }

    public boolean isTestOrder() {
        return testOrder;
    }

    public void setTestOrder(boolean testOrder) {
        this.testOrder = testOrder;
    }

    public boolean isStatusDelete() {
        return statusDelete;
    }

    public void setStatusDelete(boolean statusDelete) {
        this.statusDelete = statusDelete;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public List<MissionAddresses> getMissionAddresses() {
        return missionAddresses;
    }

    public void setMissionAddresses(List<MissionAddresses> missionAddresses) {
        this.missionAddresses = missionAddresses;
    }

    public int getLateDriverBookedMin() {
        return lateDriverBookedMin;
    }

    public void setLateDriverBookedMin(int lateDriverBookedMin) {
        this.lateDriverBookedMin = lateDriverBookedMin;
    }

    public Driver getBookedDriverId() {
        return bookedDriverId;
    }

    public void setBookedDriverId(Driver bookedDriverId) {
        this.bookedDriverId = bookedDriverId;
    }

    public int getCountNotified() {
        return countNotified;
    }

    public void setCountNotified(int countNotified) {
        this.countNotified = countNotified;
    }

    public MissionRate getMissionRate() {
        return missionRate;
    }

    public void setMissionRate(MissionRate missionRate) {
        this.missionRate = missionRate;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public BookingState getBookingState() {
        return bookingState;
    }

    public void setBookingState(BookingState bookingState) {
        this.bookingState = bookingState;
    }

    public DateTime getTimeOfStarting() {
        return timeOfStarting;
    }

    public void setTimeOfStarting(DateTime timeOfStarting) {
        this.timeOfStarting = timeOfStarting;
    }

    public Score getScore() {
        return score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getTimeOfRequesting() {
        return timeOfRequesting;
    }

    public void setTimeOfRequesting(DateTime timeOfRequesting) {
        this.timeOfRequesting = timeOfRequesting;
    }

    public DateTime getTimeOfAssigning() {
        return timeOfAssigning;
    }

    public void setTimeOfAssigning(DateTime timeOfAssigning) {
        this.timeOfAssigning = timeOfAssigning;
    }

    public DateTime getTimeOfArriving() {
        return timeOfArriving;
    }

    public void setTimeOfArriving(DateTime timeOfArriving) {
        this.timeOfArriving = timeOfArriving;
    }

    public DateTime getTimeOfSeating() {
        return timeOfSeating;
    }

    public void setTimeOfSeating(DateTime timeOfSeating) {
        this.timeOfSeating = timeOfSeating;
    }

    public DateTime getTimeOfFinishing() {
        return timeOfFinishing;
    }

    public void setTimeOfFinishing(DateTime timeOfFinishing) {
        this.timeOfFinishing = timeOfFinishing;
    }

    public List<Integer> getExpectedArrivalTimes() {
        return expectedArrivalTimes;
    }

    public void setExpectedArrivalTimes(List<Integer> expectedArrivalTimes) {
        this.expectedArrivalTimes = expectedArrivalTimes;
    }

    public Location getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(Location locationFrom) {
        this.locationFrom = locationFrom;
    }

    public Location getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(Location locationTo) {
        this.locationTo = locationTo;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Driver getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(Driver driverInfo) {
        this.driverInfo = driverInfo;
    }

    public Client getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(Client clientInfo) {
        this.clientInfo = clientInfo;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    public PaymentStateCard getPaymentStateCard() {
        return paymentStateCard;
    }

    public void setPaymentStateCard(PaymentStateCard paymentStateCard) {
        this.paymentStateCard = paymentStateCard;
    }

    //    public List<DriverLocation> getLocations() {
//        return locations;
//    }
//
//    public void setLocations(List<DriverLocation> locations) {
//        this.locations = locations;
//    }
//
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public AutoClass getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(AutoClass autoClass) {
        this.autoClass = autoClass;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id;
//                +
//                ", timeOfRequesting=" + timeOfRequesting +
//                ", timeOfAssigning=" + timeOfAssigning +
//                ", timeOfArriving=" + timeOfArriving +
//                ", timeOfSeating=" + timeOfSeating +
//                ", timeOfFinishing=" + timeOfFinishing +
//                ", expectedArrivalTimes=" + expectedArrivalTimes +
//                ", locationFrom=" + locationFrom +
//                ", locationTo=" + locationTo +
//                ", paymentType=" + paymentType +
//                ", comments='" + comments + '\'' +
//                ", driverInfo=" + driverInfo +
//                ", clientInfo=" + clientInfo +
//                ", state=" + state +
//                ", autoClass=" + autoClass +
//                '}';
    }

    public DateTime getTimeOfReadyToGo() {
        return timeOfReadyToGo;
    }

    public void setTimeOfReadyToGo(DateTime timeOfReadyToGo) {
        this.timeOfReadyToGo = timeOfReadyToGo;
    }

    public int getPorchNumber() {
        return porchNumber;
    }

    public void setPorchNumber(int porchNumber) {
        this.porchNumber = porchNumber;
    }



    public static enum BookingState {
        NONE,
        WAITING,
        DRIVER_ASSIGNED,
        DRIVER_NOTIFIED,
        DRIVER_APPROVED,
        DRIVER_DECLINED,
        STARTED,
    }



    public static enum State {
        NONE,
        BOOKED,
        NEW,
        ASSIGNED,
        ARRIVED,
        READY_TO_GO,
        IN_TRIP,
        IN_TRIP_PAUSED,
        IN_TRIP_END,
        COMPLETED,
        CANCELED,
        AUTO_SEARCH,
        TURBO,
        FAILED,;
    }



    public static enum PaymentStateCard {
        NONE,
        WAIT_PAYMENT_CLIENT,
        PAYMENT_CLIENT_YES,
        PAYMENT_CLIENT_NO,
        GOOD_PAYMENT,
        FAILED_PAYMENT
    }




    @Embeddable
    public static class PauseInfo {
        @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
        @Column(name = "start_pause")
        private DateTime startPause;

        @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
        @Column(name = "end_pause")
        private DateTime endPause;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "latitude", column = @Column(name = "pause_address")),
                @AttributeOverride(name = "latitude", column = @Column(name = "pause_lat")),
                @AttributeOverride(name = "longitude", column = @Column(name = "pause_long"))
        })
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public DateTime getStartPause() {
            return startPause;
        }

        public void setStartPause(DateTime startPause) {
            this.startPause = startPause;
        }

        public DateTime getEndPause() {
            return endPause;
        }

        public void setEndPause(DateTime endPause) {
            this.endPause = endPause;
        }
    }



    @Embeddable
    public static class Statistics {
        @Columns(columns = {@Column(name = "price_expected_currency"), @Column(name = "price_expected_amount")})
        @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmountAndCurrency")
        private Money priceExpected;

        @Columns(columns = {@Column(name = "price_in_fact_currency"), @Column(name = "price_in_fact_amount")})
        @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmountAndCurrency")
        private Money priceInFact;

        @Columns(columns = {@Column(name = "sum_increase_currency"), @Column(name = "sum_increase")})
        @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmountAndCurrency")
        private Money sumIncrease;

        @Column(name = "total_time")
        private int totalTime;

        @Column(name = "over_waiting_time")
        private int overWaitingTime;

        @Column(name = "distance_expected")
        private long distanceExpected;

        @Column(name = "distance_real")
        private int distanceInFact;

        @Column(name = "waiting_over")
        private int waitingOverFree;

        @Column(name = "fixed_price", columnDefinition = "BIT DEFAULT 1", length = 1) // f:add : columnDefinition = "boolean default true" , nullable = false
        private boolean fixedPrice;

        @Column(name = "use_bonuses", columnDefinition = "BIT DEFAULT 0", length = 1)
        private boolean useBonuses;

        @Enumerated(value = EnumType.STRING)
        @ElementCollection(targetClass = MissionService.class)
        @CollectionTable(name = "services_expected", joinColumns = {@JoinColumn(name = "mission_id")})
        @LazyCollection(LazyCollectionOption.FALSE)
        private List<MissionService> servicesExpected = new ArrayList<>();

        @Enumerated(value = EnumType.STRING)
        @ElementCollection(targetClass = MissionService.class)
        @CollectionTable(name = "services_used", joinColumns = {@JoinColumn(name = "mission_id")})
        @LazyCollection(LazyCollectionOption.FALSE)
        private List<MissionService> servicesInFact = new ArrayList<>();

        @Enumerated(value = EnumType.STRING)
        @ElementCollection(targetClass = PauseInfo.class)
        @CollectionTable(name = "pauses", joinColumns = {@JoinColumn(name = "mission_id")})
        @LazyCollection(LazyCollectionOption.FALSE)
        private List<PauseInfo> pauses = new ArrayList<>();


        public Money getSumIncrease() {
            return sumIncrease;
        }

        public void setSumIncrease(Money sumIncrease) {
            this.sumIncrease = sumIncrease;
        }

        public boolean isUseBonuses() {
            return useBonuses;
        }

        public void setUseBonuses(boolean useBonuses) {
            this.useBonuses = useBonuses;
        }

        public int getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(int totalTime) {
            this.totalTime = totalTime;
        }

        public long getDistanceExpected() {
            return distanceExpected;
        }

        public void setDistanceExpected(long distanceExpected) {
            this.distanceExpected = distanceExpected;
        }

        public int getDistanceInFact() {
            return distanceInFact;
        }

        public void setDistanceInFact(int distanceInFact) {
            this.distanceInFact = distanceInFact;
        }

        public int getWaitingOverFree() {
            return waitingOverFree;
        }

        public void setWaitingOverFree(int waitingOver) {
            this.waitingOverFree = waitingOver;
        }

        public boolean isFixedPrice() {
            return fixedPrice;
        }

        public void setFixedPrice(boolean fixedPrice) {
            this.fixedPrice = fixedPrice;
        }

        public List<MissionService> getServicesExpected() {
            return servicesExpected;
        }

        public void setServicesExpected(List<MissionService> expectedMissionServices) {
            this.servicesExpected = expectedMissionServices;
        }

        public List<MissionService> getServicesInFact() {
            return servicesInFact;
        }

        public void setServicesInFact(List<MissionService> usedServices) {
            this.servicesInFact = usedServices;
        }

        public Money getPriceExpected() {
            return priceExpected;
        }

        public void setPriceExpected(Money priceExpected) {
            this.priceExpected = priceExpected;
        }

        public Money getPriceInFact() {
            return priceInFact;
        }

        public void setPriceInFact(Money priceInFact) {
            this.priceInFact = priceInFact;
        }

        public int getOverWaitingTime() {
            return overWaitingTime;
        }

        public void setOverWaitingTime(int overWaitingTime) {
            this.overWaitingTime = overWaitingTime;
        }

        public List<PauseInfo> getPauses() {
            return pauses;
        }

        public void setPauses(List<PauseInfo> pauses) {
            this.pauses = pauses;
        }
    }


    /**
     * File created by max on 07/05/2014 4:13.
     */

    @Embeddable
    public static class Score {
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

        public String getEstimateComment() {
            return estimateComment;
        }

        public void setEstimateComment(String estimateComment) {
            this.estimateComment = estimateComment;
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
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mission)) return false;
        Mission mission = (Mission) o;
        return Objects.equal(isStatusDelete(), mission.isStatusDelete()) &&
                Objects.equal(isTestOrder(), mission.isTestOrder()) &&
                Objects.equal(getId(), mission.getId()) &&
                Objects.equal(getTimeOfStarting(), mission.getTimeOfStarting()) &&
                Objects.equal(getTimeOfRequesting(), mission.getTimeOfRequesting()) &&
                Objects.equal(getLocationFrom(), mission.getLocationFrom()) &&
                Objects.equal(getLocationTo(), mission.getLocationTo()) &&
                Objects.equal(getPaymentType(), mission.getPaymentType()) &&
                Objects.equal(getDriverInfo(), mission.getDriverInfo()) &&
                Objects.equal(getClientInfo(), mission.getClientInfo()) &&
                Objects.equal(getState(), mission.getState()) &&
                Objects.equal(getPaymentStateCard(), mission.getPaymentStateCard()) &&
                Objects.equal(getBookingState(), mission.getBookingState()) &&
                Objects.equal(getAutoClass(), mission.getAutoClass()) &&
                Objects.equal(getBookedDriverId(), mission.getBookedDriverId()) &&
                Objects.equal(getIsLate(), mission.getIsLate());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getTimeOfStarting(), getTimeOfRequesting(), getLocationFrom(), getLocationTo(), getPaymentType(), getDriverInfo(), getClientInfo(), getState(), getPaymentStateCard(), getBookingState(), getAutoClass(), getBookedDriverId(), isStatusDelete(), isTestOrder(), getIsLate());
    }
}