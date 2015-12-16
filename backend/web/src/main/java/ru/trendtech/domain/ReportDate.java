package ru.trendtech.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by petr on 12.11.2014.
 */

@Embeddable
public class ReportDate {
    @Column(name = "dates")
    private String dates;

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }
}
