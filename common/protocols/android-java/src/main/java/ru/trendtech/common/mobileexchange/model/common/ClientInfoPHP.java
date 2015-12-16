package ru.trendtech.common.mobileexchange.model.common;

public class ClientInfoPHP {
    private long id;
    private String firstName;
    private String lastName;
    private String picture;
    private String pictureUrl;
    private String country; // formatted on server side
    private String city; // formatted on server side
    private String phone;
    private int birthdayDay;
    private int birthdayMonth;
    private int birthdayYear;
    private String email;
    private String gender; // true - male, false - female
    private int rating;
    private String password;
    private double bonuses;
    private long registrationDate;
    private long blockingDate;
    private double amount;

    public double getAmount() {return amount;}

    public void setAmount(double amount) {this.amount = amount;}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBirthdayDay() {
        return birthdayDay;
    }

    public void setBirthdayDay(int birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public int getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(int birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public int getBirthdayYear() {
        return birthdayYear;
    }

    public void setBirthdayYear(int birthdayYear) {
        this.birthdayYear = birthdayYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicture() {return picture;}

    public void setPicture(String picture) {this.picture = picture;}

    public String getPictureUrl() {return pictureUrl;}

    public void setPictureUrl(String pictureUrl) {this.pictureUrl = pictureUrl;}

    public double getBonuses() {
        return bonuses;
    }

    public void setBonuses(double bonuses) {
        this.bonuses = bonuses;
    }

    public long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public long getBlockingDate() {
        return blockingDate;
    }

    public void setBlockingDate(long blockingDate) {
        this.blockingDate = blockingDate;
    }

//    public boolean isGenderDefined() {
//        return this.gender != null;
//    }
//
//    public boolean isMale() {
//        return this.gender != null && this.gender;
//    }
//
//    public boolean isFemale() {
//        return this.gender != null && !this.gender;
//    }
}
