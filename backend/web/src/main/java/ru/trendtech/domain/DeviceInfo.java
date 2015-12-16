package ru.trendtech.domain;

import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "device_info")
public class DeviceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "device_type")
    private Type type = Type.UNKNOWN;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "device_state")
    private State state = State.INACTIVE;

    @Column(name = "time_updated")
    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastUpdate;

    @Column(name = "socket_id")
    private String socket_id;

    @Column(name = "last_failed_data")
    private String last_failed_data;

    @Column(name = "push_id")
    private String pushId;


    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getSocket_id() {
        return socket_id;
    }

    public void setSocket_id(String socket_id) {
        this.socket_id = socket_id;
    }

    public String getLast_failed_data() {
        return last_failed_data;
    }

    public void setLast_failed_data(String last_failed_data) {
        this.last_failed_data = last_failed_data;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(DateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

//

    @Override
    public String toString() {
        return "";
        /*
                "DeviceInfo{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", type=" + type +
                ", state=" + state +
                ", lastUpdate=" + lastUpdate +
                '}';
        */
    }

    public static enum State {
        ACTIVE,
        INACTIVE,;
    }

    public static enum Type {
        UNKNOWN(0),
        ANDROID(1),
        APPLE(2),
        ANDROID_CLIENT(3),
        TERMINAL_CLIENT(4),;

        private final int id;

        Type(int id) {
            this.id = id;
        }

        public static Type getDeviceType(int value) {
            Type result = null;
            for (Type type : Type.values()) {
                if (type.getId() == value) {
                    result = type;
                    break;
                }
            }
            return result;
        }

        public int getId() {
            return id;
        }
    }
}
