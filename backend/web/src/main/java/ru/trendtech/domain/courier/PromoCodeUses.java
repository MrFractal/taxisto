package ru.trendtech.domain.courier;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ru.trendtech.domain.Client;

import javax.persistence.*;

/**
 * Created by petr on 06.11.2015.
 */
@Entity
@Table(name = "c_promo_code_uses")
public class PromoCodeUses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne
    @JoinColumn(name = "c_promo_code_id", nullable = false)
    private PromoCodeCourier promoCode;

    @Column(name = "date_of_use")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateOfUse;

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

    public PromoCodeCourier getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(PromoCodeCourier promoCode) {
        this.promoCode = promoCode;
    }

    public DateTime getDateOfUse() {
        return dateOfUse;
    }

    public void setDateOfUse(DateTime dateOfUse) {
        this.dateOfUse = dateOfUse;
    }
}
