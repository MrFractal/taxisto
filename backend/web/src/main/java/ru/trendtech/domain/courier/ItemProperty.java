package ru.trendtech.domain.courier;

import javax.persistence.*;

/**
 * Created by petr on 07.09.2015.
 */
@Entity
@Table(name = "c_item_property")
public class ItemProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name_poperty")
    private String namePoperty;

    @Column(name = "alcohol" , nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean alcohol; // 1 - данный товар бухло

    /*
       todo: somethings else
    */

    public String getNamePoperty() {
        return namePoperty;
    }

    public void setNamePoperty(String namePoperty) {
        this.namePoperty = namePoperty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAlcohol() {
        return alcohol;
    }

    public void setAlcohol(boolean alcohol) {
        this.alcohol = alcohol;
    }
}
