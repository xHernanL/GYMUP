package com.bitgymup.gymup;

import java.io.Serializable;

public class Gym implements Serializable {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String mobileNumber;
    private String rut;
    private String street;
    private String portNumber;
    private String city;
    private String country;
    private String fullAddress;
    private String location;
    //private ImageView logo;


    public Gym(String id, String name, String email, String phoneNumber, String mobileNumber, String rut, String street, String portNumber, String city, String country, String fullAddress, String location) {
        this.id           = id;
        this.name         = name;
        this.email        = email;
        this.phoneNumber  = phoneNumber;
        this.mobileNumber = mobileNumber;
        this.rut          = rut;
        this.street       = street;
        this.portNumber   = portNumber;
        this.city         = city;
        this.country      = country;
        this.fullAddress  = fullAddress;
        this.location     = location;
    }

    public String getId() {return id;}

    public void setId(String idgym) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRut() {
        return rut;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }
}
