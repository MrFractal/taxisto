package ru.trendtech.domain.dgis;

import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Mission;

import javax.persistence.*;

/**
 * Created by petr on 06.10.2014.
 */

@Entity
@Table(name = "dgis_grym_address")
public class DGISAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "number")
    private String number;

    //@OneToOne
    //private DGISStreet dgisStreet;
    @OneToOne
    @JoinColumn(name = "street_idx")
    private DGISStreet dgisStreet;

    @OneToOne
    @JoinColumn(name = "feature_idx")
    private DGISMapBuilding dgisMapBuilding;


    public DGISStreet getDgisStreet() {
        return dgisStreet;
    }

    public void setDgisStreet(DGISStreet dgisStreet) {
        this.dgisStreet = dgisStreet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    //    public DGISStreet getDgisStreet() {
//        return dgisStreet;
//    }
//
//    public void setDgisStreet(DGISStreet dgisStreet) {
//        this.dgisStreet = dgisStreet;
//    }

    public DGISMapBuilding getDgisMapBuilding() {
        return dgisMapBuilding;
    }

    public void setDgisMapBuilding(DGISMapBuilding dgisMapBuilding) {
        this.dgisMapBuilding = dgisMapBuilding;
    }
}


