package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by petr on 11.11.2014.
 */


@Entity
@Table(name = "mission_canceled")
public class MissionCanceled {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cancel_by", nullable = false)
    private String cancelBy;

    @Column(name = "mission_id", nullable = false)
    private Long missionId;

    @Column(name = "cancel_by_id") // если сервак, то нулл
    private Long cancelById;

    @Column(name = "time_of_canceled")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfCanceled;

    @Column(name = "reason")
    private String reason;

    @Column(name = "state_before_canceled")
    private String stateBeforeCanceled;

    @OneToOne
    @JoinColumn(name = "reason_id")
    private Reason reasonInfo;

    public Reason getReasonInfo() {
        return reasonInfo;
    }

    public void setReasonInfo(Reason reasonInfo) {
        this.reasonInfo = reasonInfo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStateBeforeCanceled() {
        return stateBeforeCanceled;
    }

    public void setStateBeforeCanceled(String stateBeforeCanceled) {
        this.stateBeforeCanceled = stateBeforeCanceled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCancelBy() {
        return cancelBy;
    }

    public void setCancelBy(String cancelBy) {
        this.cancelBy = cancelBy;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public Long getCancelById() {
        return cancelById;
    }

    public void setCancelById(Long cancelById) {
        this.cancelById = cancelById;
    }

    public DateTime getTimeOfCanceled() {
        return timeOfCanceled;
    }

    public void setTimeOfCanceled(DateTime timeOfCanceled) {
        this.timeOfCanceled = timeOfCanceled;
    }
}
