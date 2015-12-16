package ru.trendtech.domain;

import javax.persistence.*;

/**
 * Created by petr on 07.11.2014.
 */

@Entity
@Table(name = "versions_app")
public class VersionsApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "status_version", nullable = false)
    private int statusVersion;

    @Column(name = "client_type", nullable = false)
    private String clientType;

    @Column(name = "show_tarif")
    private String showTarif;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatusVersion() {
        return statusVersion;
    }

    public void setStatusVersion(int statusVersion) {
        this.statusVersion = statusVersion;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getShowTarif() {
        return showTarif;
    }

    public void setShowTarif(String showTarif) {
        this.showTarif = showTarif;
    }
}
