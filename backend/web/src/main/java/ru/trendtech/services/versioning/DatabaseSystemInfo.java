package ru.trendtech.services.versioning;

import javax.persistence.*;
import java.util.List;

/**
 * User: max
 * Date: 1/18/13
 * Time: 10:12 PM
 */
@Entity
@Table(name = "db_system_info")
public class DatabaseSystemInfo {
    private static final String SEPARATOR = ";";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "applied_scripts")
    private String appliedScripts;

    @Column(name = "applied_methods")
    private String appliedMethods;

    private static String addValue(String value, String str, String separator) {
        if (value != null && !"".equals(value)) {
            value = value + separator + str;
        } else {
            value = str;
        }
        return value;
    }

    public String getAppliedScripts() {
        return appliedScripts;
    }

    public void setAppliedScripts(String appliedScripts) {
        this.appliedScripts = appliedScripts;
    }

    public String getAppliedMethods() {
        return appliedMethods;
    }

    public void setAppliedMethods(String appliedMethods) {
        this.appliedMethods = appliedMethods;
    }

    @Transient
    public void addAppliedScripts(List<String> values) {
        this.appliedScripts = addValues(this.appliedScripts, values, SEPARATOR);
    }

    @Transient
    public void addAppliedMethods(List<String> values) {
        this.appliedMethods = addValues(this.appliedMethods, values, SEPARATOR);
    }

    private String addValues(String str, List<String> values, String separator) {
        String result = str;
        for (String value : values) {
            result = addValue(result, value, separator);
        }
        return result;
    }

}
