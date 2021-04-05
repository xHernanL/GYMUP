package com.bitgymup.gymup.admin;

public class clients {
    private String id_user,username, status;

    public clients() {
    }

    public clients(String id_user, String username, String status) {
        this.id_user = id_user;
        this.username = username;
        this.status = status;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
