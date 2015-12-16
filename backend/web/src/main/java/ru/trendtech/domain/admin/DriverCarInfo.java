package ru.trendtech.domain.admin;

import javax.persistence.*;

@Entity
@Table(name = "car_info")
public class DriverCarInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


}
