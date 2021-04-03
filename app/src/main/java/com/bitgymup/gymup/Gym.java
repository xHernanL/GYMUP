package com.bitgymup.gymup;

import java.io.Serializable;

public class Gym implements Serializable {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String mobileNumber;
    private String rut;
    //private ImageView logo;


    public Gym(String id, String name, String email, String phoneNumber, String mobileNumber, String rut) {
        this.id           = id;
        this.name         = name;
        this.email        = email;
        this.phoneNumber  = phoneNumber;
        this.mobileNumber = mobileNumber;
        this.rut          = rut;
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

    public void setRut(String rut) {
        this.rut = rut;
    }
}
