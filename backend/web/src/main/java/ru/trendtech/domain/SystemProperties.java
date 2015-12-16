package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 05.11.2014.
 */
@Entity
@Table(name = "properties")
public class SystemProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "prop_name")
    private String propName;

    @Column(name = "prop_value")
    private String propValue;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }
}
