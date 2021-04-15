package com.bitgymup.gymup.users;

import java.io.Serializable;

public class Promotion implements Serializable{
    private String id;
    private String title;
    private String promotion;

    public Promotion(String id, String title, String promotion) {
        this.id          = id;
        this.title       = title;
        this.promotion   = promotion;
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
}
