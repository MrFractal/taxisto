package ru.trendtech.domain.dgis;


import ru.trendtech.domain.Mission;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by petr on 06.10.2014.
 */
@Entity
@Table(name = "dgis_grym_street")
public class DGISStreet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String nameStreet;

  @Column(name = "city_idx")
  private int cityIndex;
//  @Column(name = "feature_idx")
//  private Integer featureIndex;

    @OneToMany
    @JoinColumn(name = "street_idx")
    private List<DGISAddress> dgisAddressList = new ArrayList<>();


    public int getCityIndex() {
        return cityIndex;
    }

    public void setCityIndex(int cityIndex) {
        this.cityIndex = cityIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameStreet() {
        return nameStreet;
    }

    public void setNameStreet(String nameStreet) {
        this.nameStreet = nameStreet;
    }

//    public int getCityIndex() {
//        return cityIndex;
//    }
//
//    public void setCityIndex(int cityIndex) {
//        this.cityIndex = cityIndex;
//    }
//
//    public int getFeatureIndex() {
//        return featureIndex;
//    }
//
//    public void setFeatureIndex(int featureIndex) {
//        this.featureIndex = featureIndex;
//    }

    public List<DGISAddress> getDgisAddressList() {
        return dgisAddressList;
    }

    public void setDgisAddressList(List<DGISAddress> dgisAddressList) {
        this.dgisAddressList = dgisAddressList;
    }
}
