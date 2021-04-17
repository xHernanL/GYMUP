package com.bitgymup.gymup.users;

import java.io.Serializable;

public class Promotion implements Serializable{
    private String id;
    private String title;
    private String promotion;
    private String gymName;

    public Promotion(String id, String title, String promotion, String gymName) {
        this.id        = id;
        this.title     = title;
        this.promotion = promotion;
        this.gymName   = gymName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }
}
