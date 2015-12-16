package ru.trendtech.domain.courier;

import ru.trendtech.domain.Location;

import javax.persistence.*;

/**
 * Created by petr on 20.08.2015.
 */
@Entity
@Table(name = "c_order_address")
public class OrderAddress{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private Location addressLocation = new Location();

    @Column(name = "street")
    private String street = "";

    @Column(name = "house")
    private String house = "";

    @Column(name = "housing")
    private String housing = "";

    @Column(name = "apartment")
    private String apartment = "";

    @OneToOne
    @JoinColumn(name = "c_order_id", nullable = false)
    private Order order;

    @Column(name = "contact_person")
    private String contactPerson = "";

    @Column(name = "contact_person_phone")
    private String contactPersonPhone = "";

    @Column(name = "comment")
    private String comment = "";

    @Column(name = "order_number", nullable = false)
    private int orderNumber;

    @Column(name = "is_to_address", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean isToAddress;

    @Column(name = "to_address", columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean to;

    @Column(name = "target_address_state")
    @Enumerated(value = EnumType.STRING)
    private TargetAddressState targetAddressState;


    public boolean isTo() {
        return to;
    }

    public void setTo(boolean to) {
        this.to = to;
    }

    public TargetAddressState getTargetAddressState() {
        return targetAddressState;
    }

    public void setTargetAddressState(TargetAddressState targetAddressState) {
        this.targetAddressState = targetAddressState;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public boolean isToAddress() {
        return isToAddress;
    }

    public void setIsToAddress(boolean isToAddress) {
        this.isToAddress = isToAddress;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getAddressLocation() {
        return addressLocation;
    }

    public void setAddressLocation(Location addressLocation) {
        this.addressLocation = addressLocation;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
