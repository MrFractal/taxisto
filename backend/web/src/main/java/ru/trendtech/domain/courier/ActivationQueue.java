package ru.trendtech.domain.courier;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ru.trendtech.domain.Client;

import javax.persistence.*;

/**
 * Created by petr on 02.09.2015.
 */
@Entity
@Table(name = "c_activation_queue")
public class ActivationQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private Client client;

    // todo: requestING
    @Column(name = "time_of_request", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfRequest;

    @Column(name = "time_of_activation")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfActivation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public DateTime getTimeOfRequest() {
        return timeOfRequest;
    }

    public void setTimeOfRequest(DateTime timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public DateTime getTimeOfActivation() {
        return timeOfActivation;
    }

    public void setTimeOfActivation(DateTime timeOfActivation) {
        this.timeOfActivation = timeOfActivation;
    }
}
