package ru.trendtech.services.async;

import java.io.IOException;

/**
 * Created by petr on 30.07.2015.
 */
public class Report {
    private String name;
    private String description;
    private int count;

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}