package ru.trendtech.domain.courier;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ru.trendtech.domain.Client;
import ru.trendtech.services.promo.PromoCode;

import javax.persistence.*;

/**
 * Created by petr on 30.10.2015.
 */
@Entity
@Table(name = "c_promo_code_courier")
public class PromoCodeCourier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value", unique = true, nullable = false, length = 8)
    private String value;

    @Column(name = "amount")
    private int amount;

    @OneToOne
    @JoinColumn(name = "from_client_id")
    private Client fromClient;

    @OneToOne
    @JoinColumn(name = "to_client_id")
    private Client toClient;

    @Column(name = "channel", length = 32)
    private String channel;

    @Column(name = "available_use_count" , nullable = false)
    private int availableUseCount = 0; // сколько раз можно юзать этот промо

    @Column(name = "use_in_fact")
    private int useCountInFact = 0; // сколько раз заюзали

    @Column(name = "generate_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfGenerate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Client getFromClient() {
        return fromClient;
    }

    public void setFromClient(Client fromClient) {
        this.fromClient = fromClient;
    }

    public Client getToClient() {
        return toClient;
    }

    public void setToClient(Client toClient) {
        this.toClient = toClient;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getAvailableUseCount() {
        return availableUseCount;
    }

    public void setAvailableUseCount(int availableUseCount) {
        this.availableUseCount = availableUseCount;
    }

    public int getUseCountInFact() {
        return useCountInFact;
    }

    public void setUseCountInFact(int useCountInFact) {
        this.useCountInFact = useCountInFact;
    }

    public DateTime getTimeOfGenerate() {
        return timeOfGenerate;
    }

    public void setTimeOfGenerate(DateTime timeOfGenerate) {
        this.timeOfGenerate = timeOfGenerate;
    }
}
