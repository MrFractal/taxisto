package ru.trendtech.common.mobileexchange.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 23.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverProfileInfo {
    private int growth;
    private int weight;
    private String familyStatus;
    private boolean childrens;
    private String hobby;
    private String dreem;
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(String familyStatus) {
        this.familyStatus = familyStatus;
    }

    public boolean isChildrens() {
        return childrens;
    }

    public void setChildrens(boolean childrens) {
        this.childrens = childrens;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getDreem() {
        return dreem;
    }

    public void setDreem(String dreem) {
        this.dreem = dreem;
    }
}
